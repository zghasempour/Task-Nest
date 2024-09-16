package com.example.todolist.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.models.Priority
import com.example.todolist.data.models.TasksData
import com.example.todolist.data.viewmodel.TasksViewModel
import com.example.todolist.databinding.FragmentAddBinding
import com.example.todolist.fragments.SharedViewModel

class AddFragment : Fragment() {

    private var _binding :FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mTasksViewModel: TasksViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddBinding.inflate(inflater,container,false)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate your menu items here
                menuInflater.inflate(R.menu.add_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle menu item selection
                return when (menuItem.itemId) {
                    R.id.add -> {
                        // Handle the menu item action
                        insertDataToDb()
                        true
                    }
                    else -> false
                }
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        return binding.root
    }

    private fun insertDataToDb() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()


        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if (validation){
        val newData = TasksData(
            0,
            mTitle,
            mSharedViewModel.parsePriority(mPriority),
            mDescription
        )

            mTasksViewModel.insertData(newData)

            Toast.makeText(requireContext(),"Successfully added", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(),"Please fill out fields", Toast.LENGTH_LONG).show()
        }
    }

}