package com.viktorija.notesapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.viktorija.notesapp.data.database.Category
import com.viktorija.notesapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // drawerLayout variable
    private lateinit var appBarConfiguration: AppBarConfiguration

    // navigation controller
    private lateinit var navController: NavController

    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.Factory(requireNotNull(this).application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding variable
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // Setup navigation with drawer layout
        navController = this.findNavController(R.id.nav_host_fragment)

        // Setup appBarConfiguration to let application know
        // which menu fragments are top level fragments and connect that with drawer
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.notes_list_fragment),
            binding.drawerLayout
        )

        // Allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

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

        // Open Important Notes category
        navView.menu.findItem(R.id.important_fragment).setOnMenuItemClickListener {
            navController.navigate(NavigationDirections.openImportantNotes())
            drawerLayout.closeDrawers()

            true
        }

        initDrawerCategoriesList()
    }

    // Initializing dynamic drawer categories
    private fun initDrawerCategoriesList() {
        // observe list of categories in the database and refresh drawer
        viewModel.categories.observe(this, Observer {
            it?.let {
                // recreating the context of the drawer menu
                val groupId = navView.menu.findItem(R.id.notes_list_fragment).groupId

                val categoryMenu = navView.menu.findItem(R.id.categories_menu).subMenu
                categoryMenu.clear()

                // for each category create menu item in drawer
                it.forEach { category ->
                    val categoryMenuItem =
                        categoryMenu.add(
                            groupId,
                            category.id.toInt(),
                            Menu.NONE,
                            "${category.title.capitalize()} (${category.notesCount})"
                        );
                    categoryMenuItem.setIcon(R.drawable.ic_radio_button)
                    categoryMenuItem.setOnMenuItemClickListener {
                        // Open selected category
                        navController.navigate(NavigationDirections.openCategoryNotes(categoryId = category.id))
                        drawerLayout.closeDrawers()

                        true
                    }
                }
            }
        })
    }

    // Dialog to add new category
    fun showAddNewCategoryDialog(menuItem: MenuItem) {
        val categoryEditText = EditText(this)
        categoryEditText.layoutParams = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add a new category")
            .setView(categoryEditText)
            .setPositiveButton(
                "Add"
            ) { _, _ ->
                val categoryName = categoryEditText.text.toString().trim()

                viewModel.saveCategory(Category(categoryName))
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        // after dialog is displayed initially disable Add button so that user can not enter empty result
        val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        addButton.isEnabled = false

        // enable it only when text value is not empty
        categoryEditText.addTextChangedListener {
            it?.let {
                addButton.isEnabled = it.trim().isNotEmpty()
            }
        }
    }

    // Up navigation
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
