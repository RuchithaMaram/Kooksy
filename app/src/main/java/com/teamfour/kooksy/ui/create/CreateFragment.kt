package com.teamfour.kooksy.ui.create

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.teamfour.kooksy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.databinding.FragmentCreateBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateViewModel

    private lateinit var imageView: ImageView
    private lateinit var photoFile: File
    private val REQUEST_CAMERA = 1
    private val REQUEST_GALLERY = 2

    // Ingridents Counter
    private var ingredientCount = 1 // Start the count with 1 for the default ingredient
    private var dynamicIngredientCount = 2 // Track number of dynamically added ingredients

    // Step Counter
    private var stepCount = 1 // Track the number of steps, starting with 1
    private var dynamicStepCount = 2 // Track number of dynamically added steps

    private val TAG = "CreateFragment" // TAG for logging

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(CreateViewModel::class.java)

        // Handle the Back Button click
        binding.backButton.setOnClickListener {
            // Navigate back to the Home page
            findNavController().navigate(R.id.navigation_home)
        }

        Log.d(TAG, "View created") // Log to indicate the view is created
        // Handle Submit Recipe Button Click
        binding.submitRecipeButton.setOnClickListener {
            try {
                submitRecipe()
            } catch (e: Exception) {
                Log.e(TAG, "Error during recipe submission", e)
            }
        }

        // Initialize ingredientCount and stepCount before setting click listeners
        ingredientCount = 1
        stepCount = 1

        // Handle Add Ingredient Button Click
        binding.addIngredientButton.setOnClickListener {
            addNewIngredient() // Dynamically add new ingredients
        }

        // Handle Add Step Button Click
        binding.addStepButton.setOnClickListener {
            addNewStep() // Dynamically add new steps
        }
