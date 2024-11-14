package com.teamfour.kooksy.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamfour.kooksy.R

class ProcedureAdapter(private val recipeProcedure: List<String>) :
    RecyclerView.Adapter<ProcedureAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_procedure, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = recipeProcedure.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = recipeProcedure[position]
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
    }


}
