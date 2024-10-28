package com.teamfour.kooksy.ui.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teamfour.kooksy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private var ingredientCount = 1 // Start the count with 1 for the default ingredient
    private var dynamicIngredientCount = 2 // Track number of dynamically added ingredients
    private val db = FirebaseFirestore.getInstance() // Firestore instance

    private var stepCount = 1 // Track the number of steps, starting with 1
    private val TAG = "CreateFragment" // TAG for logging

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.d(TAG, "View created") // Log to indicate the view is created
        // Handle Submit Recipe Button Click
        binding.submitRecipeButton.setOnClickListener {
            try {
                submitRecipe()
            } catch (e: Exception) {
                Log.e(TAG, "Error during recipe submission", e)
            }
        }
        // Ingredient 1 and Quantity 1 are defined in the XML layout.
        ingredientCount = 1 // Initialize with Ingredient 1 already set up in the XML.

        // Handle Add an Image Button CLick
        binding.addImageButton.setOnClickListener {
            Toast.makeText(requireContext(), "Add Image button clicked!", Toast.LENGTH_SHORT).show() // TODO
        }

        // Handle Add Ingredient Button Click
        binding.addIngredientButton.setOnClickListener {
            addNewIngredient() // Dynamically add new ingredients
        }

        // Handle Add Step Button Click
        binding.addStepButton.setOnClickListener {
            addNewStep() // Dynamically add new steps
        }

        return root
    }

    // Function to dynamically add new ingredients (with a delete button)
    private fun addNewIngredient() {
        val currentIngredientNumber = dynamicIngredientCount++ // Increment for each new ingredient
        Log.d(TAG, "Adding new Ingredient $currentIngredientNumber")// Log the addition of a new ingredient

        // Create a new vertical LinearLayout to hold Ingredient and Quantity below each other
        val ingredientLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        // Create an EditText for the ingredient name
        val ingredientName = EditText(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            hint = "Ingredient $currentIngredientNumber"
            textSize = 16f
            setPadding(12, 12, 12, 12)
            background = requireContext().getDrawable(R.drawable.rounded_edittext)
        }

        // Create an EditText for the quantity
        val ingredientQuantity = EditText(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            hint = "Quantity $currentIngredientNumber"
            textSize = 16f
            setPadding(12, 12, 12, 12)
            background = requireContext().getDrawable(R.drawable.rounded_edittext)
        }

        // Create a delete button (trash icon) for the ingredient
        val deleteButton = ImageButton(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.ic_delete_black_12dp) // Reusing your delete icon
            contentDescription = "Delete Ingredient"
            setPadding(8, 8, 8, 8)

            // Set an OnClickListener to delete the ingredient
            setOnClickListener {
                binding.ingredientsContainer.removeView(ingredientLayout) // Remove this ingredient row
                dynamicIngredientCount-- // Decrement the dynamic ingredient count
                adjustIngredientLabels() // Adjust the labels of the remaining ingredients
                Toast.makeText(requireContext(), "Ingredient removed", Toast.LENGTH_SHORT).show()
            }
        }

        // Add Ingredient and Quantity EditText fields to the layout
        ingredientLayout.addView(ingredientName)
        ingredientLayout.addView(ingredientQuantity)

        // Add the delete button after Ingredient and Quantity
        ingredientLayout.addView(deleteButton)

        // Add the entire ingredient layout to the ingredients_container
        binding.ingredientsContainer.addView(ingredientLayout)

        // Show a message indicating a new ingredient is added
        Toast.makeText(requireContext(), "Ingredient $currentIngredientNumber added", Toast.LENGTH_SHORT).show()
    }

    // Function to adjust the labels of the ingredients after deletion
    private fun adjustIngredientLabels() {
        var currentIngredientNumber = 2 // Start from Ingredient 2 since Ingredient 1 is fixed in the layout
        // Iterate through all views in the ingredients_container to update their labels
        for (i in 0 until binding.ingredientsContainer.childCount) {
            val ingredientLayout = binding.ingredientsContainer.getChildAt(i) as LinearLayout
            val ingredientEditText = ingredientLayout.getChildAt(0) as EditText
            val quantityEditText = ingredientLayout.getChildAt(1) as EditText
            ingredientEditText.hint = "Ingredient $currentIngredientNumber"
            quantityEditText.hint = "Quantity $currentIngredientNumber"
            currentIngredientNumber++
        }
    }

    // Function to dynamically add new steps (with a delete button)
    private fun addNewStep() {
        val currentStepNumber = stepCount++ // Increment for each new step
        Log.d(TAG, "Adding new Step $currentStepNumber")

        // Create a new vertical LinearLayout to hold Step EditText and a delete button
        val stepLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        // Create an EditText for the step
        val stepInput = EditText(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            hint = "Step $currentStepNumber" // Hint with current step number
            textSize = 16f
            setPadding(12, 12, 12, 12)
            minLines = 3
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
            background = requireContext().getDrawable(R.drawable.rounded_edittext)
        }

        // Create a delete button (trash icon) for the step
        val deleteButton = ImageButton(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.ic_delete_black_12dp) // Your delete icon resource
            contentDescription = "Delete Step $currentStepNumber"
            setPadding(8, 8, 8, 8)

            // Set an OnClickListener to delete the step
            setOnClickListener {
                binding.stepsContainer.removeView(stepLayout) // Remove this step from the container
                stepCount-- // Decrement the step count
                adjustStepLabels() // Adjust labels for remaining steps
                Toast.makeText(requireContext(), "Step $currentStepNumber removed", Toast.LENGTH_SHORT).show()
            }
        }

        // Add Step EditText and delete button to the layout
        stepLayout.addView(stepInput)
        stepLayout.addView(deleteButton)

        // Add the entire step layout to the stepsContainer
        binding.stepsContainer.addView(stepLayout)

        // Show a message indicating the new step is added
        Toast.makeText(requireContext(), "Step $currentStepNumber added", Toast.LENGTH_SHORT).show()
    }

    // Function to adjust the labels of the steps after deletion
    private fun adjustStepLabels() {
        var currentStepNumber = 2 // Start at Step 2 since Step 1 is fixed
        // Iterate through all views in the steps_container to update their labels
        for (i in 1 until binding.stepsContainer.childCount) { // Start from index 1 (Step 2 onwards)
            val stepLayout = binding.stepsContainer.getChildAt(i) as LinearLayout
            val stepEditText = stepLayout.getChildAt(0) as EditText
            stepEditText.hint = "Step $currentStepNumber"
            currentStepNumber++
        }
    }

    // Function to submit recipe to Firestore
    private fun submitRecipe() {
        Log.d(TAG, "Submitting recipe") // Log to indicate recipe submission started
        // Collect data from input fields
        val recipeName = binding.txtRecipeName.text.toString()
        val recipecal = binding.caloriesInput.text.toString().toIntOrNull() ?: 0
        val cookTime = binding.cookTimeInput.text.toString().toIntOrNull() ?: 0
        val difficulty = binding.difficultySpinner.selectedItem.toString()
        val ingredients = getIngredientsList()
        val steps = getStepsList()

        if (recipeName.isNotEmpty() && ingredients.isNotEmpty() && steps.isNotEmpty()) {
            val recipeData = hashMapOf(
                "recipe_name" to recipeName,
                "recipe_imageURL" to "https://example.com/recipe_image.jpg", // Placeholder image URL
                "recipe_calories" to recipecal,
                "recipe_cookTime" to cookTime,
                "recipe_difficultyLevel" to difficulty,
                "recipe_ingredients" to ingredients,
                "recipe_instructions" to steps,
                "recipe_rating" to 0.0, // Rating to be added by other users later
                "createdBy" to null, // Placeholder for user_id, for later authentication
                "createdOn" to com.google.firebase.Timestamp.now() // Auto-generated timestamp
            )

            // Submit to Firestore
            Log.d(TAG, "Submitting recipe: $recipeName")
            db.collection("RECIPE").add(recipeData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Recipe submitted with ID: ${documentReference.id}") // Log success
                    Toast.makeText(requireContext(), "Recipe submitted!", Toast.LENGTH_SHORT).show()

                    // Reset the form fields after submission
                    resetFormFields()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error submitting recipe", e) // Log failure with exception details
                    Toast.makeText(requireContext(), "Failed to submit recipe: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        } else {
            Log.w(TAG, "Submission failed: Missing required fields") // Log a warning if fields are missing
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to get the ingredients list from the form
    private fun getIngredientsList(): List<Map<String, String>> {
        val ingredients = mutableListOf<Map<String, String>>()

        // Loop through dynamically added ingredients
        for (i in 0 until binding.ingredientsContainer.childCount) {
            val ingredientLayout = binding.ingredientsContainer.getChildAt(i) as ViewGroup
            val ingredientName = (ingredientLayout.getChildAt(0) as EditText).text.toString()
            val ingredientQuantity = (ingredientLayout.getChildAt(1) as EditText).text.toString()

            if (ingredientName.isNotEmpty() && ingredientQuantity.isNotEmpty()) {
                val ingredientMap = mapOf(
                    "ingredient_name" to ingredientName,
                    "ingredient_quantity" to ingredientQuantity
                )
                ingredients.add(ingredientMap)
            }
        }

        // Add the first ingredient (hardcoded in the layout)
        val firstIngredient = binding.ingredient1.text.toString()
        val firstQuantity = binding.quantity1.text.toString()
        if (firstIngredient.isNotEmpty() && firstQuantity.isNotEmpty()) {
            ingredients.add(mapOf(
                "ingredient_name" to firstIngredient,
                "ingredient_quantity" to firstQuantity
            ))
        }
        Log.d(TAG, "Ingredients list collected: $ingredients") // Log collected ingredients
        return ingredients
    }

    // Function to get the steps list from the form
    private fun getStepsList(): List<String> {
        val steps = mutableListOf<String>()

        // Add the first step (hardcoded in the layout)
        val firstStep = binding.step1Input.text.toString()
        if (firstStep.isNotEmpty()) {
            steps.add("Step 1: $firstStep") // Add with a label for Step 1
        }

        // Loop through dynamically added steps starting from index 1
        var stepIndex = 2 // Start from Step 2
        for (i in 0 until binding.stepsContainer.childCount) {
            val stepLayout = binding.stepsContainer.getChildAt(i) as? LinearLayout // Ensure it's a layout with step EditText inside
            if (stepLayout != null && stepLayout.childCount > 0) {
                val stepText = (stepLayout.getChildAt(0) as? EditText)?.text.toString() // Get step EditText content
                if (stepText.isNotEmpty()) {
                    steps.add("Step $stepIndex: $stepText") // Label each step dynamically
                    stepIndex++ // Increment step index
                }
            }
        }
        Log.d(TAG, "Steps list collected: $steps") // Log collected steps
        return steps
    }

    // Function to reset the form fields after submission
    private fun resetFormFields() {
        binding.txtRecipeName.text?.clear()
        binding.caloriesInput.text?.clear()
        binding.cookTimeInput.text?.clear()

        // Reset spinner to first item (Easy)
        binding.difficultySpinner.setSelection(0)

        // Remove dynamically added ingredients and steps
        binding.ingredientsContainer.removeAllViews()
        binding.stepsContainer.removeAllViews()

        // Reset hardcoded first ingredient and step
        binding.ingredient1.text?.clear()
        binding.quantity1.text?.clear()
        // Reset the first step input (Step 1) directly
        binding.step1Input.text?.clear() // Clear the input for Step 1

        // Reset stepCount and add Step 1 back
        stepCount = 1
        addNewStep() // Re-add Step 1

        Toast.makeText(requireContext(), "Form reset!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
