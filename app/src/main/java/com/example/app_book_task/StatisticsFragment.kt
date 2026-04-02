package com.example.app_book_task

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_book_task.databinding.FragmentStatisticsBinding
import com.example.app_book_task.db.DBHelper
import java.text.SimpleDateFormat
import java.util.*

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())
        
        // Получаем userId из SharedPreferences
        val authPrefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = authPrefs.getInt("user_id", -1)

        if (userId != -1) {
            val tasks = dbHelper.getAllTasks(userId)
            
            // 1. Основные показатели
            val total = tasks.size
            val completed = tasks.count { it.isCompleted }
            val pending = total - completed

            binding.tvTotalTasks.text = getString(R.string.total_tasks, total)
            binding.tvCompletedTasks.text = getString(R.string.completed_tasks, completed)
            binding.tvPendingTasks.text = getString(R.string.pending_tasks, pending)

            // 2. Группировка по дате
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val statsByDate = tasks.groupBy { dateFormat.format(Date(it.date)) }

            val detailsBuilder = StringBuilder()
            statsByDate.forEach { (date, tasksOnDate) ->
                val dailyTotal = tasksOnDate.size
                val dailyCompleted = tasksOnDate.count { it.isCompleted }
                val dailyPending = dailyTotal - dailyCompleted
                
                detailsBuilder.append("📅 $date\n")
                detailsBuilder.append("   • Total: $dailyTotal\n")
                detailsBuilder.append("   • Done: $dailyCompleted\n")
                detailsBuilder.append("   • Pending: $dailyPending\n\n")
            }

            binding.tvStatsDetails.text = detailsBuilder.toString()
        } else {
            // Если пользователь не найден (не должно случаться при правильной навигации)
            binding.tvTotalTasks.text = "Please login to see statistics"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
