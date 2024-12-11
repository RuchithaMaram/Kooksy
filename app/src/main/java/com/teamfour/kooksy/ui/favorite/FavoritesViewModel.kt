package com.teamfour.kooksy.ui.favorite

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.teamfour.kooksy.ui.profile.Recipe
import com.teamfour.kooksy.utils.Utils
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _menuItems = MutableLiveData<List<Recipe>>()
    val menuItems: LiveData<List<Recipe>> = _menuItems

    private val _isFavouritevalueUpdated = MutableLiveData<Boolean>()
    val isFavouritevalueUpdated: LiveData<Boolean> = _isFavouritevalueUpdated

    private val _isRatingvalueUpdated = MutableLiveData<Boolean>() // Add this property
    val isRatingvalueUpdated: LiveData<Boolean> = _isRatingvalueUpdated

    private val sharedPreferences = application.getSharedPreferences("FavoritesPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()


    // Firestore instance
    private val db = FirebaseFirestore.getInstance()

    companion object {
        private const val TAG = "FavoritesViewModel"
    }

    fun getMenuItems() {
        viewModelScope.launch {
            val offlineFavorites = getOfflineFavorites()
            if (offlineFavorites.isNotEmpty()) {
                Log.d(TAG, "Offline favorites loaded: ${offlineFavorites.size} items.")
                _menuItems.value = offlineFavorites
            } else {
                fetchFavoritesFromFirebase()
            }
        }
    }

    private fun fetchFavoritesFromFirebase() {
        db.collection("RECIPE").get().addOnSuccessListener { result ->
            val itemsList = result.mapNotNull { document ->
                Utils.parseResponseToRecipe(document)
            }
            val favList = itemsList.filter { it.is_favourite }
            Log.d(TAG, "Favorites fetched from Firebase: ${favList.size} items.")
            _menuItems.value = favList
            saveOfflineFavorites(favList) // Cache favorites for offline use
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Failed to fetch favorites from Firebase.", exception)
        }
    }

    fun updateFavoriteStatus(isFavorite: Boolean, recipeItem: Recipe) {
        viewModelScope.launch {
            recipeItem.is_favourite = isFavorite
            val favorites = getOfflineFavorites().toMutableList()
            if (isFavorite) {
                favorites.add(recipeItem)
                Log.d(TAG, "Added to offline favorites: ${recipeItem.recipe_name}")
            } else {
                favorites.removeAll { it.documentId == recipeItem.documentId }
                Log.d(TAG, "Removed from offline favorites: ${recipeItem.recipe_name}")
            }
            saveOfflineFavorites(favorites)

            // Update LiveData to notify observers
            _isFavouritevalueUpdated.value = isFavorite
        }
    }

    fun submitRating(isRated: Boolean, ratingValue: Int, recipeItem: Recipe) {
        viewModelScope.launch {
            val totalRating = recipeItem.totalRating + ratingValue
            val ratingCount = recipeItem.ratingCount + 1
            val averageRating = totalRating / ratingCount
            recipeItem.is_rated = isRated
            recipeItem.totalRating = totalRating
            recipeItem.ratingCount = ratingCount

            val favorites = getOfflineFavorites().toMutableList()
            val updatedRecipeIndex = favorites.indexOfFirst { it.documentId == recipeItem.documentId }
            if (updatedRecipeIndex >= 0) {
                favorites[updatedRecipeIndex] = recipeItem
                saveOfflineFavorites(favorites)
            }

            // Simulate updating Firestore (if required)
            db.collection("RECIPE").document(recipeItem.documentId).update(
                mapOf(
                    "is_rated" to isRated,
                    "totalRating" to totalRating,
                    "ratingCount" to ratingCount,
                    "averageRating" to averageRating
                )
            ).addOnSuccessListener {
                Log.d(TAG, "Rating successfully updated for recipe: ${recipeItem.recipe_name}")
                _isRatingvalueUpdated.value = true
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Failed to update rating for recipe: ${recipeItem.recipe_name}", exception)
                _isRatingvalueUpdated.value = false
            }
        }
    }

    private fun getOfflineFavorites(): List<Recipe> {
        val json = sharedPreferences.getString("favorites", null) ?: return emptyList()
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveOfflineFavorites(favorites: List<Recipe>) {
        val json = gson.toJson(favorites)
        sharedPreferences.edit().putString("favorites", json).apply()
        Log.d(TAG, "Offline favorites saved: ${favorites.size} items.")
    }
}
