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

var userId : Long = 52

@HiltViewModel
class FbViewModel @Inject constructor(
    val auth: FirebaseAuth
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<Event<String>?>(null)

    private val _userState = mutableStateOf(UserReplyState())
    val userState: State<UserReplyState> = _userState

    private val _favoriteState = mutableStateOf(FavoriteReplyState())
    val favoriteState: State<FavoriteReplyState> = _favoriteState

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

        viewModelScope.launch{
            try{
                val response = userService.postNewUser(body)
                Log.d("DEBUG_server", "Tutto OK, id: ${response.id}")
                userId = response.id
                _userState.value = _userState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
                signedIn.value = true
            }catch (e: Exception){
                Log.d("DEBUG_server", "SONO CATCH ${e.message}")
                _userState.value = _userState.value.copy(
                    loading = false,
                    error = "Error adding new user ${e.message}"
                )
            }
            inProgress.value = false
        }



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
        /*
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
        */
        inProgress.value = true

        viewModelScope.launch{
            try{
                val response = userService.loginUser(email, pass)
                Log.d("DEBUG_server", "Tutto OK, id: ${response.id}")
                userId = response.id
                _userState.value = _userState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
                signedIn.value = true
            } catch (e: Exception) {
                Log.d("DEBUG_server", "SONO CATCH ${e.message}")
                _userState.value = _userState.value.copy(
                    loading = false,
                    error = "Error adding new user ${e.message}"
                )
            }
            inProgress.value = false
        }
    }


    fun getFavorites() {
        viewModelScope.launch{
            try{
                val response = userService.getFavourite(userState.value.obj.id)
                _favoriteState.value = _favoriteState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _favoriteState.value = _favoriteState.value.copy(
                    loading = false,
                    error = "Error fetching most popular series ${e.message}"
                )
            }
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

    data class FavoriteReplyState(
        val loading: Boolean = true,
        val obj: List<Long> = listOf(-1),
        val error: String? = null
    )
}