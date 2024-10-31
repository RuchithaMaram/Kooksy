package com.teamfour.kooksy.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teamfour.kooksy.databinding.FragmentProfileDetailsBinding


class FragmentProfileDetails : Fragment() {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.updateButton?.setOnClickListener {
            findNavController().popBackStack()
        }

       /* val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

        // Add MenuProvider for handling menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
              //  menuInflater.inflate(R.menu.my_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return navController.navigateUp()
            }
        }, viewLifecycleOwner)*/
    }


}