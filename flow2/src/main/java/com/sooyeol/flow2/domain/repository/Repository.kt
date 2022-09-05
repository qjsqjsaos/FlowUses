package com.sooyeol.flow2.domain.repository

import com.example.shared.model.Blog

interface Repository {
    suspend fun getBlogList(): List<Blog>
}