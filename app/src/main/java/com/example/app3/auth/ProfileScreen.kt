package com.example.app3.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.example.app3.ui.theme.switch_colors
import com.example.app3.userService
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@SuppressLint("WorldReadableFiles")
@Composable
fun ScrollProfilePage(innerPadding: PaddingValues, name: String, photoUrl: String?, navController: NavController, currUser: SharedPreferences) {

    val preferences = LocalContext.current.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val editor = preferences.edit()
    // Initialize the preferences only if they are not already set
    LaunchedEffect(Unit) {
        if (!preferences.contains("ReceiveNotifications")) {
            editor.putBoolean("ReceiveNotifications", true)
        }
        if (!preferences.contains("HideAlreadySeenEpisodes")) {
            editor.putBoolean("HideAlreadySeenEpisodes", false)
        }
        editor.apply()
    }

    var isNotificationChecked by remember { mutableStateOf(preferences.getBoolean("ReceiveNotifications", true)) }
    var isHideChecked by remember { mutableStateOf(preferences.getBoolean("HideAlreadySeenEpisodes", false)) }

    Column (
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(innerPadding)
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (photoUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        if (name != "null") {
            Text(
                text = name,
                color = Color.White,
                fontFamily = inter_font,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 12.dp)
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Receive notifications",
                fontFamily = inter_font,
                color = Color.White,
                maxLines = 2,
            )
            Switch(
                checked = isNotificationChecked,
                onCheckedChange = { checked ->
                    isNotificationChecked = checked
                    editor.putBoolean("ReceiveNotifications", checked)
                    editor.apply()
                },
                colors = switch_colors
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Hide already seen episodes",
                fontFamily = inter_font,
                color = Color.White,
                maxLines = 2,
            )
            Switch(
                checked = isHideChecked,
                onCheckedChange = { checked ->
                    isHideChecked = checked
                    editor.putBoolean("HideAlreadySeenEpisodes", checked)
                    editor.apply()
                },
                colors = switch_colors
            )
        }

        Button(
            onClick = {
                if (Firebase.auth.currentUser != null)
                    Firebase.auth.signOut()

                val edit = currUser.edit()
                edit.putLong("id", -1L)
                edit.putString("name", "")
                edit.putString("picture", "")
                edit.apply()

                navController.navigate(DestinationScreen.Main.route)
            },
            colors = ButtonColors(
                containerColor = ourRed,
                contentColor = Color.White,
                disabledContainerColor = ourRed,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_logout_25),
                contentDescription = "Log Out",
                colorFilter = ColorFilter.tint(Color.White)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Logout",
                fontFamily = inter_font,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, vm: FbViewModel, currUser: SharedPreferences) {
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }

    val name = currUser.getString("name", "")
    val photoUrl = currUser.getString("picture", "")
    Log.d("PRINT", "name $name, pic $photoUrl")

    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        modifier = Modifier
            .nestedScroll(scrollBehaviour.nestedScrollConnection)
            .background(darkBlue),
        topBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SERIES\nTIME",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight(700),
                        color = Color.White,
                    ),
                    modifier = Modifier
                        .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                        .clickable { navController.navigate(DestinationScreen.Home.route) }
                )
                IconButton(
                    onClick = {
                        navController.navigate(DestinationScreen.Notification.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        tint = Color.White,
                        contentDescription = "Go to notification page"
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

                IconButton(
                    onClick = { /* Do Nothing */}
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        tint = ourRed,
                        contentDescription = "Already in profile page"
                    )
                }
            }
        }
    ) {
        innerPadding -> ScrollProfilePage(innerPadding, name + "", photoUrl, navController, currUser)
    }
}
