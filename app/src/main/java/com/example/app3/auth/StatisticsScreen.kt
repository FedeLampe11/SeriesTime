package com.example.app3.auth

import android.content.SharedPreferences
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.app3.FbViewModel

@Composable
fun StatisticsScreen(navController: NavController, vm: FbViewModel, currUser: SharedPreferences) {
    Text(text = "ANALA", color = Color.Yellow)
}