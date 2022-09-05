package com.sooyeol.flow2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shared.model.UiBlog
import com.sooyeol.flow2.databinding.ActivityMainBinding
import com.sooyeol.flow2.ui.UiState.UiState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainViewModel>()

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
                if (state is UiState.Success<*>) {
                    Log.d("결과", (state.data as UiBlog).toString())
//                    binding.recyclerView.apply {
//                        setHasFixedSize(true)
//                        layoutManager = GridLayoutManager(this@SooyeolActivity, 2)
//                        adapter = BlogListAdapter(state.data as List<UiBlog>)
//                    }
                } else {
                    Toast.makeText(this@MainActivity, (state as UiState.Failure).exception?.message!!, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}