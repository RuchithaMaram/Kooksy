package com.teamfour.kooksy.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import com.google.firebase.firestore.toObject
import com.teamfour.kooksy.MyApp
import com.teamfour.kooksy.ui.home.data.RecipeData
import com.teamfour.kooksy.ui.profile.Recipe
import com.teamfour.kooksy.utils.Utils

/** This class manages the list of recipes
 * It uses LIVEDATA class so that fragment can observe changes
 * LIVEDATA - Automatically updates the UI when data changes
 **/
class HomeViewModel : ViewModel() {

    //Live data to store the list of recipes
    private val _recipesList = MutableLiveData<List<Recipe>>()
    val recipesList: LiveData<List<Recipe>> get() = _recipesList

    init {
        loadRecipesFromFirebase()
    }

    fun loadRecipesFromFirebase() {
        MyApp.db.collection("RECIPE").get().addOnSuccessListener { result ->
            val itemsList = mutableListOf<Recipe>()
            result.mapNotNull { document ->
                itemsList.add(Utils.parseResponseToRecipe(document))
            }
            _recipesList.value = itemsList
        }.addOnFailureListener { exception ->
            exception.printStackTrace();
            Log.e("HomeViewModel", "Error fetching recipes: ", exception)
        }
    }

    fun searchRecipes(searchText: String) {
        MyApp.db.collection("RECIPE").whereGreaterThanOrEqualTo("recipe_name", searchText)
            .whereLessThanOrEqualTo("recipe_name", searchText + "\uf8ff").get()
            .addOnSuccessListener { searchResults ->
                val searchList = mutableListOf<Recipe>()
                searchResults.mapNotNull { document ->
                    searchList.add(Utils.parseResponseToRecipe(document))
                }
                _recipesList.value = searchList
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }
}