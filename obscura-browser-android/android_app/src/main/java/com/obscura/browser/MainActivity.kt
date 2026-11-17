package com.obscura.browser

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkException
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Material You themed content view
        setContentView(R.layout.activity_main)

        // Initialize the obscura browser engine
        ObscuraNative.initEngine()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        
        // Set up app bar with toolbar
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_tabs,
                R.id.nav_bookmarks,
                R.id.nav_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up bottom navigation with tab switching
        bottomNav.setupWithNavController(navController)

        // Set up tab item click listeners for custom tab switching
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.action_main_fragment_to_home_fragment)
                    true
                }
                R.id.nav_tabs -> {
                    navController.navigate(R.id.action_main_fragment_to_tab_fragment)
                    true
                }
                R.id.nav_bookmarks -> {
                    navController.navigate(R.id.action_main_fragment_to_bookmarks_fragment)
                    true
                }
                R.id.nav_settings -> {
                    navController.navigate(R.id.action_main_fragment_to_settings_fragment)
                    true
                }
                else -> false
            }
        }

        // Handle toolbar back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up the obscura engine
        ObscuraNative.cleanupEngine()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new_tab -> {
                // Handle new tab action
                Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.action_new_tab,
                    Snackbar.LENGTH_SHORT
                ).show()
                return true
            }
            R.id.action_settings -> {
                // Handle settings
                Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.action_settings,
                    Snackbar.LENGTH_SHORT
                ).show()
                return true
            }
            R.id.action_share -> {
                // Handle share
                Snackbar.make(
                    findViewById(android.R.id.content),
                    R.string.action_share,
                    Snackbar.LENGTH_SHORT
                ).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}