package com.example.app3

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FbViewModel @Inject constructor(
    val auth: FirebaseAuth
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<Event<String>?>(null)

    private val _userState = mutableStateOf(UserReplyState())
    val userState: State<UserReplyState> = _userState

    private fun addNewUser(body: Map<String, String?>){
        viewModelScope.launch{
            try{
                val response = userService.postNewUser(body)
                Log.d("DEBUG_server", "SONO QUI CAXXO")
                _userState.value = _userState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
            }catch (e: Exception){
                Log.d("DEBUG_server", "SONO CATCH ${e.message}")
                _userState.value = _userState.value.copy(
                    loading = false,
                    error = "Error adding new user ${e.message}"
                )
            }
        }
    }

    fun onSignUp(name: String, email: String, pass: String) {
        val body = mapOf(
            "id" to null,
            "full_name" to name,
            "email" to email,
            "password" to pass,
            "meta_api_key" to null,
            "google_api_key" to null,
            "profile_picture" to null
        )
        Log.d("DEBUG_server", "1: $body")
        inProgress.value = true

        addNewUser(body)
        Log.d("DEBUG_server", "2: $body")

        /*auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signedIn.value = true
                    handleException(it.exception, "signup successful")
                } else {
                    handleException(it.exception, "signup failed")
                }
                inProgress.value = false
            }*/
    }

    fun login(email: String, pass: String) {
        inProgress.value = true

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signedIn.value = true
                    handleException(it.exception, "login successful")
                } else {
                    handleException(it.exception, "login failed")
                }
                inProgress.value = false
            }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        popupNotification.value = Event(message)
    }

    data class UserReplyState(
        val loading: Boolean = true,
        val obj: UserAuthReply = UserAuthReply(-1, "", "", "", "", "", ""),
        val error: String? = null
    )
}