package com.sooyeol.flow2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sooyeol.flow2.data.repository.RepositoryImpl
import com.sooyeol.flow2.domain.usecase.UseCase

class MainViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(UseCase(RepositoryImpl())) as T
        }else{
            throw IllegalArgumentException()
        }
    }
}