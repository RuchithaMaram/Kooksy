package com.teamfour.kooksy.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamfour.kooksy.R
import com.teamfour.kooksy.ui.home.data.RecipeData

// Extends RecyclerView Adapter class
class HomeAdapter(private val recipesList: List<RecipeData>, onClick: () -> Unit):RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    // In view holder we attach all our card view elements with variables
    class ViewHolder(homeView: View): RecyclerView.ViewHolder(homeView){
        val recipeImage: ImageView = homeView.findViewById(R.id.recipeImage)
        val recipeName: TextView = homeView.findViewById(R.id.recipeName)
        val recipeCookTime: TextView = homeView.findViewById(R.id.recipeCookTime)
        val recipeRating: TextView = homeView.findViewById(R.id.recipeRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Fetches the recipe card layout
        val homeView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(homeView)
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Attach our view variables with respective data class variables
        // We can see our view with actual values
        val recipe = recipesList[position]
        holder.recipeImage.setImageResource(R.drawable.pasta)
        holder.recipeName.text = recipe.recipeName
        holder.recipeCookTime.text = recipe.recipeCookTime
        holder.recipeRating.text = recipe.recipeRating.toString()
    }
}

