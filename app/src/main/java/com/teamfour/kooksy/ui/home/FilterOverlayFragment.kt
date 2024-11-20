package com.teamfour.kooksy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.teamfour.kooksy.R
import com.teamfour.kooksy.databinding.FragmentFilterOverlayBinding

class FilterOverlayFragment(
    private val applyFilters: (String, Boolean, Int) -> Unit,
    private val clearFilters: () -> Unit // Add this function for clearing filters
) : DialogFragment() {

    private var _binding: FragmentFilterOverlayBinding? = null
    private val binding get() = _binding!!

    private var selectedDishType = "Both"
    private var selectedDifficulty = "All"
    private var selectedRating = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterOverlayBinding.inflate(inflater, container, false)
        val view = binding.root

        // Apply the slide-in animation
        val slideInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        view.startAnimation(slideInAnimation)


        // Set up the dish type filter
        binding.dishTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedDishType = when (checkedId) {
                R.id.radio_without_meat -> "Without Meat"
                R.id.radio_with_meat -> "Meat"
                else -> "Both"
            }
        }

        // Set up the difficulty level filter
        binding.difficultyRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedDifficulty = when (checkedId) {
                R.id.radio_easy -> "Easy"
                R.id.radio_medium -> "Medium"
                R.id.radio_hard -> "Hard"
                else -> "All"
            }
        }

        // Set up the rating slider
        binding.ratingSeekBar.max = 4 // Adjust for a range of 1 to 5
        binding.ratingSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedRating = progress + 1 // Adjust to make the range 1 to 5
                binding.ratingValue.text = selectedRating.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Apply Filter Button Listener
        binding.applyFilterButton.setOnClickListener {
            applyFilters(selectedDifficulty, selectedDishType == "Without Meat", selectedRating)
            dismiss()
        }

        // Clear Filter Button Listener
        binding.clearFilterButton.setOnClickListener {
            clearFilters() // Call the function to clear filters
            dismiss()
        }

        // Close the overlay when clicking the close icon
        binding.closeFilter.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setDimAmount(0.5f)
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
