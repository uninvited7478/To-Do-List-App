package com.example.todolistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_tasks")
data class Task(
    var name: String,
    var description: String,
    var isCompleted: Boolean = false,
    var isImportant: Boolean = false,
    @PrimaryKey
    val taskId: Long = System.currentTimeMillis()
)