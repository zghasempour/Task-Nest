package com.example.todolist.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tasks_table")
@Parcelize
data class TasksData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title:String,
    var priority: Priority,
    var description : String
): Parcelable
