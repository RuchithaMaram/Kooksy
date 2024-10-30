package com.teamfour.kooksy.ui.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.FragmentMyRecipeBinding

class MyRecipe : Fragment() {

    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // You can initialize ViewModel-related logic here if needed
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting up back button
        binding.backButton.setOnClickListener {
            findNavController().popBackStack() // Navigate back when back button is clicked
        }

        // Observe data from the ViewModel and update UI if needed
        // viewModel.yourLiveData.observe(viewLifecycleOwner) { data ->
        //     // Update UI based on data
        // }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
