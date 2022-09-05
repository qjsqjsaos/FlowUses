package com.sooyeol.flow2.ui.mapper

import com.example.shared.model.Blog
import com.example.shared.model.UiBlog

private fun Blog.toUiBlog() = UiBlog(
    blog = this,
    isSelected = false
)

fun List<Blog>.toUiBlogList() = map(Blog::toUiBlog)