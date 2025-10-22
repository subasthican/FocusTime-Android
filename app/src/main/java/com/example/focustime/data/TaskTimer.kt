package com.example.focustime.data

data class TaskTimer(
    val taskTitle: String,
    val elapsedTime: Long = 0L, // in milliseconds
    val isRunning: Boolean = false,
    val startTime: Long = 0L,
    val pausedTime: Long = 0L
) {
    companion object {
        fun createEmpty(): TaskTimer {
            return TaskTimer(
                taskTitle = "No task selected",
                elapsedTime = 0L,
                isRunning = false
            )
        }
    }
    
    fun getFormattedTime(): String {
        val totalSeconds = elapsedTime / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}


