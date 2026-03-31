package com.example.todolistapp.database

import androidx.room.*
import com.example.todolistapp.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM todo_tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM todo_tasks WHERE taskId = :taskId")
    fun getTaskById(taskId: Long): Flow<Task?>
}