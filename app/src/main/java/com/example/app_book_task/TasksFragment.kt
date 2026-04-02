package com.example.app_book_task

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_book_task.adapters.TasksAdapter
import com.example.app_book_task.databinding.FragmentTasksBinding
import com.example.app_book_task.db.DBHelper
import com.example.app_book_task.models.Task

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: TasksAdapter
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())
        
        val authPrefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        userId = authPrefs.getInt("user_id", -1)

        val settingsPrefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val sortByDate = settingsPrefs.getBoolean("sort_date", false)

        adapter = TasksAdapter(
            dbHelper.getAllTasks(userId, sortByDate),
            onStatusChange = { task, isCompleted ->
                dbHelper.updateTaskStatus(task.id, isCompleted)
            },
            onDeleteClick = { task ->
                dbHelper.deleteTask(task.id)
                adapter.updateTasks(dbHelper.getAllTasks(userId, sortByDate))
            }
        )

        binding.rvTasks.layoutManager = LinearLayoutManager(context)
        binding.rvTasks.adapter = adapter

        binding.btnAddTask.setOnClickListener {
            val title = binding.etTaskTitle.text.toString()
            val description = binding.etTaskDescription.text.toString()

            if (title.isNotEmpty() && userId != -1) {
                dbHelper.addTask(Task(userId = userId, title = title, description = description))
                adapter.updateTasks(dbHelper.getAllTasks(userId, sortByDate))
                binding.etTaskTitle.text.clear()
                binding.etTaskDescription.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
