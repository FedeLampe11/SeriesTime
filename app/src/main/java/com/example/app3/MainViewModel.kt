package com.example.app3

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _serieState = mutableStateOf(ReplyState())
    val seriesState: State<ReplyState> = _serieState

    init {
        fetchCategories()
    }

    private fun fetchCategories(){
        viewModelScope.launch{
            try{
                val response = seriesService.getCategories()
                _serieState.value = _serieState.value.copy(
                    obj = response,
                    loading = false,
                    error = null
                )
            }catch (e: Exception){
                _serieState.value = _serieState.value.copy(
                    loading = false,
                    error = "Error fetching Categories ${e.message}"
                )
            }
        }
    }
    data class ReplyState(
        val loading: Boolean = true,
        val obj: Reply = Reply("", -1, -1, emptyList()),
        val error: String? = null
    )

}