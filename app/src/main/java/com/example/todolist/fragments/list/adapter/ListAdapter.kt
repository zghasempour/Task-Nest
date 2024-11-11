package com.example.todolist.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.models.TasksData
import com.example.todolist.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
     var tasksList = emptyList<TasksData>()

    class MyViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tasksData: TasksData) {
            binding.tasksData = tasksData
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RowLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }

    fun setData(tasksData: List<TasksData>){
        val tasksDiffUtil = TasksDiffUtil(tasksList, tasksData)
        val tasksDiffResult = DiffUtil.calculateDiff(tasksDiffUtil)
        this.tasksList = tasksData
        tasksDiffResult.dispatchUpdatesTo(this)
    }
}