package com.example.habittracker.feature_authorization.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.habittracker.feature_authorization.R
import com.example.habittracker.feature_authorization.databinding.FragmentAuthorizationBinding
import com.example.habittracker.ui_kit.R.*
import com.example.habittracker.ui_kit.presentation.HasTitle


class AuthorizationFragment : Fragment(), HasTitle {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding: FragmentAuthorizationBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthorizationBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authorization, container, false)
    }

    companion object {
    }

    override fun getTitleResId(): Int = string.authorization_fragment_title
}