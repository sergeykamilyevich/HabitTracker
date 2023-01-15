package com.example.habittracker.feature_authorization.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.habittracker.feature_authorization.databinding.FragmentAuthorizationBinding
import com.example.habittracker.feature_authorization.di.components.FeatureAuthorizationComponentProvider
import com.example.habittracker.ui_kit.R.string
import com.example.habittracker.ui_kit.presentation.HasTitle
import com.example.habittracker.viewmodels.presentation.AuthorizationViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthorizationFragment : Fragment(), HasTitle {

    @Inject
    lateinit var viewModel: AuthorizationViewModel

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    private val viewModel: AuthorizationViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory)[AuthorizationViewModel::class.java]
//    }

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding: FragmentAuthorizationBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthorizationBinding is null")

    override fun onAttach(context: Context) {
        (requireActivity().application as FeatureAuthorizationComponentProvider)
            .featureAuthorizationComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSave.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.saveToken(tiedToken.text as? CharSequence ?: EMPTY_TOKEN as CharSequence)
                }

                findNavController().popBackStack()
            }
        }
    }

    companion object {

        private val EMPTY_TOKEN = ""
    }

    override fun getTitleResId(): Int = string.authorization_fragment_title
}