package com.example.focustime.ui.timer

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focustime.R
import com.example.focustime.data.LapTimeEntry
import com.example.focustime.data.Task
import com.example.focustime.data.TaskTimer
import com.example.focustime.databinding.FragmentTimerBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var lapTimeAdapter: LapTimeAdapter
    private var taskTimer = TaskTimer.createEmpty()
    private var timerHandler: Handler? = null
    private var timerRunnable: Runnable? = null
    private var lapCounter = 0
    private var isRunning = false
    
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private val LAP_TIMES_KEY = "saved_lap_times"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("focus_time_prefs", Context.MODE_PRIVATE)
        
        setupRecyclerView()
        setupClickListeners()
        loadLapTimes() // Load saved lap times
        updateUI()
        
        // Ensure task selector is clickable on initial load
        binding.tvTaskSelector.isClickable = true
        binding.tvTaskSelector.alpha = 1.0f
        println("DEBUG: Initial setup - taskSelector clickable: ${binding.tvTaskSelector.isClickable}")
    }

    private fun setupRecyclerView() {
        lapTimeAdapter = LapTimeAdapter()
        binding.recyclerViewLapTimes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lapTimeAdapter
        }
        println("DEBUG: RecyclerView setup complete - adapter: $lapTimeAdapter")
    }

    private fun setupClickListeners() {
        binding.btnStart.setOnClickListener {
            if (isRunning) {
                // Timer is already running, do nothing
                return@setOnClickListener
            } else if (taskTimer.elapsedTime > 0) {
                // Timer is paused, resume it
                resumeTimer()
            } else {
                // Timer is stopped, start fresh
                startTimer()
            }
        }

        binding.btnPause.setOnClickListener {
            pauseTimer() // Only pause
        }

        binding.btnReset.setOnClickListener {
            resetTimer() // Only reset
        }
        
        // Long press on reset button to clear lap times
        binding.btnReset.setOnLongClickListener {
            clearLapTimes()
            true // Consume the long click
        }

        binding.btnLap.setOnClickListener {
            addLapTime() // Add lap time
        }

        binding.tvTaskSelector.setOnClickListener {
            println("DEBUG: Task selector clicked - isRunning: $isRunning")
            println("DEBUG: Task selector clickable: ${binding.tvTaskSelector.isClickable}")
            println("DEBUG: Task selector alpha: ${binding.tvTaskSelector.alpha}")
            
            if (!isRunning) {
                println("DEBUG: Timer not running, showing task selector")
                showTaskSelector()
            } else {
                println("DEBUG: Timer running, showing toast")
                // Show message that task can't be changed while running
                android.widget.Toast.makeText(requireContext(), "Cannot change task while timer is running", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        // Also add a touch listener to see if touches are being detected
        binding.tvTaskSelector.setOnTouchListener { view, event ->
            println("DEBUG: Task selector touched - action: ${event.action}")
            false // Let the click listener handle it
        }
    }

    private fun startTimer() {
        isRunning = true
        taskTimer = taskTimer.copy(isRunning = true, startTime = System.currentTimeMillis())
        
        // Update button states
        updateButtonStates()
        
        // Start timer
        startTimerUpdates()
        
        // Animate start button
        animateButtonPress(binding.btnStart)
        
        // Debug: RecyclerView should be working now
        println("DEBUG: Timer started, RecyclerView ready for lap times")
    }

    private fun pauseTimer() {
        isRunning = false
        taskTimer = taskTimer.copy(isRunning = false, pausedTime = taskTimer.elapsedTime)
        
        // Update button states
        updateButtonStates()
        
        // Stop timer
        stopTimerUpdates()
        
        // Animate pause button
        animateButtonPress(binding.btnPause)
    }

    private fun resumeTimer() {
        isRunning = true
        // Calculate new start time: current time minus the already elapsed time
        val currentTime = System.currentTimeMillis()
        val newStartTime = currentTime - taskTimer.pausedTime
        taskTimer = taskTimer.copy(isRunning = true, startTime = newStartTime, pausedTime = 0)
        
        // Update button states
        updateButtonStates()
        
        // Start timer
        startTimerUpdates()
        
        // Animate start button
        animateButtonPress(binding.btnStart)
    }

    private fun resetTimer() {
        isRunning = false
        taskTimer = TaskTimer.createEmpty()
        lapCounter = 0
        
        // Update UI
        updateUI()
        
        // Don't clear lap times - keep them for reference
        // lapTimeAdapter.clearLapTimes() // Removed - lap times should persist
        
        // Stop timer
        stopTimerUpdates()
        
        // Animate reset button
        animateButtonPress(binding.btnReset)
    }

    private fun startTimerUpdates() {
        timerHandler = Handler(Looper.getMainLooper())
        timerRunnable = object : Runnable {
            override fun run() {
                if (isRunning) {
                    val currentTime = System.currentTimeMillis()
                    val elapsed = currentTime - taskTimer.startTime + taskTimer.pausedTime
                    taskTimer = taskTimer.copy(elapsedTime = elapsed)
                    
                    updateTimerDisplay()
                    
                    timerHandler?.postDelayed(this, 100) // Update every 100ms for smooth animation
                }
            }
        }
        timerHandler?.post(timerRunnable!!)
    }

    private fun stopTimerUpdates() {
        timerHandler?.removeCallbacks(timerRunnable!!)
    }

    private fun updateTimerDisplay() {
        val formattedTime = taskTimer.getFormattedTime()
        
        // Smooth digit transition animation
        if (binding.tvTimerDisplay.text != formattedTime) {
            animateTimerChange(formattedTime)
        }
    }

    private fun animateTimerChange(newTime: String) {
        val animator = ObjectAnimator.ofFloat(binding.tvTimerDisplay, "scaleX", 1f, 0.8f, 1f)
        animator.duration = 200
        animator.interpolator = OvershootInterpolator()
        animator.addUpdateListener {
            if (it.animatedFraction > 0.5f) {
                binding.tvTimerDisplay.text = newTime
            }
        }
        animator.start()
    }

    private fun updateButtonStates() {
        // Start button always stays as play button since we have separate pause button
        binding.btnStart.text = "▶"
        binding.btnStart.background = resources.getDrawable(R.drawable.timer_button_start, null)
        
        // Update task selector appearance based on timer state
        if (isRunning) {
            binding.tvTaskSelector.alpha = 0.5f
            binding.tvTaskSelector.isClickable = false
        } else {
            binding.tvTaskSelector.alpha = 1.0f
            binding.tvTaskSelector.isClickable = true
        }
        
        println("DEBUG: updateButtonStates - isRunning: $isRunning, taskSelector clickable: ${binding.tvTaskSelector.isClickable}")
    }

    private fun updateUI() {
        binding.tvTimerDisplay.text = taskTimer.getFormattedTime()
        binding.tvTaskSelector.text = taskTimer.taskTitle
        updateButtonStates()
    }

    private fun animateButtonPress(button: View) {
        val scaleDown = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.95f)
        val scaleUp = ObjectAnimator.ofFloat(button, "scaleX", 0.95f, 1f)
        
        scaleDown.duration = 100
        scaleUp.duration = 100
        scaleUp.interpolator = OvershootInterpolator()
        
        scaleDown.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                scaleUp.start()
            }
        })
        
        scaleDown.start()
    }

    private fun showTaskSelector() {
        println("DEBUG: showTaskSelector called")
        if (binding.cardTaskDropdown.visibility == View.VISIBLE) {
            println("DEBUG: Hiding dropdown")
            // Hide dropdown
            hideTaskDropdown()
        } else {
            println("DEBUG: Showing dropdown")
            // Show dropdown
            showTaskDropdown()
        }
    }

    private fun showTaskDropdown() {
        // Get real tasks from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("focus_time_prefs", Context.MODE_PRIVATE)
        val tasksJson = sharedPreferences.getString("saved_tasks", null)
        
        val realTasks = mutableListOf<String>()
        realTasks.add("No task selected") // Always include this option
        
        if (tasksJson != null) {
            try {
                val gson = Gson()
                val type = object : TypeToken<List<Task>>() {}.type
                val tasks = gson.fromJson<List<Task>>(tasksJson, type)
                tasks.forEach { task ->
                    if (!task.isDone) { // Only show incomplete tasks
                        realTasks.add(task.title)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // If no real tasks, add some sample tasks
        if (realTasks.size == 1) {
            realTasks.addAll(listOf(
                "Focus on coding",
                "Read documentation", 
                "Review code",
                "Plan next sprint"
            ))
        }
        
        // Setup dropdown adapter
        val adapter = TaskDropdownAdapter(realTasks) { selectedTask ->
            taskTimer = taskTimer.copy(taskTitle = selectedTask)
            binding.tvTaskSelector.text = selectedTask
            hideTaskDropdown()
        }
        
        binding.recyclerTaskDropdown.layoutManager = LinearLayoutManager(context)
        binding.recyclerTaskDropdown.adapter = adapter
        
        // Show dropdown with animation
        binding.cardTaskDropdown.visibility = View.VISIBLE
        binding.cardTaskDropdown.alpha = 0f
        binding.cardTaskDropdown.animate()
            .alpha(1f)
            .setDuration(200)
            .start()
    }

    private fun hideTaskDropdown() {
        binding.cardTaskDropdown.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                binding.cardTaskDropdown.visibility = View.GONE
            }
            .start()
    }

    private fun addLapTime() {
        // Always animate button press
        animateButtonPress(binding.btnLap)
        
        println("DEBUG: addLapTime called - isRunning: $isRunning, elapsedTime: ${taskTimer.elapsedTime}")
        println("DEBUG: lapCounter before: $lapCounter")
        println("DEBUG: lapTimeAdapter: $lapTimeAdapter")
        
        if (isRunning) {
            lapCounter++
            val currentTime = taskTimer.elapsedTime
            val taskName = if (taskTimer.taskTitle.isNotEmpty()) taskTimer.taskTitle else "No task selected"
            lapTimeAdapter.addLapTime(lapCounter, currentTime, taskName)
            saveLapTimes() // Save to SharedPreferences
            println("DEBUG: Lap $lapCounter added at ${currentTime}ms for task: $taskName")
            println("DEBUG: lapTimeAdapter itemCount: ${lapTimeAdapter.itemCount}")
        } else {
            println("DEBUG: Timer not running, lap not added")
        }
    }
    
    private fun clearLapTimes() {
        lapTimeAdapter.clearLapTimes()
        lapCounter = 0
        saveLapTimes() // Save empty list
        android.widget.Toast.makeText(requireContext(), "Lap times cleared", android.widget.Toast.LENGTH_SHORT).show()
        println("DEBUG: Lap times cleared")
    }
    
    // SharedPreferences helper functions for lap times
    private fun saveLapTimes() {
        val lapTimesList = lapTimeAdapter.getLapTimesList()
        val lapTimesJson = gson.toJson(lapTimesList)
        sharedPreferences.edit()
            .putString(LAP_TIMES_KEY, lapTimesJson)
            .apply()
        println("DEBUG: Saved ${lapTimesList.size} lap times to SharedPreferences")
    }
    
    private fun loadLapTimes() {
        val lapTimesJson = sharedPreferences.getString(LAP_TIMES_KEY, null)
        if (lapTimesJson != null) {
            try {
                val type = object : TypeToken<List<LapTimeEntry>>() {}.type
                val savedLapTimes = gson.fromJson<List<LapTimeEntry>>(lapTimesJson, type)
                lapTimeAdapter.setLapTimes(savedLapTimes)
                
                // Update lap counter to continue from the last lap number
                if (savedLapTimes.isNotEmpty()) {
                    lapCounter = savedLapTimes.maxOf { it.lapNumber }
                }
                
                println("DEBUG: Loaded ${savedLapTimes.size} lap times from SharedPreferences")
            } catch (e: Exception) {
                e.printStackTrace()
                println("DEBUG: Error loading lap times from SharedPreferences")
            }
        } else {
            println("DEBUG: No saved lap times found")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimerUpdates()
        _binding = null
    }
}

