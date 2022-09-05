package com.sooyeol.flow2.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sooyeol.flow2.domain.usecase.UseCase
import com.sooyeol.flow2.ui.uistate.UiState
import com.sooyeol.flow2.ui.mapper.toUiBlogList
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val useCase: UseCase) : ViewModel() {

    private val _blogList  = MutableStateFlow<UiState>(UiState.Loading)
    val blogList : StateFlow<UiState> = _blogList.asStateFlow()

    init {
        viewModelScope.launch {
            flow{
                emit(useCase.getBlogList().toUiBlogList())
            }.onCompletion {
                Log.d("onCompletion", "Success")
            }.catch { e ->
                Log.d("catch Error", e.message.toString())
                _blogList.emit(UiState.Failure(e))
            }.collect {
                Log.d("collect Success", it.toString())
                _blogList.emit(UiState.Success(it))
            }
        }
    }
}