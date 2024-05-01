package com.example.app3.auth

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
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
import androidx.core.view.accessibility.AccessibilityViewCommand.MoveHtmlArguments
import androidx.navigation.NavController
import com.example.app3.DestinationScreen
import com.example.app3.FbViewModel
import com.example.app3.R
import com.example.app3.ui.theme.inter_font

@Composable
fun SignUpScreen(navController: NavController, vm: FbViewModel) {
    val emty by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cpassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var cpasswordVisibility by remember { mutableStateOf(false) }
    var errorE by remember { mutableStateOf(false) }
    var errorP by remember { mutableStateOf(false) }
    var errorCP by remember { mutableStateOf(false) }
    var errorC by remember { mutableStateOf(false) }
    var pLenght by remember { mutableStateOf(false) }
    
    /*
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (vm.inProgress.value)
            CircularProgressIndicator()
    }
    */
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Spacer(modifier = Modifier.height(250.dp))
        
        Text(
            text = "Sign Up",
            color = Color.White,
            fontWeight = FontWeight(700),
            fontSize = 60.sp,
            fontFamily = inter_font,
            modifier = Modifier.padding(end = 80.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

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
                        Modifier.clickable { email = emty }
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
                unfocusedIndicatorColor = Color(0xFF202027),
                focusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x30FFFFFF),
                unfocusedContainerColor = Color(0xFF202027),
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
        if (pLenght) {
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
                pLenght = it.length < 6
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
                unfocusedIndicatorColor = Color(0xFF202027),
                focusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x30FFFFFF),
                unfocusedContainerColor = Color(0xFF202027),
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
                unfocusedIndicatorColor = Color(0xFF202027),
                focusedIndicatorColor = Color.LightGray,
                cursorColor = Color.White,
                focusedContainerColor = Color(0x30FFFFFF),
                unfocusedContainerColor = Color(0xFF202027),
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
                .border(width = 2.dp, color = Color(0xFFFF003D)),
            contentAlignment = Alignment.Center,
        ){
            Button(onClick = {
                if (email.isNotEmpty()) {
                    errorE = false
                    if (password.isNotEmpty()) {
                        errorP = false
                        if (cpassword.isNotEmpty()) {
                            errorC = false
                            if (password == cpassword) {
                                errorCP = false
                                vm.onSignUp(email, password)
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
            },
                colors = ButtonDefaults.buttonColors(
                    Color.Transparent
                ),
                modifier = Modifier.width(200.dp)
            ) {
                Text(
                    text = "Sign Up",
                    color = Color(0xFFFF003D),
                    fontFamily = inter_font,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(500),
                )
            }
            if (vm.signedIn.value) {
                navController.navigate(DestinationScreen.Success.route)
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
                    color = Color(0xFFFF003D),
                    fontSize = 26.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = inter_font,
                ),
                onClick = {
                    navController.navigate(DestinationScreen.Login.route)
                }
            )
        }
    }
}