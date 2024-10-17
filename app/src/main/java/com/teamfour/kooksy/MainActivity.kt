package com.teamfour.kooksy

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.teamfour.kooksy.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // Add all the created fragments to below list home, fav, create and profile
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorite,
                R.id.navigation_create,
                R.id.navigation_profile
            )
        )
        navView.setupWithNavController(navController)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController.addOnDestinationChangedListener { _, _, _ ->
            supportActionBar?.title = navController.currentDestination?.label
            when (navController.currentDestination?.id) {
                R.id.navigation_home,
                R.id.navigation_profile,
                R.id.navigation_create,
                R.id.navigation_favorite -> {
                    navView.visibility = View.VISIBLE
                }

                else -> {
                    navView.visibility = View.GONE
                }
            }
        }

        val callback = onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
