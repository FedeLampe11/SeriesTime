package com.example.app3.auth

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.Details
import com.example.app3.Episode
import com.example.app3.FbViewModel
import com.example.app3.MainViewModel
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed

// TODO: pensa se spostare il cuore in alto o meno

@Composable
fun EpisodeRow(episode: Episode) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 30.dp)
            .border(width = 2.dp, shape = RectangleShape, color = darkBlue),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Season: ${episode.season} Ep: ${episode.episode}",
            fontSize = 16.sp,
            fontFamily = inter_font,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(6.dp)
        )
        // TODO: metti segnale se episodio già visto
        if (false) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Episode already seen",
                tint = ourRed,
                modifier = Modifier.clickable {
                    // TODO: remove from seen list
                }
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Episode not already seen",
                tint = ourRed,
                modifier = Modifier.clickable {
                    // TODO: add in seen list
                }
            )
        }
    }
}

@Composable
fun ScrollDetailPage(obj: Details, vm: FbViewModel, currUser: SharedPreferences) {
    val userId = currUser.getLong("id", -1L)

    val favorite = remember { mutableStateOf(vm.favoriteState.value.list.contains(obj)) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        item { // Static part
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(obj.image_thumbnail_path),
                    contentDescription = "Series Thumbnail",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 30.dp, horizontal = 10.dp)
                        .clipToBounds(),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = obj.name + "",
                    fontSize = 30.sp,
                    fontFamily = inter_font,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .padding(top = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rating: " + obj.rating + "",
                    fontSize = 18.sp,
                    fontFamily = inter_font,
                    fontWeight = FontWeight.Bold,
                    color = ourRed,
                )

                if (favorite.value) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite button",
                        tint = ourRed,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                vm.removeFavoriteSeries(userId, obj.id.toLong())
                                favorite.value = !favorite.value
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
                                vm.addFavoriteSeries(userId, obj.id.toLong())
                                favorite.value = !favorite.value
                            }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = obj.description + "",
                    fontSize = 20.sp,
                    fontFamily = inter_font,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        }
        if (!obj.episodes.isNullOrEmpty()) {
            item{
                Text(
                    text = "Episodes",
                    fontSize = 18.sp,
                    fontFamily = inter_font,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 20.dp)
                )
            }
            items(obj.episodes) {
                episode -> EpisodeRow(episode)
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, vm: FbViewModel, seriesId: String, currUser: SharedPreferences) {
    val photoUrl = currUser.getString("picture", "")
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val apiViewModel: MainViewModel = viewModel()
    apiViewModel.fetchDetailPage(seriesId)
    val viewState by apiViewModel.detailState

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
        innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    Text(text = "${viewState.error}" + "",
                        color = Color.White)
                }

                else -> {
                    ScrollDetailPage(viewState.obj, vm, currUser)
                }
            }
        }
    }
}