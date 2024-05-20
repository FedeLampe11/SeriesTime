package com.example.app3.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.app3.FbViewModel
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ScrollContent(innerPadding: PaddingValues, email: String) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .background(Color.Black)
    ) {
        Text(text = "Ciao", color = Color.White)
        repeat(40) {
            Text(text = email, color = Color.White)
        }
        Text(text = "ACM", color = Color.White)
        repeat(40) {
            Text(text = email, color = Color.White)
        }
        Text(text = "AC MILAN", color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(navController: NavController, vm: FbViewModel) {

    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val email = user?.email.toString()
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = darkBlue,
                    scrolledContainerColor = Color.Black,
                    actionIconContentColor = Color(0xFFFF003D),
                    navigationIconContentColor = Color(0xFFFF003D),
                    titleContentColor = Color(0xFFFF003D)
                ),
                title = { 
                    Text(
                        text = "SERIES TIME",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = inter_font,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Open left side menù */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open Personal Profile */ }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour
            )
        }
    ) {
        innerPadding -> ScrollContent(innerPadding, email)
    }
}