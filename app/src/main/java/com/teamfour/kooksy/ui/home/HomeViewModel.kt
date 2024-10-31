package com.teamfour.kooksy.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.teamfour.kooksy.ui.profile.Recipe

/** This class manages the list of recipes
 * It uses LIVEDATA class so that fragment can observe changes
 * LIVEDATA - Automatically updates the UI when data changes
 **/
class HomeViewModel : ViewModel() {

    //Live data to store the list of recipes
    private val _recipesList = MutableLiveData<List<Recipe>>()
    val recipesList: LiveData<List<Recipe>> get() = _recipesList
    private val database = FirebaseFirestore.getInstance()

    init{
        loadRecipesFromFirebase()
    }

    fun loadRecipesFromFirebase() {
        database.collection("RECIPE")
            .get()
            .addOnSuccessListener { result ->
                Log.i("Recipes", "Number of documents retrieved: ${result.size()}")
                var recipeList = mutableListOf<Recipe>()
                if (result.isEmpty) {
                    Log.i("Recipes", "No documents found.")
                } else {
                    for (document in result) {
                        // Loop through each document and convert it to Recipe(Data Class)
                        document.toObject(Recipe::class.java).let { recipeList.add(it) }
                    }
                    //Update live data with fetched recipes
                    _recipesList.value = recipeList
                }
    }
            .addOnFailureListener { exception ->
                exception.printStackTrace();
                Log.e("HomeViewModel", "Error fetching recipes: ", exception)
            }
    }

    fun  searchRecipes(searchText: String){
        database.collection("RECIPE")
            .whereGreaterThanOrEqualTo("recipe_name",searchText)
            .whereLessThanOrEqualTo("recipe_name",searchText+ "\uf8ff")
            .get()
            .addOnSuccessListener { searchResults ->
                var searchList = mutableListOf<Recipe>()
                Log.i("Search Text", "Search Text : ${searchResults.size()}")
                for(document in searchResults){
                    document.toObject(Recipe::class.java).let { searchList.add(it) }
                }
                //Update live data with the search list
                _recipesList.value = searchList
            }
            .addOnFailureListener{exception ->
                exception.printStackTrace()
            }
    }
}