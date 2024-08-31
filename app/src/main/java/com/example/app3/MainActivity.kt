package com.example.app3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app3.auth.DetailScreen
import com.example.app3.auth.FavouriteScreen
import com.example.app3.auth.HomeScreen
import com.example.app3.auth.LoginScreen
import com.example.app3.auth.MainScreen
import com.example.app3.auth.MyListViewModel
import com.example.app3.auth.NotificationScreen
import com.example.app3.auth.ProfileScreen
import com.example.app3.auth.SearchScreen
import com.example.app3.auth.SignUpScreen
import com.example.app3.auth.StatisticsScreen
import com.example.app3.auth.createNotificationChannel
import com.example.app3.main.NotificationMessage
import com.example.app3.ui.theme.App3Theme
import com.facebook.CallbackManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var callbackManager: CallbackManager
    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        callbackManager = CallbackManager.Factory.create()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidthDp = displayMetrics.widthPixels / (displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        val isTablet = if (screenWidthDp >= 600) true else false
        requestedOrientation = if (isTablet) {
            android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        setContent {
            window.statusBarColor = getColor(R.color.black)
            window.navigationBarColor = getColor(R.color.black)
            App3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthenticationApp(callbackManager, isTablet)
                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

sealed class DestinationScreen(val route: String) {
    object Main: DestinationScreen("main")
    object SignUp: DestinationScreen("signup")
    object Login: DestinationScreen("login")
    object Home: DestinationScreen("home")
    object Favourite: DestinationScreen("favourite")
    object Profile: DestinationScreen("profile")
    object Detail: DestinationScreen("detail/{seriesId}") {
        fun createRoute(seriesId: String) = "detail/$seriesId"
    }
    object Search: DestinationScreen("search")
    object Notification: DestinationScreen("notification")
    object Statistics: DestinationScreen("statistics")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticationApp(callbackManager: CallbackManager, isTablet: Boolean) {
    val vm = hiltViewModel<FbViewModel>()
    val navController =  rememberNavController()

    val currUser = LocalContext.current.getSharedPreferences("UserData", Context.MODE_PRIVATE)
    val userEditor = currUser.edit()
    // Initialize the preferences only if they are not already set
    LaunchedEffect(Unit) {
        if (!currUser.contains("id"))
            userEditor.putLong("id", -1)
        if (!currUser.contains("name"))
            userEditor.putString("name", "")
        if (!currUser.contains("picture"))
            userEditor.putString("picture", "")

        userEditor.apply()
    }

    val apiViewModel: MainViewModel = viewModel()
    val viewState by apiViewModel.seriesState

    val listVM: MyListViewModel = viewModel()

    val recommenderVM : RecommenderViewModel = viewModel()

    NotificationMessage(vm)

    RunCheckFavoritesDaily(vm, listVM)

    val startPage = if (currUser.getLong("id", -1L) == -1L) DestinationScreen.Main.route else DestinationScreen.Home.route

    NavHost(navController = navController, startDestination = startPage) {
        composable(DestinationScreen.Main.route) {
            MainScreen(navController, currUser, isTablet)
        }
        composable(DestinationScreen.SignUp.route) {
            SignUpScreen(navController, vm, currUser, )
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController, vm, callbackManager, currUser, )
        }
        composable(DestinationScreen.Home.route) {
            HomeScreen(navController, viewState, vm, currUser, listVM, recommenderVM, isTablet)
        }
        composable(DestinationScreen.Favourite.route) {
            FavouriteScreen(navController, vm, currUser, listVM, isTablet)
        }
        composable(DestinationScreen.Profile.route) {
            ProfileScreen(navController, currUser, vm, listVM, isTablet)
        }
        composable(DestinationScreen.Detail.route) { backStackEntry ->
            val seriesId = backStackEntry.arguments?.getString("seriesId")
            seriesId?.let {
                DetailScreen(navController, vm, listVM, it, currUser, isTablet)
            }
        }
        composable(DestinationScreen.Search.route) {
            SearchScreen(navController, currUser, listVM, recommenderVM, isTablet)
        }
        composable(DestinationScreen.Notification.route) {
            NotificationScreen(navController, currUser, listVM, isTablet)
        }
        composable(DestinationScreen.Statistics.route) {
            StatisticsScreen(navController, vm, listVM, currUser, isTablet)
        }
    }
}
