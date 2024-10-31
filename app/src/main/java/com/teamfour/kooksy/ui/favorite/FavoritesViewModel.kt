package com.teamfour.kooksy.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfour.kooksy.MyApp
import com.teamfour.kooksy.ui.profile.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Favorites Fragment"
    }
    val text: LiveData<String> = _text

    private val _menuItems = MutableLiveData<List<Recipe>>()
    val menuItems: LiveData<List<Recipe>> = _menuItems

    fun getMenuItems() {
        viewModelScope.launch {
            MyApp.db.collection("RECIPE").get().addOnSuccessListener { result ->
                val userList = mutableListOf<Recipe>()
                for (document in result) {
                    document.toObject(Recipe::class.java).let { userList.add(it) }
                }
              //  val favList = userList.filter { it.is_favourite == true }
                _menuItems.value = userList
            }.addOnFailureListener { exception ->
                println(exception.toString())
            }
        }
    }

}
