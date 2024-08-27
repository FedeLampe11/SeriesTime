package com.example.app3

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class RecommenderViewModel : ViewModel() {

    private val _recommenderState = mutableStateOf(RecommendedReplyState())
    val recommenderState: State<RecommendedReplyState> = _recommenderState
    fun getRecommended(userId: Long) {
        viewModelScope.launch{
            try{
                val response = recommenderService.getRecommended(userId)

                _recommenderState.value = _recommenderState.value.copy(
                    list = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _recommenderState.value = _recommenderState.value.copy(
                    loading = false,
                    error = "Error fetching recommended series ${e.message}"
                )
            }
        }
    }

    data class RecommendedReplyState(
        val loading: Boolean = true,
        val list: List<Details> = emptyList(),
        val error: String? = null
    )
}