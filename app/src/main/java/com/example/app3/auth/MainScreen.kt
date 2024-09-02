package com.example.app3.auth

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.app3.DestinationScreen
import com.example.app3.R
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed

val annotatedText = buildAnnotatedString {
    withStyle(style = SpanStyle(color = Color.White)) {
        append("Never miss your ")
    }
    withStyle(style = SpanStyle(color = ourRed)) {
        append("stories")
    }
}

@Composable
fun VideoBackground(uri: Uri, isTablet: Boolean) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            VideoView(context).apply {
                setVideoURI(uri)
                setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = true
                    start()
                }
            }
        },
        modifier = if (!isTablet) Modifier.fillMaxHeight() else Modifier.fillMaxWidth()
    )
}

@Composable
fun MainScreen(navController: NavController, isTablet: Boolean) {

    val context = LocalContext.current

    val videoUri =
        if (!isTablet)
            Uri.parse("android.resource://${context.packageName}/${R.raw.intro_background_cut}")
        else
            Uri.parse("android.resource://${context.packageName}/${R.raw.intro_background}")

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        VideoBackground(uri = videoUri, isTablet)

        if (!isTablet) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "SERIES\nTIME",
                    style = TextStyle(
                        fontSize = 22.sp,
                        lineHeight = 24.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight(600),
                        color = Color.White,
                    ),
                    modifier = Modifier.padding(top = 37.dp, start = 37.dp)
                )

                Spacer(modifier = Modifier.height(420.dp))

                Text(
                    text = annotatedText,
                    style = TextStyle(
                        fontSize = 55.sp,
                        lineHeight = 53.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight(1000),
                        color = Color.White,
                    ),
                    modifier = Modifier.padding(horizontal = 37.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.sharp_arrow_forward_ios_24),
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 300.dp)
                        .size(75.dp)
                        .clickable {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            navController.navigate(DestinationScreen.Login.route)
                        }
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "SERIES\nTIME",
                    style = TextStyle(
                        fontSize = 36.sp,
                        lineHeight = 40.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight(700),
                        color = Color.White,
                    ),
                    modifier = Modifier.padding(top = 37.dp, start = 37.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(500.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = annotatedText,
                        style = TextStyle(
                            fontSize = 55.sp,
                            lineHeight = 53.sp,
                            fontFamily = inter_font,
                            fontWeight = FontWeight(1000),
                            color = Color.White,
                        ),
                        modifier = Modifier.padding(horizontal = 37.dp).padding(start = 50.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.sharp_arrow_forward_ios_24),
                        contentDescription = "Arrow",
                        modifier = Modifier
                            .size(75.dp)
                            .clickable {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                                navController.navigate(DestinationScreen.Login.route)
                            }
                    )
                }
            }
        }
    }
}