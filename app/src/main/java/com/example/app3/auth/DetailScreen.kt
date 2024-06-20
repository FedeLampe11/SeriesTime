package com.example.app3.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.Movie
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ScrollDetailPage(innerPadding: PaddingValues, series: Movie) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(innerPadding),
        
    ){
        item {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                // TODO: prendi immagine dalla serie
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Series Thumbnail",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = 100.dp)
                        .padding(top = 20.dp, bottom = 30.dp),
                )
            }

        }
        item {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = series.name,
                    fontSize = 30.sp,
                    fontFamily = inter_font,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2
                )

                // TODO: mettere controllo  se serie è nei preferiti
                if (true) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite button",
                        tint = ourRed,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                // TODO: togli dai preferiti
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite button",
                        tint = ourRed,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                            // TODO: metti nei preferiti
                        }
                    )
                }
            }
        }
        // TODO: metti gli episodi della serie

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, vm: FbViewModel, seriesId: Long) {
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val photoUrl = user?.photoUrl
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val series = favouriteList.find { it.id == seriesId }

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
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Go back to last page"
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
                        navController.navigate(DestinationScreen.Home.route)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Filled.Home,
                        tint = ourRed,
                        contentDescription = "Go to homepage"
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

                if (photoUrl != null)
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
                            contentDescription = "Localized description"
                        )
                    }
            }
        }
    ) {
        innerPadding ->
        if (series != null) {
            ScrollDetailPage(innerPadding, series)
        } else {
            Text(
                text = "Series not found",
                modifier = Modifier.padding(innerPadding),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = inter_font,
                color = Color.Red
            )
        }
    }
}