package com.example.flow3.resource

import com.example.shared.model.Blog
import com.example.shared.model.UiBlog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

object BlogFactory {
    fun createBlogList(): Flow<List<Blog>> {
        return flow {
            emit(
                arrayListOf(
                    Blog("제목1", "내용1"),
                    Blog("제목2", "내용2"),
                    Blog("제목3", "내용3"),
                    Blog("제목4", "내용4"),
                    Blog("제목5", "내용5"),
                    Blog("제목6", "내용6"),
                    Blog("제목7", "내용7"),
                    Blog("제목8", "내용8"),
                    Blog("제목9", "내용9"),
                    Blog("제목10", "내용10"),
                )
            )
        }
    }
}