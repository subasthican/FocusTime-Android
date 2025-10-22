package com.example.focustime.data

data class Task(
    val id: String = "",
    val title: String,
    val description: String = "",
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun createEmpty(): Task {
            return Task(
                id = System.currentTimeMillis().toString(),
                title = "",
                description = "",
                isDone = false
            )
        }
    }
}


