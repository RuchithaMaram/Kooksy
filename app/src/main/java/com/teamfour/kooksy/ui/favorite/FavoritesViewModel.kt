package com.teamfour.kooksy.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfour.kooksy.MyApp
import com.teamfour.kooksy.ui.profile.Recipe
import com.teamfour.kooksy.ui.profile.UserDetails
import com.teamfour.kooksy.utils.Utils
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Favorites Fragment"
    }
    val text: LiveData<String> = _text

    private val _menuItems = MutableLiveData<List<Recipe>>()
    val menuItems: LiveData<List<Recipe>> = _menuItems

    private val _isFavouritevalueUpdated = MutableLiveData<Boolean>()
    val isFavouritevalueUpdated: LiveData<Boolean> = _isFavouritevalueUpdated

    private val _isRatingvalueUpdated = MutableLiveData<Boolean>()
    val isRatingvalueUpdated: LiveData<Boolean> = _isRatingvalueUpdated

    fun getMenuItems() {
        viewModelScope.launch {
            MyApp.db.collection("RECIPE").get().addOnSuccessListener { result ->
                val itemsList = mutableListOf<Recipe>()
                result.mapNotNull { document ->
                    itemsList.add(Utils.parseResponseToRecipe(document))
                }
                val favList = itemsList.filter { it.is_favourite }
                _menuItems.value = favList
            }.addOnFailureListener { exception ->
                println(exception.toString())
            }
        }
    }

    fun updateFavoriteStatus(isFavorite: Boolean, recipeItem: Recipe) {
        viewModelScope.launch {
            val reference = MyApp.db.collection("RECIPE").document(recipeItem.documentId)
            reference.update("is_favourite", isFavorite)
                .addOnSuccessListener {
                    _isFavouritevalueUpdated.value = isFavorite
                }
                .addOnFailureListener {

                }
        }
    }

    fun submitRating(isRated: Boolean, avgRating: Int, recipeItem: Recipe) {
        viewModelScope.launch {
            val reference = MyApp.db.collection("RECIPE").document(recipeItem.documentId)

            val totalRating = avgRating + recipeItem.totalRating
            val ratingCount = recipeItem.ratingCount + 1
            val averageRating = totalRating / ratingCount
            val ratedBy = recipeItem.ratedBy.toMutableList()
            val userId = UserDetails.user?.user_id
            val updates = hashMapOf(
                "is_rated" to isRated,
                "totalRating" to totalRating,
                "ratingCount" to ratingCount,
                "averageRating" to averageRating,
                "ratedBy" to ratedBy.apply { add(userId.toString()) }
            )

            reference.update(updates)
                .addOnSuccessListener {
                    _isRatingvalueUpdated.value = isRated
                }
                .addOnFailureListener {

                }
        }
    }

}
