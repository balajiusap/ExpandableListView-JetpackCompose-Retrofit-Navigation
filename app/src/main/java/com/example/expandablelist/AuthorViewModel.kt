package com.example.expandablelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expandablelist.data.Author
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthorViewModel : ViewModel() {

    private val _authorList: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)
    val authorList = _authorList

    fun getAuthorList() {
        viewModelScope.launch {
            try {
                _authorList.value = ApiState.Success(ApiService.apiService.getAuthorList())
            } catch (e: Exception) {
                e.printStackTrace()
                _authorList.value = ApiState.Error("Api Error")
            }

        }
    }

    sealed class ApiState {
        data object Loading : ApiState()
        data class Success(val data: List<Author>) : ApiState()
        data class Error(val error: String) : ApiState()
    }
}