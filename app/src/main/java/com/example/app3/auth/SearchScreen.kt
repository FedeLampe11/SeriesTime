package com.example.app3.auth

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.MainViewModel
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollSearchPage(innerPadding: PaddingValues, navController: NavController, apiViewModel: MainViewModel) {

    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val history = remember { mutableStateListOf<String>() }

    val viewState = apiViewModel.searchState.value

    var toggle = false

    Column (
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .background(Color.Black)
    ){
        SearchBar(
            query = text,
            onQueryChange = { text = it },
            onSearch = {
                if (text.isNotEmpty())
                    if (history.contains(text)) {
                        history.remove(text)
                    }
                history.add(0, text)
                active = false
                // start fetching data
                apiViewModel.fetchSearch(text)
                toggle = true
            },
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier
                .fillMaxWidth()
                //.padding(innerPadding)
                .padding(10.dp),
            placeholder = {
                Text(
                    text = "Search...",
                    fontFamily = inter_font,
                    color = Color.LightGray,
                    fontWeight = FontWeight(500),
                    fontSize = 20.sp
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (active) {
                    IconButton(
                        onClick = {
                            if (text.isNotEmpty())
                                text = ""
                            else
                                active = false
                        }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear Search")
                    }
                }
            },
            colors = SearchBarDefaults.colors(
                containerColor = darkBlue,
                dividerColor = Color.LightGray,
                inputFieldColors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedContainerColor = Color(0x30FFFFFF),
                    unfocusedContainerColor = darkBlue,
                    focusedLeadingIconColor = Color.LightGray,
                    unfocusedLeadingIconColor = Color.LightGray,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray,
                    focusedTrailingIconColor = Color.Transparent,
                    unfocusedTrailingIconColor = Color.LightGray,
                    focusedTextColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray
                )
            )
        ) {
            history.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 20.dp)
                        .clickable {
                            text = it
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = it,
                        fontFamily = inter_font,
                        color = Color.LightGray,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    IconButton(
                        onClick = {
                            history.remove(it)
                        }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear Search", tint = Color.LightGray)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
                .padding(bottom = 15.dp)
        ) {
            when {
                viewState.loading -> {
                    if (toggle) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 15.dp),
                            color = ourRed,
                        )
                    }
                }

                viewState.error != null -> {
                    //Text(text = "Error occurred!")
                    Text(text = "${viewState.error}")
                }

                else -> {
                    if (viewState.obj.tv_shows.isNotEmpty()) {
                        LazyVerticalGrid(
                            GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(viewState.obj.tv_shows) { series ->
                                SeriesItem2(series, showName = true, navController)
                            }
                        }
                    } else {
                        Text(
                            text = "Sorry no series corresponds to '$text'",
                            fontFamily = inter_font,
                            color = Color.LightGray,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(30.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: FbViewModel, currUser: SharedPreferences) {

    val photoUrl = currUser.getString("picture", "")
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val apiViewModel: MainViewModel = viewModel()

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
                    onClick = { /* Do nothing */ }
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
                else {
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
        }
    ) {
        innerPadding -> ScrollSearchPage(innerPadding, navController, apiViewModel)
    }

}