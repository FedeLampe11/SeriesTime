package com.example.app3.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.inter_font

// TODO download and insert background video

val text = buildAnnotatedString {
    withStyle(style = SpanStyle(color = Color.White)) {
        append("Never miss your ")
    }
    withStyle(style = SpanStyle(color = Color(0xFFFF003D))) {
        append("stories")
    }
}

@Composable
fun MainScreen(navController: NavController, vm: FbViewModel) {
    Column (
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(Color.Black)
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

    /*Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp)
    ) {
        Text(
            text = "Welcome Berlusconi!",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize =  40.sp
        )
        Spacer(modifier = Modifier.height(80.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Red, Color.White, Color.Red)
                    )
                )
        ) {
            Button(onClick = {
                navController.navigate(DestinationScreen.SignUp.route)
            },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    text = "Sign Up Bagai",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Red, Color.White, Color.Red)
                    )
                )
        ) {
            Button(onClick = {
                navController.navigate(DestinationScreen.Login.route)
            },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    text = "Login Pota!",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }*/
}