// Initialize ImageView and Add Image Button
        imageView = binding.imageView
        binding.addImageButton.setOnClickListener {
            showImageSourceDialog()
        }



        return binding.root
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image Source")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            try {
                photoFile = createImageFile()
                val photoURI: Uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, REQUEST_CAMERA)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(null)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    { super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    Glide.with(this).load(photoFile).into(imageView)
                    imageView.visibility = View.VISIBLE
                }
                REQUEST_GALLERY -> {
                    val selectedImageUri: Uri? = data?.data
                    Glide.with(this).load(selectedImageUri).into(imageView)
                    imageView.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageRef = storage.reference.child("recipe_images/${System.currentTimeMillis()}.jpg")
        val uploadTask = storageRef.putFile(imageUri)

      /*  uploadTask.addOnSuccessListener { taskSnapshot ->
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                saveImageToFirestore(uri.toString())
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to get download URL", e)
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Log.e(TAG, "Image upload failed", e)
            Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
        } */

        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                saveImageToFirestore(uri.toString())
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to fetch download URL: ${e.message}")
            }
        }


    }


    private fun saveImageToFirestore(imageUrl: String) {
        val firestore = FirebaseFirestore.getInstance()
       // val recipeData = mapOf("recipeimage" to imageUrl)

       /* firestore.collection("RECIPE").add(recipeData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Image saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save image to Firestore", e)
                Toast.makeText(requireContext(), "Failed to save image URL", Toast.LENGTH_SHORT).show()
            } */

        val recipeData = mapOf("recipeimage" to imageUrl)
        firestore.collection("RECIPE").add(recipeData)
            .addOnSuccessListener { Log.d(TAG, "Image URL saved successfully!") }
            .addOnFailureListener { e -> Log.e(TAG, "Failed to save image: ${e.message}") }


    }



    // Function to submit recipe to Firestore
    private fun submitRecipe() {
        // Collect data from input fields
        val recipeName = binding.txtRecipeName.text.toString()
        val recipecal = binding.caloriesInput.text.toString().toIntOrNull() ?: 0
        val cookTime = binding.cookTimeInput.text.toString().toIntOrNull() ?: 0
        val difficulty = binding.difficultySpinner.selectedItem.toString()
        val ingredients = getIngredientsList()
        val steps = getStepsList()

        viewModel.submitRecipe(
            recipeName, recipecal, cookTime, difficulty, ingredients, steps,
            onSuccess = {
                showSuccessAnimation()
                lifecycleScope.launch {
                    delay(3000)  // Delay for 3 seconds
                    resetFormFields()
                    hideSuccessAnimation()
                }
            },
            onFailure = { e ->
                Toast.makeText(requireContext(), "Failed to submit recipe: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showSuccessAnimation() {
        binding.lottieAnimationView.apply {
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    private fun hideSuccessAnimation() {
        binding.lottieAnimationView.visibility = View.GONE
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

    // Function to dynamically add new steps (with a delete button)
    private fun addNewStep() {
        val currentStepNumber = dynamicStepCount++ // Increment for each new step, starting from 2
        Log.d(TAG, "Adding new Step $currentStepNumber") // Log the addition of a new step

        // Create a new vertical LinearLayout to hold Step EditText and a delete button
        val stepLayout = LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
        }

        // Create an EditText for the dynamic step
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
                dynamicStepCount-- // Decrement the dynamic step count
                adjustStepLabels() // Adjust labels for remaining steps
                Toast.makeText(requireContext(), "Step removed", Toast.LENGTH_SHORT).show()
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
        var currentStepNumber = 1 // Start with Step 1
        // Iterate through all views in the steps_container to update their labels
        for (i in 0 until binding.stepsContainer.childCount) {
            val stepLayout = binding.stepsContainer.getChildAt(i) as? LinearLayout // Ensure it's a LinearLayout
            if (stepLayout != null && stepLayout.childCount > 0) {
                val stepEditText = stepLayout.getChildAt(0) as? EditText // Get the first child as EditText
                if (stepEditText != null) {
                    stepEditText.hint = "Step $currentStepNumber" // Update hint with the correct step number
                    currentStepNumber++ // Increment the step number
                }
            }
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
        val firstStep = binding.step1Input.text.toString().trim()
        if (firstStep.isNotEmpty()) {
            steps.add("Step 1: $firstStep") // Add with a label for Step 1
        }

        // Loop through dynamically added steps starting from index 2
        var stepIndex = 2
        for (i in 0 until binding.stepsContainer.childCount) {
            val stepLayout = binding.stepsContainer.getChildAt(i) as? LinearLayout ?: continue // Continue if stepLayout is null
            val stepEditText = stepLayout.getChildAt(0) as? EditText ?: continue // Continue if stepEditText is null

            val stepText = stepEditText.text.toString().trim() // Trim to remove any extra spaces
            if (stepText.isNotEmpty()) {
                steps.add("Step $stepIndex: $stepText") // Add the step text with the index
                stepIndex++ // Increment step index
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

        private fun resetForm() {
            imageView.setImageDrawable(null) // Clears the image
            imageView.visibility = View.GONE // Hides the ImageView
        }


        // Reset spinner to first item (Easy)
        binding.difficultySpinner.setSelection(0)

        // Remove dynamically added ingredients and steps
        binding.ingredientsContainer.removeAllViews()
        binding.stepsContainer.removeViews(1, binding.stepsContainer.childCount - 1) // Keep Step 1, remove all others


        // Reset hardcoded first ingredient and step
        binding.ingredient1.text?.clear()
        binding.quantity1.text?.clear()
        // Reset the first step input (Step 1) directly
        binding.step1Input.text?.clear() // Clear the input for Step 1

        // Reset the dynamic ingredient and step counters
        dynamicIngredientCount = 2  // Reset for newly added ingredients
        dynamicStepCount = 2        // Reset for newly added steps

        Toast.makeText(requireContext(), "Form reset!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Log.d(TAG, "View destroyed") Log to indicate the view is destroyed
        _binding = null
    }
}
