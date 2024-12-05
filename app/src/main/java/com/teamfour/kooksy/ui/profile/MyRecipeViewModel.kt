package com.teamfour.kooksy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.utils.Utils

class MyRecipeViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        fetchRecipesFromFirebase()
    }

    private fun fetchRecipesFromFirebase() {
       /* val userId = auth.currentUser?.uid
        if (userId == null) {
            _recipes.value = emptyList()
            return
        }*/

        db.collection("RECIPE")
            /*.whereEqualTo("createdBy", userId)*/
            .get()
            .addOnSuccessListener { result ->
                val recipeList = mutableListOf<Recipe>()
                for (document in result) {
                    result.mapNotNull { document ->
                        recipeList.add(Utils.parseResponseToRecipe(document))
                    }
                }
                _recipes.value = recipeList
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }
}
