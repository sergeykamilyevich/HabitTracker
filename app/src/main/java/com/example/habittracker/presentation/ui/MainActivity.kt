package com.example.habittracker.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchHabitListFragment()
    }

    private fun launchHabitListFragment() {
        val fragment = HabitListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(binding.mainContainer.id, fragment)
            .commit()
    }
}