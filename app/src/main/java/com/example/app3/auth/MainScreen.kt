package com.example.app3.auth

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

val text = buildAnnotatedString {
    withStyle(style = SpanStyle(color = Color.White)) {
        append("Never miss your ")
    }
    withStyle(style = SpanStyle(color = ourRed)) {
        append("stories")
    }
}

@Composable
fun VideoBackground(uri: Uri) {
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
        modifier = Modifier.fillMaxHeight()
    )
}

@Composable
fun MainScreen(navController: NavController, vm: FbViewModel) {

    // Initialize user
    Firebase.auth.signOut()

    val videoUri = Uri.parse("android.resource://${LocalContext.current.packageName}/${R.raw.intro_background_cut}")

    Box(
        modifier = Modifier.fillMaxSize().clipToBounds()
    ) {
        VideoBackground(uri = videoUri)

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize(),
        ) {
            Text(
                text = "SERIES\nTIME",
                style = TextStyle(
                    fontSize = 26.sp,
                    lineHeight = 24.sp,
                    fontFamily = inter_font,
                    fontWeight = FontWeight(700),
                    color = Color.White,
                ),
                modifier = Modifier.padding(top = 37.dp, start = 39.dp)
            )

            Spacer(modifier = Modifier.height(420.dp))

            Text(
                text = text,
                style = TextStyle(
                    fontSize = 60.sp,
                    lineHeight = 53.sp,
                    fontFamily = inter_font,
                    fontWeight = FontWeight(1000),
                    color = Color.White,
                ),
                modifier = Modifier.padding(start = 37.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.sharp_arrow_forward_ios_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 320.dp)
                    .size(300.dp)
                    .clickable {
                        navController.navigate(DestinationScreen.Login.route)
                    }
            )
        }
    }
}