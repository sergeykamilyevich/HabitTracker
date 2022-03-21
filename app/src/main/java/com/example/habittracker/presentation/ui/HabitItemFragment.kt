package com.example.habittracker.presentation.ui

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitItemBinding
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitPriority
import com.example.habittracker.presentation.color.ColorPicker
import com.example.habittracker.presentation.mappers.ColorMapper
import com.example.habittracker.presentation.mappers.HabitItemMapper
import com.example.habittracker.presentation.view_models.HabitItemViewModel
import com.google.android.material.textfield.TextInputEditText

class HabitItemFragment : Fragment() {

    private var _binding: FragmentHabitItemBinding? = null
    private val binding: FragmentHabitItemBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitItemBinding is null")

    private lateinit var viewModel: HabitItemViewModel
    private val habitItemMapper = HabitItemMapper()
    private val colorMapper = ColorMapper()

    private val spinnerAdapter by lazy {
        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            HabitPriority.values()
        )
    }
    private val colorPicker = ColorPicker()
    private val colors = colorPicker.getColors()
    private val gradientColors = colorPicker.getGradientColors()

    private var screenMode: String? = null
    private var habitItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HabitItemViewModel::class.java]
        chooseScreenMode()
        setupSpinnerAdapter()
        setupViewModelObservers()
        setupTextChangeListeners()
        setupScrollView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun chooseScreenMode() {
        when (screenMode) {
            ADD_MODE -> launchAddMode()
            EDIT_MODE -> launchEditMode()
            else -> throw RuntimeException("Unknown activity mode: $screenMode")
        }
    }

    private fun setupScrollView() {
        for (color in colors) {
            val view = LayoutInflater.from(requireActivity()).inflate(
                R.layout.item_color,
                binding.llColor,
                false
            )
            view.setBackgroundColor(color)
            view.setOnClickListener {
                setupColorViews((it.background as ColorDrawable).color)
            }
            binding.llColor.addView(view)
        }
        val gradientDrawable =
            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors)
        binding.llColor.background = gradientDrawable
    }

    private fun setupTextChangeListeners() {
        binding.tiedName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.validateName(text)
            }

        })

        binding.tiedRecurrenceNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.validateRecurrenceNumber(text)
            }

        })

        binding.tiedRecurrencePeriod.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.validateRecurrencePeriod(text)
            }
        })
    }

    private fun setupViewModelObservers() {
        viewModel.canCloseScreen.observe(viewLifecycleOwner) {
            requireActivity().onBackPressed()
        }
        viewModel.errorInputRecurrenceNumber.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedRecurrenceNumber)
        }
        viewModel.errorInputRecurrencePeriod.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedRecurrencePeriod)
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedName)
        }
    }

    private fun launchAddMode() {
        val defaultColor = colors[0]
        binding.currentColor.setBackgroundColor(defaultColor)
        setupColorViews(defaultColor)
        binding.btnSave.setOnClickListener {
            if (isFieldsFilled()) {
                viewModel.addHabitItem(habitItemMapper.mapViewToHabitItem(binding))
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.not_all_required_fields_are_filled),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun launchEditMode() {
        viewModel.getHabitItem(habitItemId
            ?: throw RuntimeException("habitItemId is null"))
        viewModel.habitItem.observe(viewLifecycleOwner) {
            setupFields(it)
        }
        binding.btnSave.setOnClickListener {
            viewModel.editHabitItem(habitItemMapper.mapViewToHabitItem(binding))
        }
    }

    private fun handleInputError(error: Boolean, textInputEditText: TextInputEditText) {
        if (error) {
            textInputEditText.error = getString(R.string.invalid_input)
        } else {
            textInputEditText.error = null
        }
        checkStatusBtnSave()
    }

    private fun checkStatusBtnSave() {
        binding.btnSave.isEnabled = !(viewModel.errorInputRecurrenceNumber.value == true
                || viewModel.errorInputRecurrencePeriod.value == true
                || viewModel.errorInputName.value == true)
    }

    private fun parseArguments() {
        arguments?.let {
            screenMode = it.getString(SCREEN_MODE)
                ?: throw RuntimeException("Activity mode didn't setup")
            if (screenMode == EDIT_MODE) {
                    habitItemId = it.getInt(HABIT_ITEM_ID, HabitItem.UNDEFINED_ID)
            }
        } ?: throw java.lang.RuntimeException("Arguments didn't setup")
    }

    private fun setupSpinnerAdapter() {
        binding.spinnerPriority.adapter = spinnerAdapter
    }

    private fun isFieldsFilled(): Boolean {
        return with(binding) {
            tiedName.text?.isNotEmpty() == true
                    && tiedRecurrenceNumber.text?.isNotEmpty() == true
                    && tiedRecurrencePeriod.text?.isNotEmpty() == true

        }
    }

    private fun setupFields(habitItem: HabitItem) {
        with(binding) {
            tiedName.setText(habitItem.name)
            tiedDescription.setText(habitItem.description)
            val spinnerPosition = spinnerAdapter.getPosition(habitItem.priority)
            spinnerPriority.setSelection(spinnerPosition)
            val checkedRadioButtonId =
                habitItemMapper.mapHabitTypeToRadioButton(habitItem.type, binding)
            radioGroup.check(checkedRadioButtonId)
            tiedRecurrenceNumber.setText(habitItem.recurrenceNumber.toString())
            tiedRecurrencePeriod.setText(habitItem.recurrencePeriod.toString())
            setupColorViews(habitItem.color)
        }
    }

    private fun setupColorViews(colorInt: Int) {
        with(binding) {
            val color = colorMapper.mapColorToRgbHsv(colorInt)
            currentColor.setBackgroundColor(colorInt)
            tvCurrentColorRgb.text =
                getString(R.string.rgb_color, color.red, color.green, color.blue)
            tvCurrentColorHsv.text =
                getString(R.string.hsv_color, color.hue, color.saturation, color.value)
        }
    }

    companion object {

        private const val SCREEN_MODE = "activity mode"
        private const val ADD_MODE = "add mode"
        private const val EDIT_MODE = "edit mode"
        private const val HABIT_ITEM_ID = "habit item id"

        @JvmStatic
        fun newInstanceAddMode() = HabitItemFragment().apply {
            arguments = Bundle().apply {
                putString(SCREEN_MODE, ADD_MODE)
            }
        }

        @JvmStatic
        fun newInstancetEditMode(habitItemId: Int) = HabitItemFragment().apply {
            arguments = Bundle().apply {
                putString(SCREEN_MODE, EDIT_MODE)
                putInt(HABIT_ITEM_ID, habitItemId)
            }
        }
    }
}