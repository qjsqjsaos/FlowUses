package com.example.flow1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flow1.ui.extension.Extensions.textChangesToFlow
import com.example.flow1.ui.widget.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 알면 좋은 정보
 *
 * emit : 값을 설정하기 위한 호출을 래핑하여 방출하는 suspend 함수이다.
 * 값의 변환이 일어날 때 가장 최근에 보낸 값이 재생 캐시에 저장되어
 * 이전 값과 같을 경우 이전 요소로 그대로 교체해 줍니다.
 * (이전 값이 같으면 값을 방출하지 않는다는 뜻)
 * 쓰레드 세이프 되며 외부 동기화 없이 코루틴 안에서 호출됩니다.
 *
 * value : ex) _name.value
 * 위에 emit과 기능상 별차이가 없다.
 * 차이점은 코루틴 밖에서 호출을 할 수 있다는 점입니다.
 *
 * update : 만약 같은 객체에서 변경사항이 있을 경우 같은 객체에서 변경된 사항만을
 * 업데이트 해주는 함수입니다. 새로운 객체가 아닌 기존 객체에서만 데이터를 변경할 때
 * 사용하기 용이합니다.
 * */
class MainViewModel : ViewModel() {
    private val _name = MutableStateFlow(UiState().copy(type = UiState.EditType.NAME))
    val name: StateFlow<UiState> = _name

    private val _address = MutableStateFlow(UiState().copy(type = UiState.EditType.ADDRESS))
    val address: StateFlow<UiState> = _address

    private val _phoneNum = MutableStateFlow(UiState().copy(type = UiState.EditType.PHONENUM))
    val phoneNum: StateFlow<UiState> = _phoneNum

    // 각 타입의 맞는 데이터를 넣어준다.
    fun emitData(state: UiState?) {
        val editState = state?.editState
        when (state?.type) {
            UiState.EditType.NAME -> updateState(_name, editState)
            UiState.EditType.ADDRESS -> updateState(_address, editState)
            else -> updateState(_phoneNum, editState) // UiState.EditType.PHONENUM
        }
    }

    // 글을 썼는지 안썼는지 비교한 enum 값을 넣어준다.
    private fun updateState(flow: MutableStateFlow<UiState>, state: UiState.EditState?) {
        flow.update {
            if(state == UiState.EditState.WRITTEN) {
                it.copy(editState = UiState.EditState.WRITTEN)
            } else {
                it.copy(editState = UiState.EditState.EMPTY)
            }
        }
    }
}