package com.example.flow1.ui

import androidx.lifecycle.ViewModel
import com.example.flow1.ui.widget.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _name = MutableStateFlow(UiState().copy(type = UiState.EditType.NAME))
    val name: StateFlow<UiState> = _name

    private val _address = MutableStateFlow(UiState().copy(type = UiState.EditType.ADDRESS))
    val address: StateFlow<UiState> = _address

    private val _phoneNum = MutableStateFlow(UiState().copy(type = UiState.EditType.PHONENUM))
    val phoneNum: StateFlow<UiState> = _phoneNum
}