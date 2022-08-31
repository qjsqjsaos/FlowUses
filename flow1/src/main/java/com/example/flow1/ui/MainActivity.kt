package com.example.flow1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.flow1.R
import com.example.flow1.ui.widget.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            vm.apply {
                flowOf(name, address, phoneNum).collectLatest {
                    it.value.editState = UiState.EditState.WRITTEN

                }
            }
        }
    }

    private fun render(state: UiState) {
        when (state.editState) {
            UiState.EditState.EMPTY -> {

            }
            UiState.EditState.WRITTEN -> {

            }
        }
    }
}