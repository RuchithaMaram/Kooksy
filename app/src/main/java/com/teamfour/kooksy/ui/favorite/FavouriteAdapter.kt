package com.teamfour.kooksy.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamfour.kooksy.R
import com.teamfour.kooksy.ui.favorite.data.MenuItem

class FavouriteAdapter(private val menuItems: List<MenuItem>, onClick: () -> Unit) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = menuItems.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val menuItem = menuItems.get(position)
        holder.name.text = menuItem.name
        holder.time.text = menuItem.time
        Picasso.get().load(menuItem.image).placeholder(R.drawable.ic_recipe_book)
            .into(holder.image)
        holder.favIcon.setImageResource(R.drawable.ic_fav_selected)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_image)
        val name: TextView = view.findViewById(R.id.item_name)
        val time: TextView = view.findViewById(R.id.time_tv)
        val favIcon: ImageView = view.findViewById(R.id.favourite_iv)
    }
}