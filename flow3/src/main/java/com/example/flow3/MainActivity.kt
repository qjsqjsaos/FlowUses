package com.example.flow3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.flow3.databinding.ActivityMainBinding
import com.example.shared.model.uistate.UiState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            /**
             * flow는 livedata처럼 lifecycle의 흐름대로 자동으로 종료되거나 재시작 되지 않습니다.
             * 만약 앱이 백그라운드로 가더라도 계속 emit된 값을 collect합니다.
             * 그래서 repeatOnLifecycle Lifecycle.State.STARTED를 사용합니다.
             * STARTED를 보게 되면,
             * activity가 onstart 되는 시점이나 onPause 되는 시점에 새로운 coroutine으로 시작되며, onStop시점에 cancel 됩니다.
             * */
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //StateFlow
                launch {
                    vm.uiState.collect { state ->
                        if (state is UiState.Success<*>) {
                            Log.d("uiState : ", state.data.toString())
                        }
                    }
                }
                //SharedFlow
                launch {
                    vm.uiShared.collect { state ->
                        if (state is UiState.Success<*>) {
                            Log.d("uiShared : ", state.data.toString())
                        }
                    }
                }

                /**
                 * StateFlow와 ShareFlow의 공통점은 HotStream이다.
                 * HotStream이란 한 flow에서 emit을 할때 여러 구독자(collect)가 동시에 데이터를 구독할 수 있는 스트림을 말한다.
                 * 예시로는 라디오 방송국이 있다. 우리가 라디오 방송국에서 나오는 라디오 주파수를 맞추게 되면, 동시에 여러 사람들이
                 * 라디오를 들을 수 있는 상황과 같다고 보면 된다.
                 * HotStream은 중간에 collect를 하면 처음부터 값을 받을 수도 있지만, 중간에 받게 되면 중간부터 데이터를 받게 된다.
                 * (라디오처럼)
                 * 추가적을 ColdStream은 무엇일까?
                 * ColdStream은 하나의 구독자만을 위한 스트림이다. HotStream이 외부에서 데이터를 받고 왔다면, ColdStream은
                 * 내부에서 데이터를 생성하여 하나의 구독자에게 전달한다.
                 * 이 말은 여러 구독자가 있어도 각각의 인스턴스를 가진 스트림을 부여하기 때문에 독립성을 띄고 있다.
                 * 하나의 인스턴스로 공유하는 HotStream과 다른 점이다.
                 *
                 * 그렇다면 이 StateFlow와 ShareFlow의 차이점은 무엇일까?
                 * 바로 conflate이다.
                 * conflate란 사전적으로 두개의 값을 하나로 병합한다는 의미이다.
                 * 하지만 flow에서는 약간 다르게 쓰인다.
                 * flow에서는 collect로 넘어온 값이 이전값과 동일하다면 스킵(병합)시키는 기능으로 통한다.
                 * StateFlow에는 내부적으로 conflate가 구현되어 있다.
                 * 고로, 최신값이 이전값과 동일하다면, 값이 넘어오질 않는다.
                 * 하지만, SharedFlow는 conflate가 구현되어 있지않다.
                 * conflate() 메서드를 통해 conflate 기능을 따로 구현해주어야 한다.
                 * */
            }
        }
        clickListener()
    }

    private fun clickListener() {
        binding.sameData.setOnClickListener {
            vm.setSameData()
        }
    }
}