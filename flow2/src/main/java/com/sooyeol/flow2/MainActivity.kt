package com.sooyeol.flow2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sooyeol.flow2.databinding.ActivityMainBinding

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

    }
}