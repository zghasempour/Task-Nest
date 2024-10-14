package com.example.todolist.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.TasksDao
import com.example.todolist.data.models.Priority
import com.example.todolist.data.models.TasksData
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var tasksList = emptyList<TasksData>()

    class MyViewHolder(itemView: RowLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val mBinding = itemView
        fun bind(tasksData: TasksData) {

            mBinding.titleTxt.text = tasksData.title.toString()
            mBinding.descriptionTxt.text = tasksData.description.toString()

            when (tasksData.priority) {
                Priority.HIGH -> mBinding.priorityIndicator.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.red
                    )
                )

                Priority.MEDIUM -> mBinding.priorityIndicator.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.yellow
                    )
                )

                Priority.LOW -> mBinding.priorityIndicator.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.green
                    )
                )
            }

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
        this.tasksList = tasksData
        notifyDataSetChanged()
    }
}