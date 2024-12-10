package com.teamfour.kooksy.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamfour.kooksy.databinding.FragmentMyRecipesBinding

class MyRecipe : Fragment() {

    private var _binding: FragmentMyRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MyRecipeViewModel
    private lateinit var adapter: RecipesAdapter

    companion object {
        private const val TAG = "MyRecipesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[MyRecipeViewModel::class.java]

        // Set up RecyclerView Adapter
        adapter = RecipesAdapter { recipe ->
            Log.d(TAG, "Recipe clicked: ${recipe.recipe_name}")
            val action = MyRecipeDirections.actionMyRecipesFragmentToRecipeDetailFragment(recipe)
            findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Observe the recipes LiveData
        viewModel.recipes.observe(viewLifecycleOwner) { recipeList ->
            if (recipeList.isNullOrEmpty()) {
                Log.d(TAG, "No recipes found to display.")
            } else {
                Log.d(TAG, "Loaded ${recipeList.size} recipes.")
                recipeList.forEach { Log.d(TAG, "Recipe: ${it.recipe_name}") }
            }
            adapter.submitList(recipeList.distinctBy { it.documentId }) // Ensure no duplicates
        }

        // Explicitly fetch recipes when the fragment is viewed
        viewModel.fetchRecipesFromFirebase()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
