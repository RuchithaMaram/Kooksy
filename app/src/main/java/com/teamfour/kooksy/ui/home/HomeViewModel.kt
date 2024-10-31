package com.teamfour.kooksy.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.teamfour.kooksy.ui.home.data.RecipeData
import com.teamfour.kooksy.ui.profile.Recipe

/** This class manages the list of recipes
 * It uses LIVEDATA class so that fragment can observe changes
 * LIVEDATA - Automatically updates the UI when data changes
 **/
class HomeViewModel : ViewModel() {

    //Live data to store the list of recipes
    private val _recipesList = MutableLiveData<List<RecipeData>>()
    val recipesList: LiveData<List<RecipeData>> get() = _recipesList
    private val database = FirebaseFirestore.getInstance()

    init{
        loadRecipesFromFirebase()
    }

    fun loadRecipesFromFirebase() {
        database.collection("RECIPE")
            .get()
            .addOnSuccessListener { result ->
                Log.i("Recipes", "Number of documents retrieved: ${result.size()}")
                var list = mutableListOf<RecipeData>()
                if (result.isEmpty) {
                    Log.i("Recipes", "No documents found.")
                } else {
                    for (document in result) {
                        // Loop through each document and convert it to RecipeData
                        val recipe = RecipeData(
                            document.id,
                            document.getString("recipe_name") ?: "",
                            document.getString("recipe_imageURL") ?: "",
                            document.getLong("recipe_cookTime")?.toInt() ?: 0,
                             (document.get("recipe_rating") as? Double) ?: 0.0
                        )
                        Log.i("recipe", "Recipe: $recipe")
                        list.add(recipe);
                    }

                    //Update live data with fetched recipes
                    _recipesList.value = list
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
                var searchList = mutableListOf<RecipeData>()
                Log.i("Search Text", "Search Text : ${searchResults.size()}")
                for(document in searchResults){
                    val searchedRecipe = RecipeData(
                        document.id,
                        document.getString("recipe_name") ?: "",
                        document.getString("recipe_imageURL") ?: "",
                        document.getLong("recipe_cookTime")?.toInt() ?: 0,
                        (document.get("recipe_rating") as? Double) ?: 0.0
                    )
                    searchList.add(searchedRecipe)
                }

                //Update live data with the search list
                _recipesList.value = searchList
            }
            .addOnFailureListener{exception ->
                exception.printStackTrace()
            }
    }
}