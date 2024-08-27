package com.example.app3.auth

import android.content.SharedPreferences
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.Details
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.example.app3.ui.theme.ourYellow

@Composable
fun SeriesRow(details: Details, navController: NavController) {
    val countdown = details.countdown?.air_date?.take(10)

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(DestinationScreen.Detail.createRoute(details.id))
            },
    ){
        Image(
            painter = rememberAsyncImagePainter(details.thumbnail),
            contentDescription = "Series Thumbnail",
            modifier = Modifier
                .width(140.dp)
                .aspectRatio(1f)
                .padding(6.dp)
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 15.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = details.name + "",
                color = Color.White,
                fontFamily = inter_font,
                fontSize = 20.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
            )

            val startDate = details.startDate?.take(4) + ""
            Text(
                text = startDate,
                color = Color.White,
                fontFamily = inter_font,
                fontSize = 14.sp,
                style = TextStyle(fontWeight = FontWeight.Normal),
            )

            if (countdown != null) {
                Text(
                    text = "New Ep. airs on $countdown",
                    color = Color.White,
                    fontFamily = inter_font,
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
            }

            Text(
                text = "${details.episodes?.last()?.season} seasons · ${details.status} · ${details.network}",
                color = Color.White,
                fontFamily = inter_font,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun AZSortButton(
        orderingType: List<String>,
        onOrderSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(
            onClick = { expanded = true }
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_sort_24),
                contentDescription = "Sort",
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ){
            orderingType.forEach { order ->
                DropdownMenuItem(
                    onClick = {
                        onOrderSelected(order)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = order,
                            fontSize = 16.sp,
                            fontFamily = inter_font,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    })
            }
        }
    }
}

@Composable
fun ScrollFavouritePage(innerPadding: PaddingValues, navController: NavController, vm: FbViewModel) {
    val favList = vm.favoriteState.value.list

    val orderingType : MutableList<String> = mutableListOf("Addition Date", "Start Date", "Title")
    var selectedText by remember { mutableStateOf(orderingType[0]) }

    val sortedList by remember (selectedText) {
        mutableStateOf(
            when (selectedText) {
                "Start Date" -> favList.sortedBy{ it.startDate }.reversed()
                "Title" -> favList.sortedBy{ it.name }
                else -> favList
            }
        )
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(innerPadding)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(2f))
            Text(
                text = "Favorite Series",
                color = ourRed,
                fontFamily = inter_font,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.weight(1f))

            AZSortButton(
                orderingType = orderingType,
                onOrderSelected = { selectedText = it }
            )
        }

        /*when (selectedText) {
            "Addition Date" -> favListCopy = favList
            "Start Date" -> favListCopy.sortedBy { it.startDate }
            "Title" -> favListCopy.sortedBy { it.name }
        }*/

        Spacer(modifier = Modifier.height(15.dp))
        if (favList.isNotEmpty()) {
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(sortedList) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SeriesRow(it, navController)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seems like you don't like any series, go check some to add!",
                    color = ourRed,
                    fontFamily = inter_font,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(vertical = 20.dp)
                )
                IconButton(
                    onClick = {
                        navController.navigate(DestinationScreen.Search.route)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Filled.Search,
                        tint = Color.White,
                        contentDescription = "Go to search page"
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen (navController: NavController, vm: FbViewModel, currUser: SharedPreferences, listVM: MyListViewModel) {

    val photoUrl = currUser.getString("picture", "")
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
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
                        tint = if (listVM.items.isNotEmpty()) ourYellow else Color.White,
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
                    onClick = { /* Do Nothing*/ }
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
        innerPadding -> ScrollFavouritePage(innerPadding, navController, vm)
    }
}