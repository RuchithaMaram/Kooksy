package com.teamfour.kooksy

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.teamfour.kooksy.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(navView, navController)

        setSupportActionBar(binding.toolbar)

        val backIcon = ResourcesCompat.getDrawable(resources,R.drawable.back_icon,null)
        binding.toolbar.navigationIcon = backIcon
    }

    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        supportActionBar?.title = navController.currentDestination?.label
        when (navController.currentDestination?.id) {
            R.id.navigation_home,
            R.id.navigation_profile,
            R.id.navigation_favorite -> {
                binding.toolbar.visibility = View.GONE
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                binding.navView.visibility = View.VISIBLE
            }

            else -> {
                binding.toolbar.visibility = View.VISIBLE
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                binding.navView.visibility = View.GONE
            }
        }
    }
}
