package com.sooyeol.flow2.data.repository

import com.example.shared.model.Blog
import com.sooyeol.flow2.domain.repository.Repository
import com.sooyeol.flow2.resource.BlogFactory.createBlogList

class RepositoryImpl : Repository {
    override suspend fun getBlogList(): List<Blog> {
        return createBlogList()
    }
}