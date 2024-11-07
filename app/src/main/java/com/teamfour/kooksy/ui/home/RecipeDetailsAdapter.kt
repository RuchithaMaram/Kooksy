package com.teamfour.kooksy.ui.home

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamfour.kooksy.R

class RecipeDetailsAdapter(private val recipeItems: List<Map<String, String>>) :
    RecyclerView.Adapter<RecipeDetailsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = recipeItems.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = recipeItems.get(position)
        holder.name.text = menuItem.get("ingredient_name")
        holder.qty.text = menuItem.get("ingredient_quantity")
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val qty: TextView = view.findViewById(R.id.qty)
    }
}

class SpaceItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = spaceHeight
    }
}
