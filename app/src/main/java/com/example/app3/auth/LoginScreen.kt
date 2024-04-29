package com.example.app3.auth

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.app3.ui.theme.inter_font
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(navController: NavController, vm: FbViewModel) {
    val emty by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorE by remember { mutableStateOf(false) }
    var errorP by remember { mutableStateOf(false) }

    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = authLauncher(
        onAuthComplete = {
            result -> user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    val token = stringResource(R.string.web_id)
    val context = LocalContext.current

    /*  Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            verticalArrangement = Arrangement.Center
        ) {
            if (vm.inProgress.value)
                CircularProgressIndicator()
        }*/
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Spacer(modifier = Modifier.height(300.dp))

        Text(
            text = "Login",
            color = Color.White,
            fontWeight = FontWeight(700),
            fontSize = 60.sp,
            fontFamily = inter_font,
            modifier = Modifier.padding(end = 130.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (errorE) {
            Text(
                text = "Enter email",
                color = Color.Red,
                fontFamily = inter_font,
                textAlign = TextAlign.Center,
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
        TextField(
            value = password,
            onValueChange = {
                password = it
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
            contentAlignment = Alignment.Center
        ){
            Button(onClick = {
                if (email.isNotEmpty()) {
                    errorE = false
                    if (password.isNotEmpty()) {
                        errorP = false
                        vm.login(email, password)
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
                    text = "Continue",
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
                text = AnnotatedString("Sign Up"),
                style = TextStyle(
                    color = Color(0xFFFF003D),
                    fontSize = 26.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = inter_font,
                ),
                onClick = {
                    navController.navigate(DestinationScreen.SignUp.route)
                }
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ){
            Button(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                    val gsc = GoogleSignIn.getClient(context, gso)
                    launcher.launch(gsc.signInIntent)
                    if (user != null) {
                        navController.navigate(DestinationScreen.Success.route)
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50.dp)),
                contentPadding = PaddingValues(5.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.DarkGray
                ),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                )
            }
            /*Button(
                onClick = {
                    LoginManager.getInstance().logInWithReadPermissions(context, listOf("email", "public_profile"))
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50.dp)),
                contentPadding = PaddingValues(5.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.DarkGray
                ),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                )
            } */
        }
    }
}

@Composable
fun authLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}