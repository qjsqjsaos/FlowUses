package com.sooyeol.flow2.domain.usecase

import com.example.shared.model.Blog
import com.sooyeol.flow2.domain.repository.Repository

class UseCase(private val repository: Repository) {
    suspend fun getBlogList(): List<Blog> {
        return repository.getBlogList()
    }
}