package com.teamfour.kooksy.ui.create

import android.os.Bundle
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
import com.teamfour.kooksy.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private var ingredientCount = 1 // Start the count with 1 for the default ingredient
    private var dynamicIngredientCount = 2 // Track number of dynamically added ingredients

    private var stepCount = 1 // Track the number of steps, starting with 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Ingredient 1 and Quantity 1 are defined in the XML layout.
        ingredientCount = 1 // Initialize with Ingredient 1 already set up in the XML.

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
            setImageResource(R.drawable.ic_delete_black_12dp) // Reuse your delete icon
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

    // Function to dynamically add new steps (with a delete button and rounded background)
    private fun addNewStep() {
        stepCount++ // Increment the step count

        // Create a new vertical LinearLayout to hold both the EditText and the delete button
        val stepLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        // Create a new EditText for the next step with rounded background
        val newStep = EditText(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            hint = "Step $stepCount" // Set the hint for the new step
            textSize = 16f
            setPadding(12, 12, 12, 12)
            minLines = 3
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
            background = requireContext().getDrawable(R.drawable.rounded_edittext) // Apply rounded background
        }

        // Create a delete button (trash icon) for the step
        val deleteButton = ImageButton(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.ic_delete_black_12dp) // Reuse your delete icon
            contentDescription = "Delete Step"
            setPadding(8, 8, 8, 8)

            // Set an OnClickListener to delete the step
            setOnClickListener {
                binding.stepsContainer.removeView(stepLayout) // Remove this step from the container
                stepCount-- // Decrement the step count when the step is removed
                adjustStepLabels() // Adjust the labels of the remaining steps
                Toast.makeText(requireContext(), "Step removed", Toast.LENGTH_SHORT).show()
            }
        }

        // Add the new EditText and delete button to the layout
        stepLayout.addView(newStep)
        stepLayout.addView(deleteButton)

        // Add the step layout (containing EditText and delete button) to the steps_container
        binding.stepsContainer.addView(stepLayout)

        // Show a message indicating a new step is added
        Toast.makeText(requireContext(), "Step $stepCount added", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
