package com.example.shared.model.uistate

sealed class UiState{
    object Loading : UiState()
    data class Failure(val exception: Throwable?) : UiState()
    data class Success<T>(val data : T) : UiState()
}