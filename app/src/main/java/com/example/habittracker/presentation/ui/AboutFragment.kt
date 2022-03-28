package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.BuildConfig
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentAboutBinding


class AboutFragment : Fragment(), HasTitle {

    private var _binding: FragmentAboutBinding? = null
    private val binding: FragmentAboutBinding
        get() = _binding ?: throw RuntimeException("FragmentAboutBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvVersionName.text = getString(R.string.version_name, BuildConfig.VERSION_NAME)
            tvVersionCode.text = getString(R.string.version_code, BuildConfig.VERSION_CODE)
            btnOk.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun getTitleResId(): Int = R.string.about
}