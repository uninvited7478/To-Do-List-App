package com.example.todolistapp.data

import com.example.todolistapp.database.ToDoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow

class ToDoRepository(private val dao: ToDoDao) {

    val tasksFlow = dao.getAllTasks()

    fun getTaskById(taskId: Long) = dao.getTaskById(taskId)

    suspend fun addTask(task: Task) {
        withContext(Dispatchers.IO) {
            dao.insertTask(task)
        }
    }

    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            dao.updateTask(task)
        }
    }

    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            dao.deleteTask(task)
        }
    }
}