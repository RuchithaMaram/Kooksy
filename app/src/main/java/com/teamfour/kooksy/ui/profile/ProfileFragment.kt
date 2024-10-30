package com.teamfour.kooksy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.FragmentProfileBinding
import com.uvs.myapplication.ui.profile.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
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
