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