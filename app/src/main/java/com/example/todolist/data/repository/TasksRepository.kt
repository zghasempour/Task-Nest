package com.example.todolist.data.repository

import androidx.lifecycle.LiveData
import com.example.todolist.data.TasksDao
import com.example.todolist.data.models.TasksData

class TasksRepository(private val tasksDao: TasksDao) {

    val getAllData : LiveData<List<TasksData>> = tasksDao.getAllData()

    suspend fun insertData(tasksData: TasksData){
        tasksDao.insertData(tasksData)
    }
}