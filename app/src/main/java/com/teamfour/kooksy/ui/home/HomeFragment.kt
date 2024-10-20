package com.teamfour.kooksy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teamfour.kooksy.MainActivity
import com.teamfour.kooksy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: HomeAdapter

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root
    return root
  }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
//        (activity as MainActivity).showProgressBar()
        homeViewModel.getRecipesList()
    }

    private fun initObservers() {
        homeViewModel.recipeItems.observe(viewLifecycleOwner) { list ->
            //  (activity as MainActivity).hideProgressBar(3000)
            adapter = HomeAdapter(list) { }
            binding.homeRecyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}