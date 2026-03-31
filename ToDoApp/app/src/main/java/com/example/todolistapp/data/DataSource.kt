package com.example.todolistapp.data

import androidx.compose.runtime.mutableStateListOf

object DataSource {
    private val tasks = mutableStateListOf<Task>().apply { addAll(getDummyContent(3)) }

    fun getDummyContent(numElements: Int): List<Task> {
        val list = mutableListOf<Task>()
        for (i in 0 until numElements) {
            list.add(
                Task(
                    name = "Task $i",
                    description = "Description $i",
                    taskId = System.currentTimeMillis() + i * 1000
                )
            )
        }
        return list
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getTasks(): List<Task> {
        return tasks
    }

    fun deleteTask(task: Task) {
        tasks.remove(task)
    }

    fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.taskId == task.taskId }
        if (index != -1) {
            tasks[index] = task
        }
    }

    fun getTask(id: Long): Task {
        return tasks.first { it.taskId == id }
    }
}