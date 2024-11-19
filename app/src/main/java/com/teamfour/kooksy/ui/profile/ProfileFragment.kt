package com.teamfour.kooksy.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("KooksyPrefs", Context.MODE_PRIVATE)

        // Set the initial state of the offline mode toggle
        val isOffline = sharedPreferences.getBoolean("offlineMode", false)
        binding.offlineModeToggle.isChecked = isOffline

        // Listen for changes in the toggle switch and save the state
        binding.offlineModeToggle.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("offlineMode", isChecked).apply()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.editProfileButton?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_fragmentProfileDetails)
        }

        // Navigate to My Recipes when clicking My Recipes Button or View
        binding.myRecipesButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_myRecipesFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
