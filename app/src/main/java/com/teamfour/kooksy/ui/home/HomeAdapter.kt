package com.teamfour.kooksy.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamfour.kooksy.R
import com.teamfour.kooksy.ui.profile.Recipe

// Extends RecyclerView Adapter class
//Notifies the RecyclerView(HomeFragment) whenever the data changes
class HomeAdapter(private var recipesList: MutableList<Recipe>):RecyclerView.Adapter<HomeAdapter.RecipeViewHolder>() {

    //Unit -> Return type - Similar to void(i.e returns nothing)
    var onItemClick: ((Recipe) -> Unit)? = null

    // In view holder we attach all our card view elements with variables
    class RecipeViewHolder(homeView: View): RecyclerView.ViewHolder(homeView){
        val recipeImage: ImageView = homeView.findViewById(R.id.recipeImage)
        val recipeName: TextView = homeView.findViewById(R.id.recipeName)
        val recipeCookTime: TextView = homeView.findViewById(R.id.recipeCookTime)
        val recipeRating: TextView = homeView.findViewById(R.id.recipeRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        // Fetches the recipe card layout
        val homeView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(homeView)
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        // Attach our view variables with respective data class variables
        // We can see our view with actual values
        val recipe = recipesList[position]
        holder.recipeImage.setImageResource(R.drawable.pasta)
        holder.recipeName.text = recipe.recipe_name
        holder.recipeCookTime.text = recipe.recipe_cookTime.toString() + " mins"
        holder.recipeRating.text = recipe.recipe_rating.toString()

        //Item view - Card View
        //Listens when user clicks on any of the recipe item
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe)
        }
    }

    fun updateData(newRecipes: ArrayList<Recipe>) {
        recipesList.clear() // Clear the old data
        recipesList.addAll(newRecipes) // Add the new data
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}

