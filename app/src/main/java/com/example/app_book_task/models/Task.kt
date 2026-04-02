package com.example.app_book_task.models

data class Task(
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String,
    val date: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)