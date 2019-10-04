package com.viktorija.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.viktorija.notesapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // drawerLayout variable
    private lateinit var appBarConfiguration: AppBarConfiguration

    // navigation contoller
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        // Binding variable
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // Setup navigation with drawer layout
        navController = this.findNavController(R.id.nav_host_fragment)

        // Setup appBarConfiguration to let application know
        // which menu fragments are top level fragments and connect that with drawer
        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.mainFragment, R.id.importantFragment),
            binding.drawerLayout)

        // This allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration)

        // Use NavigationUI to set up a Navigation View
        // By calling this method, the title in the Toolbar will automatically be updated
        // when the destination changes (assuming there is a valid label
        NavigationUI.setupWithNavController(binding.navView, navController)

        // Prevent the drawer from being swiped anywhere other than the top level destinations
        navController.addOnDestinationChangedListener { _: NavController, nd: NavDestination, _: Bundle? ->
            if (appBarConfiguration.topLevelDestinations.contains(nd.id)) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

    }

    // Up navigation
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
