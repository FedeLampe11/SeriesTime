package com.example.app3.auth

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.example.app3.ui.theme.ourYellow
import com.example.app3.ui.theme.switch_colors

@SuppressLint("WorldReadableFiles")
@Composable
fun ScrollProfilePage(innerPadding: PaddingValues, name: String, navController: NavController, currUser: SharedPreferences, vm: FbViewModel) {

    val context = LocalContext.current
    val userId = currUser.getLong("id", -1L)
    val photoUrl = currUser.getString("picture", "")

    val isUploading = remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imgUrl by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }
    val cLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        bitmap = it
    }
    var showDialog by remember { mutableStateOf(false) }
    var toUpdate by remember { mutableStateOf(false) }

    val preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val editor = preferences.edit()
    LaunchedEffect(Unit) {
        if (!preferences.contains("ReceiveNotifications"))
            editor.putBoolean("ReceiveNotifications", true)
        if (!preferences.contains("HideAlreadySeenEpisodes"))
            editor.putBoolean("HideAlreadySeenEpisodes", false)
        editor.apply()
    }

    var isNotificationChecked by remember { mutableStateOf(preferences.getBoolean("ReceiveNotifications", true)) }
    var isHideChecked by remember { mutableStateOf(preferences.getBoolean("HideAlreadySeenEpisodes", false)) }

    var selectedHour by remember { mutableIntStateOf(preferences.getInt("NotificationHour", 9)) }
    var selectedMinute by remember { mutableIntStateOf(preferences.getInt("NotificationMinute", 0)) }

    Column (
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        if (photoUrl != null && photoUrl != "") {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable { showDialog = true }
            )
            if (showDialog) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(300.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.DarkGray)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 53.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                            contentDescription = "Open Camera",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    cLauncher.launch()
                                    showDialog = false
                                    toUpdate = true
                                }
                        )
                        Text(
                            text = "Camera",
                            color = Color.White,
                            fontFamily = inter_font
                        )
                    }
                    Spacer(modifier = Modifier.padding(30.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_image_24),
                            contentDescription = "Open Gallery",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    launcher.launch("image/*")
                                    toUpdate = true
                                }
                        )
                        Text(
                            text = "Gallery",
                            color = Color.White,
                            fontFamily = inter_font
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 35.dp, bottom = 75.dp)
                    ) {
                        Text(
                            text = "x",
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .clickable { showDialog = false }
                        )
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(150.dp)
                            .clickable { showDialog = true }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.LightGray),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                            .size(150.dp)
                            .clickable { showDialog = true }
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp)
            ) {
                if (showDialog) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .width(300.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.DarkGray)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(start = 53.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                                contentDescription = "Open Camera",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        cLauncher.launch()
                                        showDialog = false
                                        toUpdate = true
                                    }
                            )
                            Text(
                                text = "Camera",
                                color = Color.White,
                                fontFamily = inter_font
                            )
                        }
                        Spacer(modifier = Modifier.padding(30.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.baseline_image_24),
                                contentDescription = "Open Gallery",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        launcher.launch("image/*")
                                        toUpdate = true
                                    }
                            )
                            Text(
                                text = "Gallery",
                                color = Color.White,
                                fontFamily = inter_font
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(start = 35.dp, bottom = 75.dp)
                        ) {
                            Text(
                                text = "x",
                                color = Color.White,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .clickable { showDialog = false }
                            )
                        }
                    }
                }
            }
        }

        if (toUpdate) {
            isUploading.value = true
            bitmap.let { bitmap ->
                if (bitmap != null) {
                    isUploading.value = false
                    uploadImageToFirebase(bitmap) { success, imageUrl ->
                        isUploading.value = false
                        if (success) {
                            imageUrl.let {
                                imgUrl = it
                                vm.updateProfilePicture(userId, imgUrl)
                                val edit = currUser.edit()
                                edit.putString("picture", imgUrl)
                                edit.apply()
                                toUpdate = false
                            }
                        }
                    }
                }
            }
        }

        if (name != "null") {
            Text(
                text = name,
                color = Color.White,
                fontFamily = inter_font,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 12.dp)
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Receive notifications",
                fontFamily = inter_font,
                color = Color.White,
                maxLines = 2,
            )
            Switch(
                checked = isNotificationChecked,
                onCheckedChange = { checked ->
                    isNotificationChecked = checked
                    editor.putBoolean("ReceiveNotifications", checked)
                    editor.apply()
                },
                colors = switch_colors
            )
        }

        if (isNotificationChecked) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Notification time",
                    fontFamily = inter_font,
                    color = Color.White,
                    maxLines = 2,
                )
                TextButton(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour: Int, minute: Int ->
                                selectedHour = hour
                                selectedMinute = minute
                                editor.putInt("NotificationHour", hour)
                                editor.putInt("NotificationMinute", minute)
                                editor.apply()
                            },
                            selectedHour,
                            selectedMinute,
                            true
                        ).show()
                    },
                    colors = ButtonColors(
                        containerColor = ourRed,
                        contentColor = Color.White,
                        disabledContainerColor = ourRed,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(
                        text = String.format("%02d:%02d", selectedHour, selectedMinute),
                        color = Color.White,
                        fontFamily = inter_font,
                        maxLines = 1
                    )
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Hide already seen episodes",
                fontFamily = inter_font,
                color = Color.White,
                maxLines = 2,
            )
            Switch(
                checked = isHideChecked,
                onCheckedChange = { checked ->
                    isHideChecked = checked
                    editor.putBoolean("HideAlreadySeenEpisodes", checked)
                    editor.apply()
                },
                colors = switch_colors
            )
        }

        Button(
            onClick = { navController.navigate(DestinationScreen.Statistics.route) },
            colors = ButtonColors(
                containerColor = ourRed,
                contentColor = Color.White,
                disabledContainerColor = ourRed,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_format_list_numbered_24),
                contentDescription = "Go to statistics page",
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Statistics",
                fontFamily = inter_font,
            )
        }

        Button(
            onClick = {
                val edit = currUser.edit()
                edit.putLong("id", -1L)
                edit.putString("name", "")
                edit.putString("picture", "")
                edit.apply()

                navController.navigate(DestinationScreen.Main.route)
            },
            colors = ButtonColors(
                containerColor = ourRed,
                contentColor = Color.White,
                disabledContainerColor = ourRed,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_logout_25),
                contentDescription = "Log Out",
                colorFilter = ColorFilter.tint(Color.White)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Logout",
                fontFamily = inter_font,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, currUser: SharedPreferences, vm: FbViewModel, listVM: MyListViewModel, isTablet: Boolean) {

    val name = currUser.getString("name", "")
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

                    IconButton(
                        onClick = { /* Do Nothing */ }
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = Icons.Filled.AccountCircle,
                            tint = ourRed,
                            contentDescription = "Already in profile page"
                        )
                    }
                }
            }
        ) { innerPadding ->
            ScrollProfilePage(innerPadding, name + "", navController, currUser, vm)
        }
    } else {
        Row (
            modifier = Modifier.fillMaxSize()
        ){
            LeftMenu(DestinationScreen.Profile, navController, photoUrl, listVM)
            ScrollProfilePage(PaddingValues(0.dp), name + "", navController, currUser, vm)
        }
    }
}
