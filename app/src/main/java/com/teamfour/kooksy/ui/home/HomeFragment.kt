package com.teamfour.kooksy.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamfour.kooksy.databinding.FragmentHomeBinding
import com.teamfour.kooksy.ui.home.data.RecipeData

//Observes the Recipes list from HomeViewModel and updates UI whenever the data changes
//It basically reacts when data changes (reaction -> Rendering UI)
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeRecyclerView: RecyclerView
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

      // Observe the LiveData from the ViewModel
      //viewLifecycleOwner -> Ensures that the observer is only active while the current view is in the Lifecycle state.

      /**In simple words, It uses observe fun to listen for changes in recipesList
       *  Live data (in HomeModelView) , when a change occurs it updates **/

      homeViewModel.recipesList.observe(viewLifecycleOwner, Observer { recipes ->
          recipes?.let {
              homeAdapter.updateData(it as ArrayList<RecipeData>) // Update the adapter's data
          }
      })

      homeAdapter.onItemClick = {recipe ->
          val explicitIntent = Intent(context,RecipeFragment::class.java)
          startActivity(explicitIntent)
      }
      return root
  }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}