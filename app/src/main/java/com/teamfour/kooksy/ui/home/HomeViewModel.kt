package com.teamfour.kooksy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamfour.kooksy.ui.favorite.data.MenuItem
import com.teamfour.kooksy.ui.home.data.RecipeData

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Home Fragment"
    }
    val text: LiveData<String> = _text

    private val _recipeItems = MutableLiveData<List<RecipeData>>()
    val recipeItems: LiveData<List<RecipeData>> = _recipeItems


    fun getRecipesList() {
        _recipeItems.value = getTempRecipesList()
    }

    private fun getTempRecipesList(): List<RecipeData> {
        val data1 = RecipeData(
            0,
            "White Sauce Pasta",
            "pasta.png",
            "25 mins",
            4.5f
        )
        val data2 = RecipeData(
            1,
            "Chicken Dum Biryani",
            "pasta.png",
            "20 mins",
            4.3f
        )

        return ArrayList<RecipeData>().apply {
            for (i in 0..10) {
                add(data1)
                add(data2)
            }
        } as List<RecipeData>
    }
}