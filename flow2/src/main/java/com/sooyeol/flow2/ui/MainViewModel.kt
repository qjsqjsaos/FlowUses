package com.sooyeol.flow2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.model.UiBlog
import com.sooyeol.flow2.domain.usecase.UseCase
import com.sooyeol.flow2.ui.UiState.UiState
import com.sooyeol.flow2.ui.mapper.toUiBlogList
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val useCase: UseCase) : ViewModel() {

    private val _blogList  = MutableStateFlow<UiState>(UiState.Loading)
    val blogList : StateFlow<UiState> = _blogList.asStateFlow()

    init {
        viewModelScope.launch {
            useCase.getBlogList()
                .toUiBlogList()
                .asFlow()
                .onCompletion {
                    //flow가 완전히 수집되었을때 실행되는 로직 try catch finally에서 finally 같은 역할이다.

                }.catch { e ->
                    //error 캐치를 하는 부분이다.
                    _blogList.emit(UiState.Failure(e))
                }.collect{
                    _blogList.emit(UiState.Success(it))
                }
        }
    }
}