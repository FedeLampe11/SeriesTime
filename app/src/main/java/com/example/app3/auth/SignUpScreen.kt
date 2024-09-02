package com.example.app3.auth

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.darkBlue
import com.example.app3.ui.theme.inter_font
import com.example.app3.ui.theme.ourRed
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

fun uploadImageToFirebase(
    bitmap: Bitmap,
    callback: (Boolean, String) -> Unit
) {
    val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("images/${bitmap}")

    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageData = baos.toByteArray()

    imageRef.putBytes(imageData).addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener {uri ->
            val imageUrl = uri.toString()
            callback(true, imageUrl)
        }.addOnFailureListener{
            callback(false, null.toString())
        }
    }.addOnFailureListener{
        callback(false, null.toString())
    }
}

@Composable
fun ImageInsertionButton(bitmap: Bitmap?, launcher: ManagedActivityResultLauncher<String, Uri?>, cLauncher: ManagedActivityResultLauncher<Void?, Bitmap?>) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp)
                    .clickable { showDialog = true }
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_person_24),
                contentDescription = "Image",
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
                            .clickable { launcher.launch("image/*") }
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

@Composable
fun SignUpScreen(navController: NavController, vm: FbViewModel, currUser: SharedPreferences) {
    val empty by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cpassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var cpasswordVisibility by remember { mutableStateOf(false) }
    var errorN by remember { mutableStateOf(false) }
    var errorE by remember { mutableStateOf(false) }
    var errorP by remember { mutableStateOf(false) }
    var errorCP by remember { mutableStateOf(false) }
    var errorC by remember { mutableStateOf(false) }
    var pLength by remember { mutableStateOf(false) }

    val context = LocalContext.current
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

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Text(
            text = "Sign Up",
            color = Color.White,
            fontWeight = FontWeight(700),
            fontSize = 60.sp,
            fontFamily = inter_font,
            modifier = Modifier.padding(end = 80.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        ImageInsertionButton(bitmap, launcher, cLauncher)

        Spacer(modifier = Modifier.height(10.dp))

        if (errorN) {
            Text(
                text = "Enter Name",
                color = Color.Red,
                fontFamily = inter_font,
                textAlign = TextAlign.Center
            )
        }

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            placeholder = {
                Text(
                    text = "Name",
                    fontFamily = inter_font,
                    color = Color.LightGray,
                    fontWeight = FontWeight(500),
                    fontSize = 26.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (name.isNotEmpty())
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = null,
                        Modifier.clickable { name = empty }
                    )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.LightGray,
                fontFamily = inter_font,
                fontWeight = FontWeight(500),
                fontSize = 26.sp
            ),
            modifier = Modifier
                .width(304.dp)
                .height(62.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = darkBlue,
                focusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x30FFFFFF),
                unfocusedContainerColor = darkBlue,
                focusedLeadingIconColor = Color.LightGray,
                unfocusedLeadingIconColor = Color.LightGray,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                focusedTrailingIconColor = Color.LightGray,
                unfocusedTrailingIconColor = Color.LightGray,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (errorE) {
            Text(
                text = "Enter email",
                color = Color.Red,
                fontFamily = inter_font,
                textAlign = TextAlign.Center
            )
        }
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            placeholder = {
                Text(
                    text = "E-mail",
                    fontFamily = inter_font,
                    color = Color.LightGray,
                    fontWeight = FontWeight(500),
                    fontSize = 26.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (email.isNotEmpty())
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = null,
                        Modifier.clickable { email = empty }
                    )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.LightGray,
                fontFamily = inter_font,
                fontWeight = FontWeight(500),
                fontSize = 26.sp
            ),
            modifier = Modifier
                .width(304.dp)
                .height(62.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = darkBlue,
                focusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x30FFFFFF),
                unfocusedContainerColor = darkBlue,
                focusedLeadingIconColor = Color.LightGray,
                unfocusedLeadingIconColor = Color.LightGray,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                focusedTrailingIconColor = Color.LightGray,
                unfocusedTrailingIconColor = Color.LightGray,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (errorP) {
            Text(
                text = "Enter Password",
                fontFamily = inter_font,
                color = Color.Red,
                textAlign = TextAlign.Center,
            )
        }
        if (pLength) {
            Text(
                text = "Password must be 6 chars",
                color = Color.Red,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        TextField(
            value = password,
            onValueChange = {
                password = it
                pLength = it.length < 6
            },
            placeholder = {
                Text(
                    text = "Password",
                    fontFamily = inter_font,
                    color = Color.LightGray,
                    fontWeight = FontWeight(500),
                    fontSize = 26.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (password.isNotEmpty()) {
                    val visibilityIcon = if (passwordVisibility) {
                        painterResource(id = R.drawable.baseline_visibility_24)
                    } else {
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    }
                    Icon(
                        painter = visibilityIcon,
                        contentDescription = if (passwordVisibility) "Hide Password" else "Show Password",
                        Modifier.clickable {
                            passwordVisibility = !passwordVisibility
                        }
                    )
                }
            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
               PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.LightGray,
                fontFamily = inter_font,
                fontWeight = FontWeight(500),
                fontSize = 26.sp
            ),
            modifier = Modifier
                .width(304.dp)
                .height(62.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = darkBlue,
                focusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x30FFFFFF),
                unfocusedContainerColor = darkBlue,
                focusedLeadingIconColor = Color.LightGray,
                unfocusedLeadingIconColor = Color.LightGray,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                focusedTrailingIconColor = Color.LightGray,
                unfocusedTrailingIconColor = Color.LightGray,
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (errorCP) {
            Text(
                text = "Password doesn't match!",
                color = Color.Red,
                fontFamily = inter_font,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        if (errorC) {
            Text(
                text = "Confirm Password please!",
                color = Color.Red,
                fontFamily = inter_font,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        TextField(
            value = cpassword,
            onValueChange = {
                cpassword = it
            },
            placeholder = {
                Text(
                    text = "Confirm",
                    fontFamily = inter_font,
                    color = Color.LightGray,
                    fontWeight = FontWeight(500),
                    fontSize = 26.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (cpassword.isNotEmpty()) {
                    val visibilityIcon = if (cpasswordVisibility) {
                        painterResource(id = R.drawable.baseline_visibility_24)
                    } else {
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    }
                    Icon(
                        painter = visibilityIcon,
                        contentDescription = if (cpasswordVisibility) "Hide Password" else "Show Password",
                        Modifier.clickable {
                            cpasswordVisibility = !cpasswordVisibility
                        }
                    )
                }
            },
            visualTransformation = if (cpasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.LightGray,
                fontFamily = inter_font,
                fontWeight = FontWeight(500),
                fontSize = 26.sp
            ),
            modifier = Modifier
                .width(304.dp)
                .height(62.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = darkBlue,
                focusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x30FFFFFF),
                unfocusedContainerColor = darkBlue,
                focusedLeadingIconColor = Color.LightGray,
                unfocusedLeadingIconColor = Color.LightGray,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray,
                focusedTrailingIconColor = Color.LightGray,
                unfocusedTrailingIconColor = Color.LightGray,
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box (
            modifier = Modifier
                .width(304.dp)
                .height(57.dp)
                .border(width = 2.dp, color = ourRed),
            contentAlignment = Alignment.Center,
        ){
            Button(onClick = {
                if (name.isNotEmpty()) {
                    errorN = false
                    if (email.isNotEmpty()) {
                        errorE = false
                        if (password.isNotEmpty()) {
                            errorP = false
                            if (cpassword.isNotEmpty()) {
                                errorC = false
                                if (password == cpassword) {
                                    errorCP = false
                                    isUploading.value = true
                                    bitmap.let{bitmap->
                                        if(bitmap!=null){
                                            isUploading.value = false
                                            uploadImageToFirebase(bitmap){ success, imageUrl ->
                                                isUploading.value = false
                                                if(success){
                                                    imageUrl.let{
                                                        imgUrl = it
                                                        vm.onSignUp(name, email, password, imgUrl)
                                                    }
                                                }
                                            }
                                        } else {
                                            vm.onSignUp(name, email, password, null)
                                        }
                                    }
                                } else {
                                    errorCP = true
                                }
                            } else {
                                errorC = true
                            }
                        } else {
                            errorP = true
                        }
                    } else {
                        errorE = true
                    }
                } else {
                    errorN = true
                }
            },
                colors = ButtonDefaults.buttonColors(
                    Color.Transparent
                ),
                modifier = Modifier.width(200.dp)
            ) {
                Text(
                    text = "Sign Up",
                    color = ourRed,
                    fontFamily = inter_font,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(500),
                )
            }
            if (vm.signedIn.value) {
                val edit = currUser.edit()
                edit.putLong("id", vm.userState.value.obj.id)
                edit.putString("name", vm.userState.value.obj.full_name)
                edit.putString("picture", vm.userState.value.obj.profile_picture)
                edit.apply()
                navController.navigate(DestinationScreen.Home.route)
            }
            vm.signedIn.value = false
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row (
            modifier = Modifier.padding(end = 170.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "or",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight(500),
                fontFamily = inter_font,
            )

            Spacer(modifier = Modifier.width(12.dp))

            ClickableText(
                text = AnnotatedString("Login"),
                style = TextStyle(
                    color = ourRed,
                    fontSize = 26.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = inter_font,
                ),
                onClick = {
                    navController.navigate(DestinationScreen.Login.route)
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}