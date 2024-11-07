package com.teamfour.kooksy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamfour.kooksy.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import com.teamfour.kooksy.R

//Observes the Recipes list from HomeViewModel and updates UI whenever the data changes
//It basically reacts when data changes (reaction -> Rendering UI)
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Set the home fragment layout
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Initialize the ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //Initialize and set up the Recycler View
        homeRecyclerView = binding.homeRecyclerView
        homeRecyclerView.layoutManager = LinearLayoutManager(context)
        homeAdapter = HomeAdapter(arrayListOf())
        homeRecyclerView.adapter = homeAdapter

        //Search text
        var searchView = binding.searchBar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchText: String?): Boolean {
                if (searchText != null) {
                    homeViewModel.searchRecipes(searchText)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    homeViewModel.loadRecipesFromFirebase() // Load all recipes if search is cleared
                }
                return true
            }
        })

        // Observe the LiveData from the ViewModel
        //viewLifecycleOwner -> Ensures that the observer is only active while the current view is in the Lifecycle state.

        /**In simple words, It uses observe fun to listen for changes in recipesList
         *  Live data (in HomeModelView) , when a change occurs it updates **/

        homeViewModel.recipesList.observe(viewLifecycleOwner, Observer { recipes ->
            recipes?.let {
                if (recipes.isNotEmpty()) {
                    homeAdapter.updateData(ArrayList(recipes)) // Update adapter's data with fetched recipes
                } else {
                    Toast.makeText(context, "No recipes found", Toast.LENGTH_SHORT).show()
                }
            }
        })

        homeAdapter.onItemClick = {recipe ->
            val action = HomeFragmentDirections.actionNavigationHomeToRecipeFragment(recipe)
            findNavController().navigate(action)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle click to navigate to Create Recipe screen
        binding.fabCreateRecipe.setOnClickListener {
            findNavController().navigate(R.id.navigation_create)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}