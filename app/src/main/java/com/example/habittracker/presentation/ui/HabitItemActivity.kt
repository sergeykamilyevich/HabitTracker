package com.example.habittracker.presentation.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.R
import com.example.habittracker.databinding.ActivityHabitItemBinding
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitItem.Companion.UNDEFINED_ID
import com.example.habittracker.domain.HabitPriority
import com.example.habittracker.presentation.color.ColorPicker
import com.example.habittracker.presentation.mappers.ColorMapper
import com.example.habittracker.presentation.mappers.HabitItemMapper
import com.example.habittracker.presentation.view_models.HabitItemViewModel
import com.google.android.material.textfield.TextInputEditText

class HabitItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitItemBinding
    private lateinit var viewModel: HabitItemViewModel
    private val habitItemMapper = HabitItemMapper()
    private val colorMapper = ColorMapper()

    private val adapter by lazy {
        ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            HabitPriority.values()
        )
    }
    private val colorPicker = ColorPicker()
    private val colors = colorPicker.getColors()
    private val gradientColors = colorPicker.getGradientColors()

//    private var habitItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[HabitItemViewModel::class.java]

        parseIntentActivityMode()
        setupSpinnerAdapter()
        setupViewModelObservers()
        setupTextChangeListeners()
        setupScrollView()
    }

    private fun setupScrollView() {
        for (color in colors) {
            val view = LayoutInflater.from(this).inflate(
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
        viewModel.canCloseScreen.observe(this) {
            finish()
        }
        viewModel.errorInputRecurrenceNumber.observe(this) {
            handleInputError(it, binding.tiedRecurrenceNumber)
        }

        viewModel.errorInputRecurrencePeriod.observe(this) {
            handleInputError(it, binding.tiedRecurrencePeriod)
        }

        viewModel.errorInputName.observe(this) {
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
                    this,
                    getString(R.string.not_all_required_fields_are_filled),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun launchEditMode() {
        val habitItemId = intent.getIntExtra(HABIT_ITEM_ID, UNDEFINED_ID)
        viewModel.getHabitItem(habitItemId)
        viewModel.habitItem.observe(this) {
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

    private fun parseIntentActivityMode() {
        val activityMode = intent.getStringExtra(ACTIVITY_MODE)
            ?: throw RuntimeException("Activity mode didn't setup")
        when (activityMode) {
            ADD_MODE -> launchAddMode()
            EDIT_MODE -> launchEditMode()
            else -> throw RuntimeException("Unknown activity mode: $activityMode")
        }
    }

    private fun setupSpinnerAdapter() {
        binding.spinnerPriority.adapter = adapter
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
            val spinnerPosition = adapter.getPosition(habitItem.priority)
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

        private const val ACTIVITY_MODE = "activity mode"
        private const val ADD_MODE = "add mode"
        private const val EDIT_MODE = "edit mode"
        private const val HABIT_ITEM_ID = "habit item id"

        fun newIntentAddMode(context: Context): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, ADD_MODE)
            return intent
        }

        fun newIntentEditMode(context: Context, habitItemId: Int): Intent {
            val intent = Intent(context, HabitItemActivity::class.java)
            intent.putExtra(ACTIVITY_MODE, EDIT_MODE)
            intent.putExtra(HABIT_ITEM_ID, habitItemId)
            return intent
        }
    }

}
