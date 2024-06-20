package com.example.app3.auth

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.app3.FbViewModel
import com.example.app3.MainViewModel
import com.example.app3.Movie
import com.example.app3.R
import com.example.app3.Series
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay


val top5List = listOf(series1, series2, series1, series2, series1)

@Composable
fun IndicatorDots(size: Int, currentIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 150.dp),
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

@Composable
fun Carousel(images: List<Movie>, navController: NavController) {
    var currentIndex by remember { mutableStateOf(0) }
    // val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000L) // Delay of 10 seconds
            currentIndex = (currentIndex + 1) % images.size
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SeriesItem(images[currentIndex], showName = false, navController)
        IndicatorDots(size = images.size, currentIndex = currentIndex)
    }
}

@Composable
fun SeriesItem(series: Movie, showName: Boolean, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(110.dp)
            .clickable {
                navController.navigate(DestinationScreen.Detail.createRoute(series.id.toLong()))
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            //painter = rememberAsyncImagePainter(series.image_thumbnail_path),
            painter = painterResource(id = R.drawable.facebook),
            contentDescription = "Series Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(bottom = 4.dp)
        )
        if (showName) {
            Text(
                text = series.name + "",
                color = Color.White,
                fontFamily = inter_font,
                fontSize = 20.sp,
                style = TextStyle(fontWeight = FontWeight.Normal),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun SeriesItem2(series: Series, showName: Boolean, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(110.dp)
            .clickable {
                navController.navigate(DestinationScreen.Detail.createRoute(series.id.toLong()))
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberAsyncImagePainter(series.image_thumbnail_path),
            //painter = painterResource(id = R.drawable.facebook),
            contentDescription = "Series Thumbnail",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(bottom = 4.dp)
        )
        if (showName) {
            Text(
                text = series.name + "",
                color = Color.White,
                fontFamily = inter_font,
                fontSize = 20.sp,
                style = TextStyle(fontWeight = FontWeight.Normal),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun SeriesScreen(seriesList: List<Movie>, innerPadding: PaddingValues, navController: NavController, viewState: MainViewModel.ReplyState){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black)
            .padding(innerPadding)
    ){
        //TODO: add searchBox
        //SearchBox()
        Spacer(modifier = Modifier.height(15.dp))

        Carousel(top5List, navController)
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

        LazyHorizontalGrid(
            GridCells.Fixed(1),
            modifier = Modifier
                .height(150.dp)
        ) {
            items(favouriteList){ series ->
                SeriesItem(series, showName = true, navController)
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

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
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                viewState.error != null -> {
                    //Text(text = "Error occurred!")
                    Text(text = "${viewState.error}")
                }

                else -> {
                    LazyHorizontalGrid(
                        GridCells.Fixed(1),
                        modifier = Modifier
                            .height(150.dp)
                    ) {
                        items(viewState.obj.tv_shows){ series ->
                            SeriesItem2(series, showName = true, navController)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        /*LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            items(seriesList){
                    series -> SeriesItem(series, showName = false, navController)
            }
        }*/
    }
}

@Composable
fun ScrollMainPage(navController: NavController, innerPadding: PaddingValues, viewState: MainViewModel.ReplyState) {
    SeriesScreen(favouriteList, innerPadding, navController, viewState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(navController: NavController, vm: FbViewModel) {

    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val photoUrl = user?.photoUrl
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val apiViewModel: MainViewModel = viewModel()
    val viewstate by apiViewModel.seriesState

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
                     modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                 )
                 IconButton(
                     onClick = {
                         navController.navigate(DestinationScreen.Profile.route)
                         //navController.navigate(DestinationScreen.Notifications.route)
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
        innerPadding -> ScrollMainPage(navController, innerPadding, viewstate)
    }
}