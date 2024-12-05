package com.teamfour.kooksy.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamfour.kooksy.databinding.RecipeItemBinding

//[RecipesAdapter] binds and displays [Recipe] objects in a RecyclerView using ListAdapter and DiffUtil for efficient updates.

class RecipesAdapter(private val onRecipeClick: (Recipe) -> Unit) :
    ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(private val binding: RecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            //Binds the recipe data to the UI elements in layout.
        fun bind(recipe: Recipe) {
            binding.recipeName.text = recipe.recipe_name
            binding.recipeCookTime.text = "${recipe.recipe_cookTime} min"
           // binding.recipeSubmissionTime.text = "Submitted on: ${recipe.createdOn}"

            binding.root.setOnClickListener {
                onRecipeClick(recipe)  // Passing Recipe object
            }
        }
    }
}

//The RecipeDiffCallback class compares old and new Recipe items in the list.
// It helps the adapter efficiently update only the items that have changed, rather than reloading the entire list.

class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.recipe_name == newItem.recipe_name
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}
