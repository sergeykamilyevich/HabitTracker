package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.habittracker.R
import com.example.habittracker.app.applicationComponent
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.di.components.MainActivityComponent
import com.example.habittracker.di.components.getComponent
import com.example.habittracker.presentation.view_models.MainViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var currentFragment: Fragment? = null

    lateinit var mainActivityComponent: MainActivityComponent

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
        setUpMainActivityComponent()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpNavigation()
        setUpViewModel()
        setUpHeaderImage()

    }

    private fun setUpHeaderImage() {
        val header = binding.navigationView.getHeaderView(DEFAULT_HEADER)
        val avatar = header.findViewById<ImageView>(R.id.avatar)
            ?: throw RuntimeException("Header image view is null")
        Glide.with(this)
            .load(IMAGE_URL)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.no_image_available)
            .circleCrop()
            .into(avatar)
    }

    private fun setUpMainActivityComponent() {
        mainActivityComponent = getComponent {
            applicationComponent
                .mainActivityComponentFactory()
                .create(this)
        }
        mainActivityComponent.inject(this)
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
                    .setAction(getString(R.string.undo)) {
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
                setMessage(getString(R.string.cloud_and_db_are_not_equals))
                setCancelable(true)
                setPositiveButton(getString(R.string.download_from_the_server)) { _, _ ->
                    viewModel.downloadAllHabitsFromCloudToDb()
                }
                setNegativeButton(getString(R.string.upload_to_the_server)) { _, _ ->
                    viewModel.uploadAllHabitsFromDbToCloud()
                }
                setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        MenuCompat.setGroupDividerEnabled(menu, true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_upload -> viewModel.uploadAllHabitsFromDbToCloud()
            R.id.menu_download -> viewModel.downloadAllHabitsFromCloudToDb()
            R.id.menu_clear_db -> viewModel.deleteAllHabitsFromDb()
            R.id.menu_clear_cloud -> viewModel.deleteAllHabitsFromCloud()
            R.id.menu_compare -> viewModel.compareCloudAndDb()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val IMAGE_URL =
            "https://img.freepik.com/free-photo/no-problem-concept-bearded-man-makes-okay-gesture-has-everything-control-all-fine-gesture-wears-spectacles-jumper-poses-against-pink-wall-says-i-got-this-guarantees-something_273609-42817.jpg"
        private const val DEFAULT_HEADER = 0

    }
}