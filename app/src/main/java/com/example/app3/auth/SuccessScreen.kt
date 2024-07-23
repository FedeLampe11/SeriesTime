package com.example.app3.auth

import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.Details
import com.example.app3.FbViewModel
import com.example.app3.MainViewModel
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import kotlinx.coroutines.delay

@Composable
fun IndicatorDots(size: Int, currentIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 400.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(size) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentIndex) 12.dp else 8.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(if (index == currentIndex) ourRed else Color.White)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(navController: NavController, viewState: MainViewModel.ReplyState) {
    val numberOfSeries = 5
    val pagerState = rememberPagerState(pageCount = { numberOfSeries })
    val topSeries = viewState.obj.take(numberOfSeries)

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000L) // Delay of 10 seconds
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % numberOfSeries)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            viewState.loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 15.dp),
                    color = ourRed,
                )
            }

            viewState.error != null -> {
                //Text(text = "Error occurred!")
                Text(text = "${viewState.error}")
            }

            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CarouselItem(topSeries[page], navController)
                    }
                }
                IndicatorDots(size = numberOfSeries, currentIndex = pagerState.currentPage)
            }
        }
    }
}

@Composable
fun SeriesItem(seriesDetails: Details, showName: Boolean, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(150.dp)
            .clickable {
                navController.navigate(DestinationScreen.Detail.createRoute(seriesDetails.id))
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberAsyncImagePainter(seriesDetails.thumbnail),
            contentDescription = "Series Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(bottom = 4.dp)
        )
        if (showName) {
            Text(
                text = seriesDetails.name + "",
                color = Color.White,
                fontFamily = inter_font,
                fontSize = 20.sp,
                style = TextStyle(fontWeight = FontWeight.Normal),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
fun CarouselItem(detail: Details, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()
            .clickable {
                navController.navigate(DestinationScreen.Detail.createRoute(detail.id))
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberAsyncImagePainter(detail.thumbnail),
            contentDescription = "Series Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(bottom = 4.dp)
        )
    }
}

@Composable
fun SeriesScreen(innerPadding: PaddingValues, navController: NavController, apiViewModel: MainViewModel, viewState: MainViewModel.ReplyState, vm: FbViewModel, currUser: SharedPreferences){

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black)
            .padding(innerPadding)
    ){
        Text(
            text = "Welcome\n ${currUser.getString("name", "Bomber")}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontFamily = inter_font,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            style = TextStyle(lineHeight = 35.sp)
        )

        Carousel(navController, viewState)
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Your List",
            color = Color.White,
            fontFamily = inter_font,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 40.dp)
                .padding(vertical = 15.dp)
        )

        if (vm.favoriteState.value.list.isNotEmpty()) {
            LazyHorizontalGrid(
                GridCells.Fixed(1),
                modifier = Modifier
                    .height(250.dp)
            ) {
                items(vm.favoriteState.value.list) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SeriesItem(it, showName = true, navController)
                    }
                }
            }
        } else {
            if (vm.favoriteState.value.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 15.dp),
                    color = ourRed,
                )
            } else {
                Text(
                    text = "Seems like you don't like any series, go check some to add!",
                    color = Color.White,
                    fontFamily = inter_font,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .padding(vertical = 20.dp)
                        .clickable {
                            navController.navigate(DestinationScreen.Search.route)
                        }
                )
            }
        }

        Text(
            text = "Suggested for you",
            color = Color.White,
            fontFamily = inter_font,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 40.dp)
                .padding(vertical = 15.dp)
        )
        // TODO: add recommender system suggestions

        Text(
            text = "Popular Series",
            color = Color.White,
            fontFamily = inter_font,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 40.dp)
                .padding(vertical = 15.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                viewState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 15.dp),
                        color = ourRed,
                    )
                }

                viewState.error != null -> {
                    //Text(text = "Error occurred!")
                    Text(text = "${viewState.error}", textAlign = TextAlign.Center)
                }

                else -> {
                    LazyHorizontalGrid(
                        GridCells.Fixed(1),
                        modifier = Modifier
                            .height(250.dp)
                    ) {
                        items(viewState.obj){
                            series -> SeriesItem(series, showName = true, navController)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(navController: NavController, vm: FbViewModel, currUser: SharedPreferences) {

    val photoUrl = currUser.getString("picture", "")
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val apiViewModel: MainViewModel = viewModel()
    val viewState by apiViewModel.seriesState
    vm.getFavorites(currUser.getLong("id", -1L))

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
    ) {
        innerPadding -> SeriesScreen(innerPadding, navController, apiViewModel, viewState, vm, currUser)
    }
}