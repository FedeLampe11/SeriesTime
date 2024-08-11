package com.example.app3.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
import com.example.app3.EpisodeReply
import com.example.app3.FbViewModel
import com.example.app3.MainViewModel
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeRow(episode: Episode, seriesId: String, userId: Long, vm: FbViewModel) {
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val hideEpisodes by remember { mutableStateOf(preferences.getBoolean("HideAlreadySeenEpisodes", false)) }

    val seenEpisodes = vm.watchedState.value.list
    val ep = EpisodeReply(episode.season, episode.episode)

    val focusRequester = remember { FocusRequester() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .border(width = 2.dp, shape = RectangleShape, color = darkBlue),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!hideEpisodes) {
            Box(
                modifier = Modifier.weight(0.8f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Season: ${episode.season} Ep: ${episode.episode}",
                        fontSize = 16.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 25.dp, top = 6.dp)
                    )
                    Text(
                        text = episode.name,
                        fontSize = 16.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(6.dp)
                            .padding(start = 19.dp)
                            .basicMarquee(animationMode = MarqueeAnimationMode.WhileFocused)
                            .focusRequester(focusRequester)
                            .focusable()
                            .clickable { focusRequester.requestFocus() },
                    )
                }
            }
            // TODO: capire come aggiornare la pagina dopo che si ha aggiornato la lista di episodi visti (mettere flag se schiacciato + e viceversa)
            if (seenEpisodes.contains(ep)) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Episode already seen",
                    tint = ourRed,
                    modifier = Modifier
                        .weight(0.2f)
                        .clickable {
                            Log.d(
                                "TEST",
                                "userId = $userId, seriesId = ${seriesId.toLong()}, ${episode.season.toLong()}, ${episode.episode.toLong()}"
                            )
                            vm.removeWatchedEpisode(
                                userId,
                                seriesId.toLong(),
                                season = episode.season.toLong(),
                                episode = episode.episode.toLong()
                            )
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Episode not already seen",
                    tint = ourRed,
                    modifier = Modifier
                        .padding(end = 23.dp)
                        .clickable {
                            vm.addWatchedEpisode(
                                userId,
                                seriesId.toLong(),
                                season = episode.season.toLong(),
                                episode = episode.episode.toLong()
                            )
                        }
                )
            }
        } else {
            if (seenEpisodes.contains(ep)) {
                Box(
                    modifier = Modifier.weight(0.8f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Season: ${episode.season} Ep: ${episode.episode}",
                            fontSize = 16.sp,
                            fontFamily = inter_font,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 25.dp, top = 6.dp)
                        )
                        Text(
                            text = episode.name,
                            fontSize = 16.sp,
                            fontFamily = inter_font,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .padding(6.dp)
                                .padding(start = 19.dp)
                                .basicMarquee(animationMode = MarqueeAnimationMode.WhileFocused)
                                .focusRequester(focusRequester)
                                .focusable()
                                .clickable { focusRequester.requestFocus() },
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Episode not already seen",
                    tint = ourRed,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable {
                            vm.addWatchedEpisode(
                                userId,
                                seriesId.toLong(),
                                season = episode.season.toLong(),
                                episode = episode.episode.toLong()
                            )
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonDropDownMenu(details: Details, userId: Long, vm: FbViewModel){

    val seasonList: MutableList<String> = mutableListOf()
    if (!details.episodes.isNullOrEmpty()) {
        for (ep in details.episodes) {
            if (!seasonList.contains("Season "+ ep.season.toString())) {
                seasonList.add("Season "+ ep.season.toString())
            }
        }
        seasonList.add("All seasons")
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(seasonList[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.Center
    ){
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 30.dp)
        ){
            TextField(
                value = selectedText,
                onValueChange = { /* Do Nothing */ },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedContainerColor = Color(0x30FFFFFF),
                    unfocusedContainerColor = darkBlue,
                    focusedLeadingIconColor = Color.LightGray,
                    unfocusedLeadingIconColor = Color.LightGray,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray,
                    focusedTrailingIconColor = Color.LightGray,
                    unfocusedTrailingIconColor = Color.LightGray,
                ),
                textStyle = TextStyle(
                    fontFamily = inter_font,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                seasonList.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                fontSize = 16.sp,
                                fontFamily = inter_font,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        },
                        onClick = {
                            selectedText = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        val selectedSeason = if (selectedText == "All seasons") selectedText else selectedText.drop(7)
        if (!details.episodes.isNullOrEmpty()) {
            details.episodes.forEach { episode ->
                if (episode.season.toString() == selectedSeason || selectedSeason == "All seasons")
                    EpisodeRow(episode, details.id, userId, vm)
            }
        }
    }
}

@Composable
fun ScrollDetailPage(obj: Details, vm: FbViewModel, currUser: SharedPreferences) {
    val userId = currUser.getLong("id", -1L)

    val favorite = remember { mutableStateOf(vm.favoriteState.value.list.contains(obj)) }

    vm.getWatched(userId, obj.id.toLong())

    if (vm.watchedState.value.loading){
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 15.dp),
                color = ourRed,
            )
        }
    } else {
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
                        painter = rememberAsyncImagePainter(obj.thumbnail),
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
                        maxLines = 2,
                        lineHeight = 32.sp
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
                        .padding(vertical = 30.dp, horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = obj.description?.replace("<br>", "\n") + "",
                        fontSize = 18.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }
            }
            if (!obj.episodes.isNullOrEmpty()) {
                item {
                    Text(
                        text = "Episodes",
                        fontSize = 20.sp,
                        fontFamily = inter_font,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .padding(bottom = 20.dp)
                    )
                }
                item {
                    SeasonDropDownMenu(obj, userId, vm)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 15.dp),
                            color = ourRed,
                        )
                    }
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