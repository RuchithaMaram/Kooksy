package com.teamfour.kooksy.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamfour.kooksy.ui.favorite.data.MenuItem

class FavoritesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Favorites Fragment"
    }
    val text: LiveData<String> = _text

    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> = _menuItems


    fun getMenuItems() {
        _menuItems.value = getTempMenuList()
    }

    private fun getTempMenuList(): List<MenuItem> {
        val item1 = MenuItem(
            0,
            "https://bigoven-res.cloudinary.com/image/upload/d_recipe-no-image.jpg/lowfat-vegetable-lasagna-1336994.jpg",
            "Vegetable Lasagna",
            "25 mins",
            false
        )
        val item2 = MenuItem(
            0,
            "https://bigoven-res.cloudinary.com/image/upload/f_auto,q_auto/sweetandsourstickythaiboneless-3a944d.jpg",
            "Sweet and Sour Thai",
            "20 mins",
            false
        )

        return ArrayList<MenuItem>().apply {
            for (i in 0..10) {
                add(item1)
                add(item2)
            }
        } as List<MenuItem>
    }
}
