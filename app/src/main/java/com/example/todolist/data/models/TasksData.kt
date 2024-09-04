package com.example.todolist.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class TasksData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title:String,
    var priority: Priority,
    var description : String
)
