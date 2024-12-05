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

        adapter = RecipesAdapter { recipe ->
            Log.d(TAG, "Recipe clicked: ${recipe.recipe_name}")
            val action = MyRecipeDirections.actionMyRecipesFragmentToRecipeDetailFragment(recipe)
            findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter

        // Recipes from the ViewModel
        viewModel.recipes.observe(viewLifecycleOwner) { recipeList ->
            adapter.submitList(recipeList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
