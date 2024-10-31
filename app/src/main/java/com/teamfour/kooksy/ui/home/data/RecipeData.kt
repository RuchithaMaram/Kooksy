package com.teamfour.kooksy.ui.home.data

import com.google.firebase.firestore.PropertyName

data class RecipeData(
    /** Tells Firebase what field name to look for in the database
     * when mapping to this property **/
    val recipeId: String = "",
    @PropertyName("recipe_name") val recipeName: String?,
    @PropertyName("recipe_imageURL") val recipeImage: String?,
    @PropertyName("recipe_cookTime") val recipeCookTime: Int?,
    @PropertyName("recipe_rating") val recipeRating: Double?
)
