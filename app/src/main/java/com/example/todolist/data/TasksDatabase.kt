package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todolist.data.models.TasksData

@Database(entities = [TasksData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao() : TasksDao

    companion object{
        @Volatile
        private var INSTANCE : TasksDatabase? = null

        fun getDatabase(context: Context) : TasksDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TasksDatabase::class.java,
                    "tasks_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}