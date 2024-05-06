package com.example.app3.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.app3.FbViewModel
import com.example.app3.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SuccessScreen(navController: NavController, vm: FbViewModel) {

    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val email = user?.email.toString()

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        Text(text = "Ciao", color = Color.Black)
        Text(text = email, color = Color.Black)

    }
}