package com.example.todolist.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.models.TasksData
import com.example.todolist.data.viewmodel.TasksViewModel
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.fragments.SharedViewModel
import com.example.todolist.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import com.example.todolist.fragments.list.SwipeToDelete as SwipeToDelete


class ListFragment : Fragment(){

    private val mTasksViewModel: TasksViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private val adapter : ListAdapter by lazy { ListAdapter() }

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater,container, false)

        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        // Set up recyclerview
        setUpRecyclerView()

        //Observe LiveDate
        mTasksViewModel.getAllData.observe(viewLifecycleOwner, Observer { data->
            mSharedViewModel.isDatabaseEmpty(data)
            adapter.setData(data)
        })



        // Add MenuProvider to the hosting activity
        requireActivity().addMenuProvider(object : MenuProvider, SearchView.OnQueryTextListener {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate your menu items here
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val searchView = menu.findItem(R.id.menu_search).actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle menu item selection
                return when (menuItem.itemId) {

                    R.id.delete_all -> {
                        confirmDelete()
                        true
                    }
                    else -> false
                }
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!= null)
                {
                    searchThroughDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query!= null)
                {
                    searchThroughDatabase(query)
                }
                return true
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        //List Animation
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        // Swipe To Delete

        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.tasksList[viewHolder.adapterPosition]
                // Delete Item
                mTasksViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore Item
                restoreDeletedData(viewHolder.itemView,deletedItem,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem:TasksData,position:Int){
        val snackBar = Snackbar.make(
           view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Undo"){
            mTasksViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes"){_,_ ->
            mTasksViewModel.deleteAllData()
            Toast.makeText(requireContext(),
                "Successfully Removed Everything!",
                Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure to remove everything?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchThroughDatabase(query: String) {

       val searchQuery = "%$query%"

        mTasksViewModel.searchData(searchQuery).observe(this, Observer { list->
            list?.let {
                adapter.setData(it)
            }
        })
    }
}