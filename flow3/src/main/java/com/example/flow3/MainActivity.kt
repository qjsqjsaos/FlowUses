package com.example.flow3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
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
            collecter()
        }
    }

    private suspend fun collecter() {
        vm.uiState.collect { state ->
            if (state is UiState.Success<*>) {
                //recyclerview 만들기
            }
        }
    }
}