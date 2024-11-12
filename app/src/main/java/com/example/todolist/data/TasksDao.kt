package com.example.todolist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.data.models.TasksData


@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<TasksData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(tasksData: TasksData)

    @Update
    suspend fun updateData(tasksData: TasksData)

    @Delete
    suspend fun deleteData(tasksData: TasksData)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllData()

    @Query("SELECT * FROM tasks_table WHERE title LIKE :searchQuery" )
    fun searchDatabase(searchQuery: String):LiveData<List<TasksData>>

    @Query("SELECT * FROM tasks_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<TasksData>>

    @Query("SELECT * FROM tasks_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<TasksData>>
}