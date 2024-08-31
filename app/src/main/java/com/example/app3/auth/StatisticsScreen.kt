package com.example.app3.auth

import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.Details
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed

fun findMostFrequentGenre(seriesList: List<Details>): String? {
    val genreCountMap = mutableMapOf<String, Int>()
    seriesList.forEach { series ->
        series.genres?.forEach { genre ->
            genreCountMap[genre] = genreCountMap.getOrDefault(genre, 0) + 1
        }
    }
    return genreCountMap.maxByOrNull { it.value }?.key
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatCard(title: String, value: String) {
    val focusRequester = remember { FocusRequester() }
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardColors(
            containerColor = Color.DarkGray,
            contentColor = Color.DarkGray,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.DarkGray
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = inter_font,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = value,
                fontSize = 22.sp,
                fontFamily = inter_font,
                fontWeight = FontWeight.ExtraBold,
                color = ourRed,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .basicMarquee(animationMode = MarqueeAnimationMode.WhileFocused)
                    .focusRequester(focusRequester)
                    .focusable()
                    .clickable { focusRequester.requestFocus() },
            )
        }
    }
}

@Composable
fun StatScreen(innerPadding: PaddingValues, vm: FbViewModel) {
    val favoriteState = vm.favoriteState.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.Black)
    ) {
        if (favoriteState.list.isNotEmpty()) {
            val seriesCount = favoriteState.list.size
            val bestRating = favoriteState.list.sortedBy { it.rating }.last().name
            val bestGenre = findMostFrequentGenre(favoriteState.list)
            val stillGoing = favoriteState.list.count { it.status == "Running" }

            Text(
                text = "Your Account Stats",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = inter_font,
            )
            StatCard(title = "Total Series Loved", value = "$seriesCount")
            StatCard(title = "Highest-Rated Fave", value = "$bestRating")
            StatCard(title = "Go-To Genre", value = "$bestGenre")
            StatCard(title = "Currently Airing Faves", value = "$stillGoing")
        } else {
            Text(
                text = "You have not used SeriesTime enough,\nplease come back later!",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_warning_amber_24),
                contentDescription = "Warning image",
                tint = ourRed,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController, vm: FbViewModel, listVM: MyListViewModel, currUser: SharedPreferences, isTablet: Boolean) {

    val photoUrl = currUser.getString("picture", "")
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    if (!isTablet) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehaviour.nestedScrollConnection)
                .background(darkBlue),
            topBar = {
                Row(
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkBlue)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
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
                                contentDescription = "Localized description"
                            )
                        }
                }
            }
        ) { innerPadding ->
            StatScreen(innerPadding, vm)
        }
    } else {
        Row (
            modifier = Modifier.fillMaxSize()
        ){
            LeftMenu(DestinationScreen.Statistics, navController, photoUrl, listVM)
            TStatScreen(vm)
        }
    }
}

@Composable
fun TStatScreen(vm: FbViewModel) {
    val favoriteState = vm.favoriteState.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (favoriteState.list.isNotEmpty()) {
            val seriesCount = favoriteState.list.size
            val bestRating = favoriteState.list.sortedBy { it.rating }.last().name
            val bestGenre = findMostFrequentGenre(favoriteState.list)
            val stillGoing = favoriteState.list.count { it.status == "Running" }

            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Your Account Stats",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = inter_font,
            )
            Row (
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
            ){
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    StatCard(title = "Total Series Loved", value = "$seriesCount")
                    Spacer(modifier = Modifier.height(30.dp))
                    StatCard(title = "Highest-Rated Fave", value = "$bestRating")
                }
                Spacer(modifier = Modifier.width(30.dp))
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    StatCard(title = "Go-To Genre", value = "$bestGenre")
                    Spacer(modifier = Modifier.height(30.dp))
                    StatCard(title = "Currently Airing Faves", value = "$stillGoing")
                }
            }
        } else {
            Text(
                text = "You have not used SeriesTime enough,\nplease come back later!",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_warning_amber_24),
                contentDescription = "Warning image",
                tint = ourRed,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}