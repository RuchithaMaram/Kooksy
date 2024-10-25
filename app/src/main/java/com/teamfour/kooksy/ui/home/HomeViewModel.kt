package com.teamfour.kooksy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamfour.kooksy.ui.home.data.RecipeData

/** This class manages the list of recipes
 * It uses LIVEDATA class so that fragment can observe changes
 * LIVEDATA - Automatically updates the UI when data changes
 **/
class HomeViewModel : ViewModel() {

    //Live data to store the list of recipes
    private val _recipesList = MutableLiveData<List<RecipeData>>()
    val recipesList: LiveData<List<RecipeData>> get() = _recipesList

    init{
        loadRecipes()
    }

    private fun loadRecipes(){
        var list = arrayListOf(
            RecipeData(1,"Chicken Biryani","pasta","20mins",4.5f),
            RecipeData(2,"Chicken Biryani","pasta","20mins",4.5f)
        )
        _recipesList.value = list
    }
}