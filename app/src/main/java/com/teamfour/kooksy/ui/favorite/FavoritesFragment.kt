package com.teamfour.kooksy.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.delay

class FavoritesFragment : Fragment() {
    private lateinit var favoritesViewModel: FavoritesViewModel
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavouriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        favoritesViewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        (activity as MainActivity).showProgressBar()
        favoritesViewModel.getMenuItems()
    }

    private fun initObservers() {
        favoritesViewModel.menuItems.observe(viewLifecycleOwner) { list ->
          //  (activity as MainActivity).hideProgressBar(3000)
            adapter = FavouriteAdapter(list) {

            }
            binding.favouritesRv.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
