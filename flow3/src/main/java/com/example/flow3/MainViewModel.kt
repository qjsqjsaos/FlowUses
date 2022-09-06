package com.example.flow3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow3.resource.BlogFactory.createBlogList
import com.example.shared.model.uistate.UiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    //설정할 수 있다는 것만 보여주기 위함
    //아래 설정은 다 기본값
    private val _uiShared = MutableSharedFlow<UiState>(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )


    val uiShared: SharedFlow<UiState> = _uiShared.asSharedFlow()

    init {
        viewModelScope.launch {
            createBlogList().collect {
                _uiState.value = UiState.Success(it)
                //SharedFlow는 value가 없음
                _uiShared.emit(UiState.Success(it))
            }
        }
    }

    //같은 데이터 넣기
    fun setSameData() {
        viewModelScope.launch {
            createBlogList().collect {
                _uiState.value = UiState.Success(it)
                //SharedFlow는 value가 없음
                _uiShared.emit(UiState.Success(it))
            }
        }
    }
}