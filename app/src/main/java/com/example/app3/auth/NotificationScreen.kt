package com.example.app3.auth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.Details
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import java.time.LocalDateTime

class MyListViewModel : ViewModel() {
    // Create a stateful list inside the ViewModel
    val items: SnapshotStateList<Details> = mutableStateListOf()
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Countdown Messages"
        val descriptionText = "Sending countdown messages"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Notifications", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun sendNotification(context: Context, series: Details, listVM: MyListViewModel) {
    val preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    val hour = preferences.getInt("NotificationHour", 9)
    val minute = preferences.getInt("NotificationMinute", 0)
    val currentTime = LocalDateTime.now()
    val targetTime = currentTime.withHour(hour).withMinute(minute).withSecond(0).withNano(0)

    val delay = if (targetTime.isAfter(currentTime)) {
        java.time.Duration.between(currentTime, targetTime).toMillis()
    } else {
        java.time.Duration.between(currentTime, targetTime.plusDays(1)).toMillis()
    }

    if (preferences.getBoolean("ReceiveNotifications", true)) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val notification = NotificationCompat.Builder(context, "Notifications")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("${series.name}!")
                .setContentText("New episode airs today!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(1, notification)
            listVM.items.add(series)
        }, delay)
    }
}

@Composable
fun NotificationRow(details: Details, navController: NavController, listVM: MyListViewModel) {
    Row (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .height(100.dp)
            .clickable {
                navController.navigate(DestinationScreen.Detail.createRoute(details.id))
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Image(
            painter = rememberAsyncImagePainter(details.thumbnail),
            contentDescription = "Series Thumbnail",
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
        Text(
            text = "New episode available for ${details.name}",
            color = Color.White,
            fontFamily = inter_font,
            fontSize = 18.sp,
            style = TextStyle(fontWeight = FontWeight.Normal),
            maxLines = 3,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                listVM.items.remove(details)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                tint = Color.White,
                contentDescription = "Remove notification"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationPage(innerPadding: PaddingValues, navController: NavController, listVM: MyListViewModel, vm: FbViewModel) {

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (listVM.items.isNotEmpty()) {
            items(listVM.items){
                details -> NotificationRow(details, navController, listVM)
            }
        } else {
            item {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Nothing to show here!",
                        color = Color.White,
                        fontFamily = inter_font,
                        fontSize = 20.sp,
                        style = TextStyle(fontWeight = FontWeight.Normal),
                        modifier = Modifier.clickable {
                            // Only for demonstration purposes
                            sendNotification(context, vm.favoriteState.value.list[0], listVM)
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController, vm: FbViewModel, currUser: SharedPreferences, listVM: MyListViewModel) {

    val photoUrl = currUser.getString("picture", "")
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        modifier = Modifier
            .nestedScroll(scrollBehaviour.nestedScrollConnection)
            .background(darkBlue),
        topBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue)
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Go back to last page"
                    )
                }
                IconButton(
                    onClick = {
                        listVM.items.clear()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        tint = if(listVM.items.isNotEmpty()) ourRed else Color.LightGray,
                        contentDescription = "Remove every notification"
                    )
                }
            }
        },
        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue)
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,

                ){
                IconButton(
                    onClick = {
                        navController.navigate(DestinationScreen.Search.route)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Filled.Search,
                        tint = ourRed,
                        contentDescription = "Go to search page"
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate(DestinationScreen.Favourite.route)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Filled.Favorite,
                        tint = ourRed,
                        contentDescription = "Go to favourite page"
                    )
                }

                if (photoUrl != null && photoUrl != "")
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { navController.navigate(DestinationScreen.Profile.route) }
                            .padding(horizontal = 11.dp)
                            .size(25.dp)
                            .clip(CircleShape)
                    )
                else
                    IconButton(
                        onClick = {
                            navController.navigate(DestinationScreen.Profile.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.Filled.AccountCircle,
                            tint = ourRed,
                            contentDescription = "Account image"
                        )
                    }
            }
        }
    ) {
            innerPadding -> NotificationPage(innerPadding, navController, listVM, vm)
    }
}