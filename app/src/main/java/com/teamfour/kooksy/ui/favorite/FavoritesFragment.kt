package com.teamfour.kooksy.ui.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavouriteAdapter

    companion object {
        private const val TAG = "FavoritesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        favoritesViewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "FavoritesFragment: View created.")

        initObservers()
        (activity as MainActivity).showProgressBar()
        val isOffline = requireContext().getSharedPreferences("KooksyPrefs", Context.MODE_PRIVATE)
            .getBoolean("offlineMode", false)

        if (isOffline) {
            Log.d(TAG, "Offline mode enabled. Loading offline favorites.")
        } else {
            Log.d(TAG, "Online mode enabled. Fetching favorites from Firebase.")
            favoritesViewModel.getMenuItems()
        }
    }

    private fun initObservers() {
        favoritesViewModel.menuItems.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                Log.d(TAG, "No favorites found.")
                binding.emptyState.visibility = View.VISIBLE
                binding.favouritesRv.visibility = View.GONE
            } else {
                Log.d(TAG, "Favorites loaded: ${list.size} items.")
                binding.emptyState.visibility = View.GONE
                binding.favouritesRv.visibility = View.VISIBLE
                adapter = FavouriteAdapter(list) { item ->
                    Log.d(TAG, "Navigating to recipe details for: ${item.recipe_name}")
                    val action =
                        FavoritesFragmentDirections.actionNavigationFavoriteToRecipeFragment(item)
                    findNavController().navigate(action)
                }
                binding.favouritesRv.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "FavoritesFragment: View destroyed.")
    }
}
