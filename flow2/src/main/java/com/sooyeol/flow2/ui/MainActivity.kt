package com.sooyeol.flow2.ui

import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shared.model.UiBlog
import com.sooyeol.flow2.databinding.ActivityMainBinding
import com.sooyeol.flow2.ui.uistate.UiState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel> { MainViewModelFactory() }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        lifecycleScope.launch {
            vm.blogList.collect { state ->

                if(state is UiState.Loading) {
                    return@collect
                }

                if (state is UiState.Success<*>) {
                    Log.d("결과", state.data.toString())
                } else {
                    Log.d("결과2", (state as UiState.Failure).exception?.message!!.toString())
                }
            }
        }
    }
}

