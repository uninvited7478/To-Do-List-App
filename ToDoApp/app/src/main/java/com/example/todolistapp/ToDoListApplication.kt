package com.example.todolistapp

import android.app.Application
import com.example.todolistapp.data.ToDoRepository
import com.example.todolistapp.database.ToDoDatabase

class ToDoListApplication : Application() {

    lateinit var toDoRepository: ToDoRepository
        private set

    override fun onCreate() {
        super.onCreate()
        toDoRepository = ToDoRepository(ToDoDatabase.getDatabase(this).dao())
    }
}