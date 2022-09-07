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
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.consumeAsFlow
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

                /**
                 * 정리
                 *
                 * StateFlow 특징
                 * 초깃값이 필요하다. 생성자에 초기값을 반드시 명시해야한다. (항상 값을 가지고 있고, 오직 한 가지 값을 가진다.)
                 * emit()과 value 속성 이용가능
                 * collector의 수에 관계없이 항상 구독하고 있는 것의 최신 값을 받는다.
                 * hot stream이다.
                 * collect를 활성화한 시점부터 방출된 데이터를 가져온다.
                 * 업데이트 된 후에 값을 반환하고 동일한 값을 반환하지 않습니다.(conflate)
                 *
                 * SharedFlow 특징
                 * 초깃값이 필요없습니다.
                 * value 사용 불가, emit()만 사용가능
                 * StateFlow의 기본적인 원형의 가까운 형태로 볼 수 있다.
                 * StateFlow처럼 최신값을 보내는 것과는 다르게 내보낼 이전의 값 수를 구성할 수 있다. (replay)
                 * emit된 데이터를 저장(cache)할 버퍼의 갯수를 지정할 수 있습니다. (extraBufferCapacity)
                 * buffer가 다 찼을때 동작을 정의할 수 있습니다. (onBufferOverflow)
                 * hot stream입니다.
                 * 업데이트하면 이전 값이 동일하든 말든 값이 반환됩니다. (conflate 따로 설정해야함.)
                * */

                launch {
                    vm.uiChannel
//                        .consumeAsFlow() //find, map, filter, first 같은 함수들을 사용하려면 cosumeAsFlow()
                    //함수를 이용해 flow로 전환하고 사용해야한다.

//                        .receive() //리시브로 받는 방법
                    //이 방법을 이용하려면 채널의 데이터를 보내지 않거나, 받지 않으면 close()메서드를
                    //통해 채널을 종료해주어야한다. close() 메서드 이후 send()나 receive() 메서드를 호출하면,
                    // ClosedReceiveChannelException 예외가 발생됩니다.

//                        .consumeEach {  } //consumeEach와 아래 코드처럼 for문을 돌리는 방법은
                    //실행하는데 매우 유사한 방법이지만,
                    //consumeEach는 실행중 에러가 발생하면, 채널 자체가 close되지만,
                    //for문을 돌리는 방법은 for문만 중단이 되고, channel은 종료되지 않는다.
                    //이 채널을 사용하고 있는 다른 곳은 정상적인 동작을 이어 간다는 것이다.
                    //가이드 문서에는 for-loop를 완벽하게 안전한 것이라 표현함.
                    //데이터를 receive할때에는 for-loop사용 권장

                    for(x in vm.uiChannel) {
                        if (x is UiState.Success<*>) {
                            Log.d("uiChannel : ", x.data.toString())
                        }
                    }
                }

                /**
                 * Channel 특징
                 * flow가 단일방향으로 데이터를 던지고 받는 형식이라면,
                 * channel은 여러 방향에서 데이터를 던지고 받는 형식, 코루틴 끼리의 데이터를 전달해준다.
                 * 현재 위 코드 사용은 클릭리스너를 위한 방법이므로, 일반적으로는 코루틴 간의 데이터 통신을 위해 사용한다.
                 * 데이터 배출은 hot stream이어서 사용을 기다리지 않고, 한번에 배출한다.
                 * 쉽게 말해 한번 밖에 배출하지 않아서, flow처럼 여러곳에서 동일한 데이터를 받는 것은 불가능하다.
                 * 버퍼 사이즈나 형태를 지정할 수 있다.
                 * FIFO (first-in first-out) 선입선출식으로 동작하며, 하나의 채널이 독점하지 않고,
                 * 순차적으로 데이터를 가져간다.
                 * */
            }
        }
        clickListener()
    }

    private fun clickListener() {
        //데이터 가져오기
        binding.sameData.setOnClickListener { vm.getData() }
    }
}