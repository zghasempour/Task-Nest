package com.example.todolist.fragments.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.data.models.TasksData
import com.example.todolist.data.viewmodel.TasksViewModel
import com.example.todolist.databinding.FragmentUpdateBinding
import com.example.todolist.fragments.SharedViewModel
import androidx.core.view.MenuProvider as MenuProvider

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel : SharedViewModel by viewModels()
    private val mTasksViewModel : TasksViewModel by viewModels()

    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

       binding.titleEt.setText(args.currentItem.title)
       binding.descriptionEt.setText(args.currentItem.description)
        binding.prioritiesSpinner.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }



            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.save -> {
                        updateItem()
                        true
                    }
                    R.id.delete -> {
                        confirmItemRemoval()
                        true
                    }
                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        return binding.root
    }
    private fun updateItem() {
        val title = binding.titleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val priority = binding.prioritiesSpinner.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title,description)

        if (validation){
            val updatedData = TasksData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(priority),
                description
            )
            mTasksViewModel.updateData(updatedData)
            Toast.makeText(requireContext(),"Data Successfully updated!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        }
        else{
            Toast.makeText(requireContext(),"Please fill data correctly!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmItemRemoval() {

        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes"){_,_ ->
            mTasksViewModel.deleteData(args.currentItem)
            Toast.makeText(requireContext(),
                "Successfully Removed : ${args.currentItem.title}",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete '${args.currentItem.title}'")
        builder.setMessage("Are you sure to remove '${args.currentItem.title}'?")
        builder.create().show()
    }

}