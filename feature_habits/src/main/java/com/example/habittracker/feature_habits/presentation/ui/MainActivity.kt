package com.example.habittracker.feature_habits.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.habittracker.core_api.R.id
import com.example.habittracker.core_api.R.menu
import com.example.habittracker.feature_habits.R
import com.example.habittracker.feature_habits.databinding.ActivityMainBinding
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponent
import com.example.habittracker.feature_habits.di.components.FeatureHabitsComponentProvider
import com.example.habittracker.feature_habits.presentation.view_models.MainViewModel
import com.example.habittracker.ui_kit.R.string
import com.example.habittracker.ui_kit.presentation.HasTitle
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var currentFragment: Fragment? = null

    val featureHabitsComponent: FeatureHabitsComponent by lazy {
        (application as FeatureHabitsComponentProvider).featureHabitsComponent
    }

    @Inject
    lateinit var viewModel: MainViewModel

    private val fragmentListener =
        object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fragmentManager: FragmentManager,
                fragment: Fragment,
                view: View,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentViewCreated(fragmentManager, fragment, view, savedInstanceState)
                if (fragment is NavHostFragment) return
                currentFragment = fragment
                updateTitle()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
        setUpFeatureHabitsComponent()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpNavigation()
        setUpViewModel()
    }

    private fun setUpFeatureHabitsComponent() {
        featureHabitsComponent.inject(this)
    }

    private fun setUpViewModel() {
        viewModel.cloudError.observe(this) {
            it.transferIfNotHandled()?.let { error ->
                makeLongToast(error)
            }
        }

        viewModel.showResultToast.observe(this) { it ->
            it.transferIfNotHandled()?.let { error ->
                makeLongToast(error)
            }
        }

        viewModel.showSnackbarHabitDone.observe(this) {
            it.transferIfNotHandled()?.let { data ->
                Snackbar.make(binding.drawerLayout, data.snackbarText, Snackbar.LENGTH_LONG)
                    .setAction(getString(string.undo)) {
                        viewModel.deleteHabitDone(data.habitDone.id)
                    }
                    .addCallback(viewModel.snackbarCallback(data.habitDone))
                    .show()
            }
        }

        viewModel.ioError.observe(this) {
            it.transferIfNotHandled()?.let { error ->
                makeLongToast(error)
            }
        }

        viewModel.showHabitsAreNotSyncDialogAlert.observe(this) {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setMessage(getString(string.cloud_and_db_are_not_equals))
                setCancelable(true)
                setPositiveButton(getString(string.download_from_the_server)) { _, _ ->
                    viewModel.downloadAllHabitsFromCloudToDb()
                }
                setNegativeButton(getString(string.upload_to_the_server)) { _, _ ->
                    viewModel.uploadAllHabitsFromDbToCloud()
                }
                setNeutralButton(getString(string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun makeLongToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setUpNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
    }

    private fun updateTitle() {
        val fragment = currentFragment
        if (fragment is HasTitle) binding.toolbar.title = getString(fragment.getTitleResId())
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    override fun onCreateOptionsMenu(newMenu: Menu): Boolean {
        menuInflater.inflate(menu.main_menu, newMenu)
        MenuCompat.setGroupDividerEnabled(newMenu, true)
        return super.onCreateOptionsMenu(newMenu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.menu_upload -> viewModel.uploadAllHabitsFromDbToCloud()
            id.menu_download -> viewModel.downloadAllHabitsFromCloudToDb()
            id.menu_clear_db -> viewModel.deleteAllHabitsFromDb()
            id.menu_clear_cloud -> viewModel.deleteAllHabitsFromCloud()
            id.menu_compare -> viewModel.compareCloudAndDb()
        }
        return super.onOptionsItemSelected(item)
    }
}