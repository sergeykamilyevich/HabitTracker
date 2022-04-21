package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.habittracker.R
import com.example.habittracker.app.applicationComponent
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.di.components.MainActivityComponent
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.presentation.view_models.HabitListViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var currentFragment: Fragment? = null

    lateinit var mainActivityComponent: MainActivityComponent

    @Inject
    lateinit var viewModel: HabitListViewModel

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
        setupMainActivityComponent()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupNavigation()
        setupViewModel()
    }

    private fun setupMainActivityComponent() {
        mainActivityComponent = applicationComponent
            .mainActivityComponentFactory()
            .create(this)
        mainActivityComponent.inject(this)
    }

    private fun setupViewModel() {
        viewModel.showToastHabitDone.observe(this) {
            it.transferIfNotHandled()?.let { result ->
                val habitType = result.habitItem.type
                val habitDone = result.habitItem.done
                val habitRecurrenceNumber = result.habitItem.recurrenceNumber
                val differenceDone = abs(habitDone - habitRecurrenceNumber)
                val differenceDoneTimes = resources.getQuantityString(
                    R.plurals.plurals_more_times,
                    differenceDone,
                    differenceDone
                )
                val snackbarText = when (habitType) {
                    HabitType.GOOD -> {
                        if (habitDone < habitRecurrenceNumber)
                            getString(R.string.worth_doing_more_times, differenceDoneTimes)
                        else getString(R.string.you_are_breathtaking)
                    }
                    HabitType.BAD -> {
                        if (habitDone < habitRecurrenceNumber)
                            getString(R.string.you_are_allowed_more_times, differenceDoneTimes)
                        else getString(R.string.stop_doing_it)

                    }
                }
                val snackbarCallback = SnackbarCallback(
                    viewModel = viewModel,
                    habitDone = result.habitDone
                ) //TODO fix to Inject
                Snackbar.make(binding.drawerLayout, snackbarText, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        viewModel.deleteHabitDone(result.habitDone.id)
                    }
                    .addCallback(snackbarCallback)
                    .show()
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
    }

    private fun updateTitle() {
        val fragment = currentFragment
        if (fragment is HasTitle) {
            binding.toolbar.title = getString(fragment.getTitleResId())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }
}