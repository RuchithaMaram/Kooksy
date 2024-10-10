package com.teamfour.kooksy.ui.favorite.data

data class MenuItem(
    val id: Int,
    val image: String,
    val name: String,
    val time: String,
    val isFavourite: Boolean = false
)
