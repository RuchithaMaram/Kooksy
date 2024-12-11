package com.teamfour.kooksy.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.RecipeItemBinding
import com.teamfour.kooksy.ui.home.HomeAdapter.RecipeViewHolder

//[RecipesAdapter] binds and displays [Recipe] objects in a RecyclerView using ListAdapter and DiffUtil for efficient updates.

class RecipesAdapter(private val onRecipeClick: (Recipe) -> Unit) :
    ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(RecipeDiffCallback()) {

        var onItemClick: ((Recipe) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
//        val binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecipeViewHolder(binding)
        val homeView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
       return RecipeViewHolder(homeView)
    }

    class RecipeViewHolder(homeView: View): RecyclerView.ViewHolder(homeView){
        val recipeImage: ImageView = homeView.findViewById(R.id.recipeImage)
        val recipeName: TextView = homeView.findViewById(R.id.recipeName)
        val recipeCookTime: TextView = homeView.findViewById(R.id.recipeCookTime)
        val recipeRating: TextView = homeView.findViewById(R.id.recipeRating)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        if (!recipe.recipe_imageURL.isNullOrEmpty()) {
            Picasso.get()
                .load(recipe.recipe_imageURL)
                .placeholder(R.drawable.recipe_error) // Placeholder image
                .error(R.drawable.recipe_error) // Error image
                .into(holder.recipeImage)
        } else {
            // Set a placeholder image if URL is empty
            holder.recipeImage.setImageResource(R.drawable.recipe_error)
        }
        holder.recipeName.text = recipe.recipe_name
        holder.recipeCookTime.text = recipe.recipe_cookTime.toString() + " mins"
//        holder.recipeRating.text = recipe.averageRating.toString()
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe)
        }
    }
//    inner class RecipeViewHolder(private val binding: RecipeItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//            //Binds the recipe data to the UI elements in layout.
//        fun bind(recipe: Recipe) {
//            binding.recipeName.text = recipe.recipe_name
//            binding.recipeCookTime.text = "${recipe.recipe_cookTime} min"
//           // binding.recipeSubmissionTime.text = "Submitted on: ${recipe.createdOn}"
//
//            binding.root.setOnClickListener {
//                onRecipeClick(recipe)  // Passing Recipe object
//            }
//        }
//    }
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
