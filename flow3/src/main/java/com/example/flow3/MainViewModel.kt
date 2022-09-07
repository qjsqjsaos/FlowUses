package com.example.flow3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow3.resource.BlogFactory.createBlogList
import com.example.shared.model.uistate.UiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    //StateFlow
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    //SharedFlow
    //설정할 수 있다는 것만 보여주기 위함
    //아래 설정은 다 기본값
    private val _uiShared = MutableSharedFlow<UiState>(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiShared: SharedFlow<UiState> = _uiShared.asSharedFlow()


    //Channel
    //Channel.BUFFERED 버퍼 설정이 가능하다
    //Channel.BUFFERED는 시스템이 정한 버퍼값 64개를 가지고 있는 상태로 설정한다는 뜻이다.
    val uiChannel = Channel<UiState>(Channel.BUFFERED)

    init { getData() }

    fun getData() {
        viewModelScope.launch {
            createBlogList().collect {
                UiState.Success(it).let { value ->
                    _uiState.value = value
                    //SharedFlow는 value가 없음
                    _uiShared.emit(value)
                    uiChannel.send(value)
                }
            }
        }
    }
}