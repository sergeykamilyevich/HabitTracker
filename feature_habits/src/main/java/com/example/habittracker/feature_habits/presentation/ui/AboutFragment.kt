package com.example.habittracker.feature_habits.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.feature_habits.databinding.FragmentAboutBinding
import com.example.habittracker.ui_kit.R.string


class AboutFragment : Fragment(), HasTitle {

    private var _binding: FragmentAboutBinding? = null
    private val binding: FragmentAboutBinding
        get() = _binding ?: throw RuntimeException("FragmentAboutBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("99999", "AboutFragment $this")
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
//            tvVersionName.text = getString(string.version_name, BuildConfig.VERSION_NAME)
//            tvVersionCode.text = getString(string.version_code, BuildConfig.VERSION_CODE)
            btnOk.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun getTitleResId(): Int = string.about
}