package com.example.app_book_task.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_book_task.databinding.ItemTaskBinding
import com.example.app_book_task.models.Task

class TasksAdapter(
    private var tasks: List<Task>,
    private val onStatusChange: (Task, Boolean) -> Unit,
    private val onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.tvTitle.text = task.title
        holder.binding.tvDescription.text = task.description
        
        // Handle checkbox
        holder.binding.cbCompleted.setOnCheckedChangeListener(null)
        holder.binding.cbCompleted.isChecked = task.isCompleted
        updateStrikeThrough(holder, task.isCompleted)
        
        holder.binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            onStatusChange(task, isChecked)
            updateStrikeThrough(holder, isChecked)
        }

        holder.binding.btnDelete.setOnClickListener { onDeleteClick(task) }
    }

    private fun updateStrikeThrough(holder: TaskViewHolder, isCompleted: Boolean) {
        if (isCompleted) {
            holder.binding.tvTitle.paintFlags = holder.binding.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvDescription.paintFlags = holder.binding.tvDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.tvTitle.paintFlags = holder.binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.binding.tvDescription.paintFlags = holder.binding.tvDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
