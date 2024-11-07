package com.teamfour.kooksy.utils

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.getField
import com.teamfour.kooksy.ui.profile.Recipe

class Utils {
    companion object {
        fun parseResponseToRecipe(document: QueryDocumentSnapshot): Recipe {
            return Recipe(
                document.getString("recipe_name") ?: "",
                document.getField<Int>("recipe_calories") ?: 0,
                document.getField<Int>("recipe_cookTime") ?: 0,
                document.getString("recipe_difficultyLevel") ?: "",
                document.getString("recipe_imageURL") ?: "",
                document.get("recipe_ingredients") as? List<Map<String, String>>
                    ?: emptyList(),
                document.get("recipe_instructions") as? List<String> ?: emptyList(),
                document.getTimestamp("createdOn"),
                document.getField<Float>("recipe_rating") ?: 0.0,
                document.getBoolean("is_favourite") ?: false,
                document.reference.id
            )
        }
    }
}