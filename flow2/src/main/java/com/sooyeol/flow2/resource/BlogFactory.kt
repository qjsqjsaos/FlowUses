package com.sooyeol.flow2.resource

import com.example.shared.model.Blog
import com.example.shared.model.UiBlog

object BlogFactory {
    fun createBlogList(): List<Blog> {
        return arrayListOf(
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
    }
}