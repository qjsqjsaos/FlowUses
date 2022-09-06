package com.sooyeol.flow2.ui

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sooyeol.flow2.domain.usecase.UseCase
import com.example.shared.model.uistate.UiState
import com.sooyeol.flow2.ui.mapper.toUiBlogList
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 플로우의 수집은 정상적으로 완료되거나, 예외가 일어나서 완료되거나 해서
 * 끝나게 된다.
 * 오늘 사용해볼 onCompletion과 catch는 try catch finally와 매우 유사하다.
 * 그렇다면 try catch와 무엇이 다를까
 * onCompletion : onCompletion은 collect에서 flow의 수집을 모두 완료했을때 호출되며,
 * catch에서 일어난 예외에 대한 값을 받을 수 있어서, 어떤 예외가 발생했는지 종료시점에 알 수 있다.
 * catch : try catch에서 catch에 해당하는 부분이다. 에러사항을 Throwable 객체로 받을 수 있다.
 * flow에서 NetworkErrorException() 예외가 일어났을때
 * 실행되는 순서는 collect -> onCompletion -> catch 입니다.
 * */
class MainViewModel(private val useCase: UseCase) : ViewModel() {

    private val _blogList  = MutableStateFlow<UiState>(UiState.Loading)
    val blogList : StateFlow<UiState> = _blogList.asStateFlow()

    private var isLast = false

    init {
        viewModelScope.launch {
            flow{
                emit(useCase.getBlogList().toUiBlogList())
                throw NetworkErrorException()
            }.onCompletion { e ->
                Log.d("onCompletion", e.toString())
                if(isLast) {
                    Log.d("onCompletion", "isLast")
                }
            }.catch { e ->
                Log.d("catch Error", e.toString())
                _blogList.emit(UiState.Failure(e))
            }.collect {
                //서버에서 요청한 리스트가 빈값이라면,
                if(it.isNotEmpty()) {
                    Log.d("collect Success", it.toString())
                    _blogList.emit(UiState.Success(it))
                } else {
                    isLast = true
                }
            }
        }
    }
}