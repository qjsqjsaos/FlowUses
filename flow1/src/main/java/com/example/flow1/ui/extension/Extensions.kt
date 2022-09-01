package com.example.flow1.ui.extension

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.example.flow1.ui.widget.UiState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

object Extensions {

    fun EditText.textChangesToFlow(editType: UiState.EditType): Flow<UiState?> {
        // callbackFlow : callback으로 반환받은 데이터를 flow로 converting 해줍니다.
        // 내부적으로 channel을 생성하는 ProducerScope 입니다.
        // 고로, 데이터 전달을 trySend로 할 수 있습니다. (기존의 offer)
        // 하나의 flow를 사용할 수 있는 builder라고 보셔도 됩니다.
        // 내부적으로 보시게 되면 buffer는 기본값으로 64개를 사용하게 되는데,
        // buffer에 데이터가 넘어갈 때 처리하는 옵션으로 onBufferOverflow 인자가 있는데,
        // 기본적인 값으로 BufferOverflow.SUSPEND로 되어있습니다. 이 옵션은 channel에 data가 64개가 넘어가면,
        // suspending 처리가 되어가 send가 block됩니다.
        return callbackFlow {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    // 기존에 offer은 이제 Deprecated가 되었고, trySend로 대체된다.
                    // send와의 차이점은 send는 비동기식(suspend)이고, trySend는 즉시 값을 얻을 수 있는 동기식(일반 함수)입니다.
                    trySend(
                        UiState(
                        editType,
                        if(text.toString().isEmpty())
                            UiState.EditState.EMPTY
                        else
                            UiState.EditState.WRITTEN
                        )
                    )
                }
            }
            //TextWatcher의 해당 리스너를 추가해줍니다.
            //awaitClose에 블록이 실행된 후에 추가 됩니다.
            addTextChangedListener(listener)

            // 콜백이 사라질때 실행, 리스너 제거
            // awaitClose : 채널이 닫히거나 취소되면, 현재의 코루틴을 일시정지하고 재개하기전에 호출됩니다.
            awaitClose { removeTextChangedListener(listener) }
        }.onStart {
            // 학습을 위해 당장은 필요없지만 적어둠
            // onStart 실행 후 -> callbackFlow 실행
            // removeTextChangedListener 리스너가 제거 되기 전까지는 한번만 실행
            Log.d("onStart", "onStart 실행")
        }
    }

}