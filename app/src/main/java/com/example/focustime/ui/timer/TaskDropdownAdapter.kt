package com.example.focustime.ui.timer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.focustime.R

class TaskDropdownAdapter(
    private val tasks: List<String>,
    private val onTaskSelected: (String) -> Unit
) : RecyclerView.Adapter<TaskDropdownAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_dropdown_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.textView.text = tasks[position]
        holder.itemView.setOnClickListener {
            onTaskSelected(tasks[position])
        }
    }

    override fun getItemCount(): Int = tasks.size
}

