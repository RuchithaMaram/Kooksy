package com.teamfour.kooksy.ui.home

import android.animation.Animator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamfour.kooksy.databinding.FragmentHomeBinding
import com.teamfour.kooksy.R
import com.teamfour.kooksy.ui.profile.Recipe
import com.teamfour.kooksy.ui.profile.UserDetails

//Observes the Recipes list from HomeViewModel and updates UI whenever the data changes
//It basically reacts when data changes (reaction -> Rendering UI)
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var homeAdapter: HomeAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Set the home fragment layout
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Initialize the ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //Initialize and set up the Recycler View
        homeRecyclerView = binding.homeRecyclerView
        homeRecyclerView.layoutManager = LinearLayoutManager(context)
        homeAdapter = HomeAdapter(arrayListOf())
        homeRecyclerView.adapter = homeAdapter

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("KooksyPrefs", Context.MODE_PRIVATE)

        // Fetch username and set it in the welcome message
        fetchAndSetUsername()

        // Set up search functionality
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchText: String?): Boolean {
                searchText?.let { homeViewModel.searchRecipes(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    homeViewModel.loadRecipesFromFirebase() // Load all recipes if search is cleared
                }
                return true
            }
        })

        // Check the offline mode setting and adjust the FAB visibility
        val isOffline = sharedPreferences.getBoolean("offlineMode", false)
        toggleOfflineMode(isOffline)

        // Observe the LiveData from the ViewModel
        //viewLifecycleOwner -> Ensures that the observer is only active while the current view is in the Lifecycle state.

        /** In simple words, It uses observe fun to listen for changes in recipesList
         *  Live data (in HomeModelView) , when a change occurs it updates **/

        homeViewModel.recipesList.observe(viewLifecycleOwner, Observer { recipes ->
            recipes?.let {
                if (recipes.isNotEmpty()) {
                    homeAdapter.updateData(ArrayList(recipes)) // Update adapter's data with fetched recipes
                } else {
                    Toast.makeText(context, "No recipes found", Toast.LENGTH_SHORT).show()
                }
            }
        })

        homeAdapter.onItemClick = {recipe ->
            val action = HomeFragmentDirections.actionNavigationHomeToRecipeFragment(recipe)
            findNavController().navigate(action)
        }

        binding.fabCreateRecipe.setOnClickListener {
            if (!isOffline) {
                showLottieEmojiAnimation()
                findNavController().navigate(R.id.navigation_create)
            }
        }

        binding.filterIcon.setOnClickListener {
            FilterOverlayFragment(
                { difficulty, withoutMeat, rating ->
                    applyFilters(difficulty, withoutMeat, rating)
                },
                {
                    clearFilters() // Clear filters when the "Clear Filter" button is clicked
                }
            ).show(childFragmentManager, "FilterOverlay")
        }

        return root
    }

    private fun fetchAndSetUsername() {
        val userId = UserDetails.user?.user_id
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("HomeFragment", "Document snapshot: ${document.data}")
                        val username = document.getString("user_name") ?: "User"
                        binding.userNametxtView.text = "Welcome $username!"
                        Log.d("HomeFragment", "Fetched username: $username")
                    } else {
                        Log.e("HomeFragment", "Document does not exist for user ID: $userId")
                        binding.userNametxtView.text = "Welcome User!"
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("HomeFragment", "Failed to fetch username", exception)
                    binding.userNametxtView.text = "Welcome User!"
                }
        } else {
            Log.w("HomeFragment", "No authenticated user found.")
            binding.userNametxtView.text = "Welcome User!"
        }
    }


    private fun applyFilters(difficulty: String, withoutMeat: Boolean, rating: Int) {
        homeViewModel.recipesList.value?.let { recipes ->
            val filteredRecipes = recipes.filter { recipe ->
                // Check if the recipe matches the difficulty level
                val matchesDifficulty = difficulty == "All" || recipe.recipe_difficultyLevel.equals(difficulty, ignoreCase = true)

                // Check if the recipe matches the meat preference
                val matchesMeatPreference = !withoutMeat || !containsMeat(recipe)

                // Check if the recipe matches the rating
                // Adjust the rating logic to include recipes with 0.0 if necessary
                val matchesRating = recipe.averageRating >= rating || recipe.averageRating == 0.0

                // Log the conditions for debugging
                Log.d("FilterDebug", "Recipe: ${recipe.recipe_name}, Difficulty: ${recipe.recipe_difficultyLevel}, Contains Meat: ${containsMeat(recipe)}, Rating: ${recipe.averageRating}")
                Log.d("FilterDebug", "matchesDifficulty: $matchesDifficulty, matchesMeatPreference: $matchesMeatPreference, matchesRating: $matchesRating")

                // Return true if all conditions are met
                matchesDifficulty && matchesMeatPreference && matchesRating
            }

            // Update the adapter with the filtered recipes
            homeAdapter.updateData(ArrayList(filteredRecipes))

            // Log the number of filtered recipes
            Log.d("FilterDebug", "Filtered Recipes Count: ${filteredRecipes.size}")
        }
    }


    private fun clearFilters() {
        // Reset all filters and load all recipes
        homeViewModel.loadRecipesFromFirebase()
    }

    private fun containsMeat(recipe: Recipe): Boolean {
        val meatIngredients = listOf("chicken", "beef", "pork", "fish", "egg")
        return recipe.recipe_ingredients.any { ingredient ->
            meatIngredients.any { meat -> ingredient["ingredient_name"]?.contains(meat, ignoreCase = true) == true }
        }
    }

    private fun toggleOfflineMode(isOffline: Boolean) {
        if (isOffline) {
            binding.fabCreateRecipe.visibility = View.GONE
            binding.offlineMessage.visibility = View.VISIBLE
        } else {
            binding.fabCreateRecipe.visibility = View.VISIBLE
            binding.offlineMessage.visibility = View.GONE
        }
    }

    private fun showLottieEmojiAnimation() {
        // Show the Lottie animation view
        val lottieEmojiAnimation = binding.lottieEmojiAnimation
        lottieEmojiAnimation.visibility = View.VISIBLE

        // Log when the animation view becomes visible
        Log.d("HomeFragment", "Lottie Emoji Animation is now visible and will start playing")

        // Play the animation
        lottieEmojiAnimation.playAnimation()
        Log.d("HomeFragment", "Lottie Emoji Animation has started")

        // Set a listener to hide the animation and navigate once it completes
        lottieEmojiAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.d("HomeFragment", "Lottie Emoji Animation has started playing")
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.d("HomeFragment", "Lottie Emoji Animation has ended")
                // Hide the animation view after the animation ends
                lottieEmojiAnimation.visibility = View.GONE
                Log.d("HomeFragment", "Lottie Emoji Animation is now hidden")
                // Navigate to the Create Recipe page
                findNavController().navigate(R.id.navigation_create)
                Log.d("HomeFragment", "Navigating to the Create Recipe page")
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.d("HomeFragment", "Lottie Emoji Animation was canceled")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.d("HomeFragment", "Lottie Emoji Animation is repeating")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}