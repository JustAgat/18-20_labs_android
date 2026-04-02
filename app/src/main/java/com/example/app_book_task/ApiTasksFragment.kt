package com.example.app_book_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_book_task.databinding.FragmentApiTasksBinding
import com.example.app_book_task.network.ApiService
import com.example.app_book_task.network.ApiTodo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiTasksFragment : Fragment() {

    private var _binding: FragmentApiTasksBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApiTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        binding.progressBar.visibility = View.VISIBLE
        service.getTodos().enqueue(object : Callback<List<ApiTodo>> {
            override fun onResponse(call: Call<List<ApiTodo>>, response: Response<List<ApiTodo>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val todos = response.body() ?: emptyList()
                    // Reusing Task model or using a different adapter for API todos
                    // For simplicity, let's just show titles in a Toast or update a list
                    Toast.makeText(context, "Loaded ${todos.size} items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ApiTodo>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}