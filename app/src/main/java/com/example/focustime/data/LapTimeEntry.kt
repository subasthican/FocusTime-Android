package com.example.focustime.data

import java.util.Date

data class LapTimeEntry(
    val id: String,
    val lapNumber: Int,
    val timeInMillis: Long,
    val taskName: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getFormattedTime(): String {
        val totalSeconds = timeInMillis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    
    fun getFormattedDate(): String {
        val date = Date(timestamp)
        val formatter = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
        return formatter.format(date)
    }
}

