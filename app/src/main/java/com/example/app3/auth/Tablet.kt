package com.example.app3.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.menuColor
import com.example.app3.ui.theme.ourRed
import com.example.app3.ui.theme.ourYellow

@Composable
fun LeftMenu(currentPage: DestinationScreen, navController: NavController, photoUrl: String?, listVM: MyListViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(270.dp)
            .background(menuColor)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        // HomePage
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(270.dp)
                .height(70.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    if (currentPage.route != DestinationScreen.Home.route)
                        navController.navigate(DestinationScreen.Home.route)
                }
                .background(
                    if (currentPage.route != DestinationScreen.Home.route)
                        menuColor
                    else
                        darkBlue
                )

        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "HomePage",
                tint = ourRed,
                modifier = Modifier.padding(horizontal = 10.dp).padding(start = 10.dp)
            )
            Text(
                text = "Homepage",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
            )
        }
        // FavoritePage
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(270.dp)
                .height(70.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    if (currentPage.route != DestinationScreen.Favourite.route)
                        navController.navigate(DestinationScreen.Favourite.route)
                }
                .background(
                    if (currentPage.route != DestinationScreen.Favourite.route)
                        menuColor
                    else
                        darkBlue
                )

        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite Page",
                tint = ourRed,
                modifier = Modifier.padding(horizontal = 10.dp).padding(start = 10.dp)
            )
            Text(
                text = "Favorites",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
            )
        }
        // Search
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(270.dp)
                .height(70.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    if (currentPage.route != DestinationScreen.Search.route)
                        navController.navigate(DestinationScreen.Search.route)
                }
                .background(
                    if (currentPage.route != DestinationScreen.Search.route)
                        menuColor
                    else
                        darkBlue
                )

        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Page",
                tint = ourRed,
                modifier = Modifier.padding(horizontal = 10.dp).padding(start = 10.dp)
            )
            Text(
                text = "Search",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
            )
        }
        // Profile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(270.dp)
                .height(70.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    if (currentPage.route != DestinationScreen.Profile.route)
                        navController.navigate(DestinationScreen.Profile.route)
                }
                .background(
                    if (currentPage.route != DestinationScreen.Profile.route)
                        menuColor
                    else
                        darkBlue
                )
        ) {
            if (photoUrl != null && photoUrl != "")
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 10.dp).padding(start = 10.dp)
                        .size(25.dp)
                        .clip(CircleShape)
                )
            else {
                Icon(
                    modifier = Modifier.padding(horizontal = 10.dp).padding(start = 10.dp),
                    imageVector = Icons.Filled.AccountCircle,
                    tint = ourRed,
                    contentDescription = "Localized description"
                )
            }

            Text(
                text = "Profile",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
            )
        }
        // Notifications
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(270.dp)
                .height(70.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    if (currentPage.route != DestinationScreen.Notification.route)
                        navController.navigate(DestinationScreen.Notification.route)
                }
                .background(
                    if (currentPage.route != DestinationScreen.Notification.route)
                        menuColor
                    else
                        darkBlue
                )

        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                tint = if (listVM.items.isNotEmpty()) ourYellow else ourRed,
                contentDescription = "Notification Page",
                modifier = Modifier.padding(horizontal = 10.dp).padding(start = 10.dp)
            )
            Text(
                text = "Notifications",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
            )
        }
        // Statistics
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(270.dp)
                .height(70.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    if (currentPage.route != DestinationScreen.Statistics.route)
                        navController.navigate(DestinationScreen.Statistics.route)
                }
                .background(
                    if (currentPage.route != DestinationScreen.Statistics.route)
                        menuColor
                    else
                        darkBlue
                )

        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_format_list_numbered_24),
                contentDescription = "Stats Page",
                tint = ourRed,
                modifier = Modifier.padding(horizontal = 10.dp).padding(start = 10.dp)
            )
            Text(
                text = "Statistics",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = inter_font,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}