package com.example.app_book_task.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("todos")
    fun getTodos(): Call<List<ApiTodo>>
}