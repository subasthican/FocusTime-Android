package com.example.focustime.ui.tasklist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focustime.R
import com.example.focustime.data.Task
import com.example.focustime.databinding.FragmentTaskListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private var isModalVisible = false
    
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private val TASKS_KEY = "saved_tasks"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        try {
            // Initialize SharedPreferences
            sharedPreferences = requireContext().getSharedPreferences("focus_time_prefs", Context.MODE_PRIVATE)
            
            setupRecyclerView()
            setupClickListeners()
            loadTasks() // Load from SharedPreferences after adapter is set up
            
            // Ensure FAB is visible when this fragment is shown
            binding.fabAddTask.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            tasks = tasks,
            onTaskClick = { task -> editTask(task) },
            onTaskToggle = { task -> toggleTask(task) },
            onTaskLongClick = { task -> showDeleteDialog(task) }
        )
        
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskModal()
        }

        binding.modalOverlay.setOnClickListener {
            hideAddTaskModal()
        }

        binding.root.findViewById<View>(R.id.tv_cancel).setOnClickListener {
            hideAddTaskModal()
        }

        binding.root.findViewById<View>(R.id.iv_close).setOnClickListener {
            hideAddTaskModal()
        }

        binding.root.findViewById<View>(R.id.tv_save).setOnClickListener {
            saveTask()
        }
    }

    private fun loadSampleTasks() {
        val sampleTasks = listOf(
            Task(
                id = "1",
                title = "Complete project proposal",
                description = "Write and submit the quarterly project proposal by Friday",
                isDone = false
            ),
            Task(
                id = "2", 
                title = "Review team feedback",
                description = "Go through all team member feedback and prepare response",
                isDone = true
            ),
            Task(
                id = "3",
                title = "Schedule team meeting",
                description = "Find a time that works for everyone next week",
                isDone = false
            )
        )
        
        tasks.addAll(sampleTasks)
        taskAdapter.notifyDataSetChanged()
        updateEmptyState()
    }
    
    private fun updateEmptyState() {
        if (tasks.isEmpty()) {
            binding.recyclerViewTasks.visibility = View.GONE
            // Show empty state if we had one
        } else {
            binding.recyclerViewTasks.visibility = View.VISIBLE
        }
    }

    private fun showAddTaskModal() {
        if (isModalVisible) return

        isModalVisible = true
        binding.apply {
            // Hide FAB when modal is shown
            fabAddTask.visibility = View.GONE
            
            modalOverlay.visibility = View.VISIBLE
            modalAddTask.visibility = View.VISIBLE

            // Reset form
            binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_task_title).text?.clear()
            binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_task_description).text?.clear()
            binding.root.findViewById<android.widget.TextView>(R.id.tv_modal_title).text = "Add New Task"

            // Apple-style slide-up animation
            modalAddTask.translationY = modalAddTask.height.toFloat()
            modalAddTask.alpha = 0f
            modalAddTask.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(400)
                .setInterpolator(DecelerateInterpolator())
                .start()

            // Animate overlay fade in
            modalOverlay.alpha = 0f
            modalOverlay.animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    private fun hideAddTaskModal() {
        if (!isModalVisible) return
        
        isModalVisible = false
        binding.apply {
            // Apple-style slide-down animation
            modalAddTask.animate()
                .translationY(modalAddTask.height.toFloat())
                .alpha(0f)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    modalAddTask.visibility = View.GONE
                }
                .start()

            // Animate overlay fade out
            modalOverlay.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    modalOverlay.visibility = View.GONE
                    // Show FAB again when modal is closed
                    fabAddTask.visibility = View.VISIBLE
                }
                .start()
        }
    }

    private fun saveTask() {
        val titleEditText = binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_task_title)
        val descriptionEditText = binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_task_description)
        
        val title = titleEditText.text?.toString()?.trim()
        val description = descriptionEditText.text?.toString()?.trim() ?: ""
        
        if (title.isNullOrEmpty()) {
            titleEditText.error = "Task title is required"
            return
        }
        
        val newTask = Task(
            id = System.currentTimeMillis().toString(),
            title = title,
            description = description,
            isDone = false
        )
        
        tasks.add(newTask)
        taskAdapter.notifyTaskInserted(tasks.size - 1)
        updateEmptyState()
        saveTasks() // Save to SharedPreferences
        
        // Scroll to the new task
        binding.recyclerViewTasks.smoothScrollToPosition(tasks.size - 1)
        
        hideAddTaskModal()
    }

    private fun editTask(task: Task) {
        showAddTaskModal()
        binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_task_title).setText(task.title)
        binding.root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_task_description).setText(task.description)
        binding.root.findViewById<android.widget.TextView>(R.id.tv_modal_title).text = "Edit Task"
    }

    private fun toggleTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            taskAdapter.notifyTaskChanged(index)
            saveTasks() // Save to SharedPreferences
        }
    }

    private fun showDeleteDialog(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete '${task.title}'? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteTask(task)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun deleteTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks.removeAt(index)
            taskAdapter.notifyTaskRemoved(index)
            updateEmptyState()
            saveTasks() // Save to SharedPreferences
            Toast.makeText(requireContext(), "Task deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Show FAB when fragment becomes visible
        binding.fabAddTask.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        // Hide FAB when fragment is not visible
        binding.fabAddTask.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    // SharedPreferences helper functions
    private fun saveTasks() {
        val tasksJson = gson.toJson(tasks)
        sharedPreferences.edit()
            .putString(TASKS_KEY, tasksJson)
            .apply()
        println("DEBUG: Saved ${tasks.size} tasks to SharedPreferences")
    }
    
    private fun loadTasks() {
        val tasksJson = sharedPreferences.getString(TASKS_KEY, null)
        if (tasksJson != null) {
            val type = object : TypeToken<List<Task>>() {}.type
            val savedTasks = gson.fromJson<List<Task>>(tasksJson, type)
            tasks.clear()
            tasks.addAll(savedTasks)
            println("DEBUG: Loaded ${savedTasks.size} tasks from SharedPreferences")
        } else {
            // Load sample tasks only if no saved data exists
            println("DEBUG: No saved tasks found, loading sample tasks")
            loadSampleTasks()
        }
        taskAdapter.notifyDataSetChanged()
        updateEmptyState()
    }
}
