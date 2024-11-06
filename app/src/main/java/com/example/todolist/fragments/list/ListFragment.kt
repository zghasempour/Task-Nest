package com.example.todolist.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.data.viewmodel.TasksViewModel
import com.example.todolist.databinding.FragmentListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton



class ListFragment : Fragment() {

    private val mTasksViewModel: TasksViewModel by viewModels()
    private val adapter : ListAdapter by lazy { ListAdapter() }

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater,container, false)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        mTasksViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->
            adapter.setData(data)
        })

        // Add MenuProvider to the hosting activity
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate your menu items here
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle menu item selection
                return when (menuItem.itemId) {
                    R.id.menu_search -> {
                        // Handle the menu item action
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingBT.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }



    }


}