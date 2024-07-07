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
        inProgress.value = true

        viewModelScope.launch{
            try{
                val response = userService.postNewUser(body)
                _userState.value = _userState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
                signedIn.value = true
            }catch (e: Exception){
                _userState.value = _userState.value.copy(
                    loading = false,
                    error = "Error adding new user ${e.message}"
                )
            }
            inProgress.value = false
        }
    }

    fun login(email: String, pass: String) {
        inProgress.value = true

        viewModelScope.launch{
            try{
                val response = userService.loginUser(email, pass)
                _userState.value = _userState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
                signedIn.value = true
            } catch (e: Exception) {
                _userState.value = _userState.value.copy(
                    loading = false,
                    error = "Error adding new user ${e.message}"
                )
            }
            inProgress.value = false
        }
    }

    fun addUserFromExternalService(body: Map<String, String?>) {
        inProgress.value = true

        viewModelScope.launch{
            try{
                val response = userService.postNewUser(body)
                _userState.value = _userState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
                signedIn.value = true
            }catch (e: Exception){
                _userState.value = _userState.value.copy(
                    loading = false,
                    error = "Error adding new user ${e.message}"
                )
            }
            inProgress.value = false
        }
    }

    fun getFavorites(userId: Long) {
        viewModelScope.launch{
            try{
                val response = userService.getFavourite(userId)
                val detailList: MutableList<Details> = mutableListOf()
                for (id in response){
                    detailList.add(seriesService.getDetailPage(id).tvShow)
                }
                _favoriteState.value = _favoriteState.value.copy(
                    list = detailList,
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
        val list: List<Details> = emptyList(),
        val error: String? = null
    )
}