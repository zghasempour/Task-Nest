package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todolist.data.models.TasksData


@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<TasksData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(tasksData: TasksData)
}