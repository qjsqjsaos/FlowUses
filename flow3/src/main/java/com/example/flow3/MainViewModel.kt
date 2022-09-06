package com.example.flow3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow3.resource.BlogFactory.createBlogList
import com.example.shared.model.uistate.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState  = MutableStateFlow<UiState>(UiState.Loading)
    val uiState : StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            createBlogList().collect {
                _uiState.value = UiState.Success(it)
            }
        }
    }
}