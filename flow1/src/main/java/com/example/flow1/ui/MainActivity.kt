package com.example.flow1.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.flow1.R
import com.example.flow1.databinding.ActivityMainBinding
import com.example.flow1.ui.extension.Extensions.textChangesToFlow
import com.example.flow1.ui.widget.UiState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observer()

        lifecycleScope.launch {
            val nameTextFlow = binding.name.textChangesToFlow(UiState.EditType.NAME)
            val addressTextFlow = binding.address.textChangesToFlow(UiState.EditType.ADDRESS)
            val phoneNumTextFlow = binding.phoneNum.textChangesToFlow(UiState.EditType.PHONENUM)

            // merge : 요소의 순서를 유지하지 않고, 다수의 flow를 단일 flow로 병합합니다.
            // 흐름의 수는 제한이 없습니다.
            merge(nameTextFlow, addressTextFlow, phoneNumTextFlow)
                .collect { vm.emitData(it) }

            // 아래 함수를 표현하는 것이 위에 merge 함수이다.
//            flowOf(nameTextFlow, addressTextFlow, phoneNumTextFlow)
//                .flattenMerge()
//                .collect { vm.emitData(it) }
        }
    }

    private fun observer() {
        lifecycleScope.launch(Dispatchers.Main) {
            // collect : 데이터가 들어오는 것과 별개로 수행작업들이 순차적으로 실행이 된다.
            // 모든 값을 수집하는 장점이 있다.
            // 하지만, 여러 데이터 중 중간에 하나의 데이터가 시간을 무제한으로 잡아먹게 되면,
            // 이 후 발행되는 데이터는 모두 처리가 되지 않는다. (앞에 것이 끝날 때까지 기다려야한다.)

            // collectLatest : 최신 데이터가 들어오면 현재 작업을 중지하고 최신 데이터에 따른 수행작업을 우선으로 둔다.
            // 최신 데이터를 이용해 빠르게 UI를 그릴 수 있어서, 더욱 나은 사용자 경험을 줄 수 있습니다.
            // 하지만, 데이터를 발행하는 시간이 빠르고, 수행작업의 시간이 더 오래걸릴 경우 새로 들어온 데이터는
            // 계속해서 취소가 됩니다. 결국 마지막 데이터만 가져오게 되는 상황이 생겨버릴 수 있습니다.

            // 결론: 데이터에 따른 모든 업데이트가 중요하다면 collect를 사용해야하고,
            // 데이터베이스 업데이트 같은 손실 없이 일부 업데이트를 무시해도 되는 작업은 collectLatest를 사용해야 합니다.


            //combine : 다수의 flow 들의 가장 최근 값을 결합하여, flow로 반환합니다.
            //merge랑 다른 점은 이렇게 name, address, phoneNum 처럼 인자를 넘겨 받고 그의 대한
            //반환을 할 수 있는 인터페이스를 제공한다는 점, 순서를 유지하지 않는 단일 flow로 만들어 준다는 점이다.
            combine(vm.name, vm.address, vm.phoneNum) { name, address, phoneNum ->

                val isNameWritten = name.editState == UiState.EditState.WRITTEN
                val isAddressWritten = address.editState == UiState.EditState.WRITTEN
                val isPhoneNumWritten = phoneNum.editState == UiState.EditState.WRITTEN

                //결과 값을 3개의 불리언의 조건식으로 반환합니다.
                //현재 EditText가 모두 타이핑이 되어 있는지 확인하고 값을 넘겨줍니다.
                isNameWritten && isAddressWritten && isPhoneNumWritten

            }.collect {
                //만약 collect로 넘어온 값이 true이면 버튼의 색상을 활성화 합니다.
                binding.button.apply {
                    text = if(it) {
                        setBackgroundResource(R.color.purple_700)
                        "활성화"
                    } else {
                        setBackgroundResource(R.color.grey)
                        "비활성화"
                    }
                }
            }
        }
    }
}