package com.example.habittracker.feature_habits.presentation.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.Habit.Companion.UNDEFINED_ID
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.domain.models.Time
import com.example.habittracker.feature_habits.R.layout
import com.example.habittracker.feature_habits.databinding.FragmentHabitItemBinding
import com.example.habittracker.feature_habits.di.components.getComponent
import com.example.habittracker.feature_habits.presentation.color.ColorPicker
import com.example.habittracker.feature_habits.presentation.models.ColorRgbHsv
import com.example.habittracker.feature_habits.presentation.models.HabitPriorityApp
import com.example.habittracker.feature_habits.presentation.view_models.HabitItemViewModel
import com.example.habittracker.feature_habits.presentation.view_models.MainViewModel
import com.example.habittracker.ui_kit.R.string
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject
import kotlin.random.Random

class HabitItemFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitItemBinding? = null
    private val binding: FragmentHabitItemBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitItemBinding is null")

    @Inject
    lateinit var habitItemMapper: com.example.habittracker.feature_habits.presentation.mappers.HabitItemMapper

    @Inject
    lateinit var colorPicker: ColorPicker
    private val colors by lazy { colorPicker.colors() }

    @Inject
    lateinit var habitItemViewModel: HabitItemViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    private val habitId by lazy {
        arguments?.getInt(HABIT_ITEM_ID, UNDEFINED_ID)
            ?: throw RuntimeException("Unknown habitItemId")
    }

    private val spinnerAdapter by lazy {
        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            HabitPriority.values().map {
                when (it) {
                    HabitPriority.LOW -> getString(string.low_priority)
                    HabitPriority.NORMAL -> getString(string.normal_priority)
                    HabitPriority.HIGH -> getString(string.high_priority)
                }
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val habitItemFragmentComponent = getComponent {
            (activity as MainActivity)
                .featureHabitsComponent
                .habitItemFragmentComponentFactory()
                .create()
        }
        habitItemFragmentComponent.inject(this)
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
//        createRandomHabits()
        if (savedInstanceState == null) habitItemViewModel.chooseScreenMode(habitId)
        setUpSpinnerAdapter()
        setUpViewModelObservers(savedInstanceState)
        setUpTextChangeListeners()
        setUpColorScrollView()
        setButtonSaveClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpColorScrollView() {
        for (color in colors) {
            val view = LayoutInflater.from(requireActivity()).inflate(
                layout.item_color,
                binding.llColor,
                false
            )
            view.setBackgroundColor(color)
            view.setOnClickListener {
                setUpColorViews((it.background as ColorDrawable).color)
            }
            binding.llColor.addView(view)
        }
        val gradientDrawable =
            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorPicker.gradientColors())
        binding.llColor.background = gradientDrawable
    }

    private fun setUpTextChangeListeners() {
        binding.tiedName.addTextChangedListener {
            habitItemViewModel.validateName(it)
        }

        binding.tiedDescription.addTextChangedListener {
            habitItemViewModel.validateDescription(it)
        }

        binding.tiedRecurrenceNumber.addTextChangedListener {
            habitItemViewModel.validateRecurrenceNumber(it)
        }

        binding.tiedRecurrencePeriod.addTextChangedListener {
            habitItemViewModel.validateRecurrencePeriod(it)
        }
    }

    private fun setUpViewModelObservers(savedInstanceState: Bundle?) {
        habitItemViewModel.canCloseItemFragment.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        habitItemViewModel.errorInputRecurrenceNumber.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedRecurrenceNumber)
        }
        habitItemViewModel.errorInputRecurrencePeriod.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedRecurrencePeriod)
        }
        habitItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedName)
        }
        habitItemViewModel.errorInputDescription.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedDescription)
        }
        habitItemViewModel.currentFragmentHabit.observe(viewLifecycleOwner) {
            if (savedInstanceState == null) setUpFields(it)
            else setUpColorViews(habitItemViewModel.currentColor())
        }
    }

    private fun setButtonSaveClickListener() {
        binding.btnSave.setOnClickListener {
            if (isFieldsFilled()) {
                habitItemViewModel.upsertHabitItem(habitItemMapper.mapViewToHabit(binding))
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(string.not_all_required_fields_are_filled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createRandomTestHabits() {
        for (i in 1..15) {
            habitItemViewModel.addHabit(
                Habit(
                    "Name $i",
                    "This habit is very important for my self-development",
                    HabitPriority.NORMAL,
                    HabitType.GOOD,
                    colors[Random.nextInt(16)],
                    Random.nextInt(10) + 1,
                    Random.nextInt(30) + 1,
                    date = Time().currentUtcDateInSeconds(),
                    done = listOf()
                )
            )
        }
    }

    private fun handleInputError(error: Boolean, textInputEditText: TextInputEditText) {
        textInputEditText.error = if (error) getString(string.invalid_input) else null
        setStatusBtnSave()
    }

    private fun setStatusBtnSave() {
        binding.btnSave.isEnabled = !(habitItemViewModel.errorInputRecurrenceNumber.value == true
                || habitItemViewModel.errorInputRecurrencePeriod.value == true
                || habitItemViewModel.errorInputName.value == true
                || habitItemViewModel.errorInputDescription.value == true)
    }

    private fun setUpSpinnerAdapter() {
        binding.spinnerPriority.adapter = spinnerAdapter
    }

    private fun isFieldsFilled(): Boolean {
        return with(binding) {
            tiedName.text?.isNotEmpty() == true
                    && tiedDescription.text?.isNotEmpty() == true
                    && tiedRecurrenceNumber.text?.isNotEmpty() == true
                    && tiedRecurrencePeriod.text?.isNotEmpty() == true
        }
    }

    private fun setUpFields(habit: Habit) {
        with(binding) {
            if (habit.name != EMPTY_STRING) {
                tiedName.setText(habit.name)
                tiedDescription.setText(habit.description)
                tiedRecurrenceNumber.setText(habit.recurrenceNumber.toString())
                tiedRecurrencePeriod.setText(habit.recurrencePeriod.toString())
            }
            val habitPriorityApp = HabitPriorityApp.fromHabitPriority(habit.priority)
            val spinnerPosition = spinnerAdapter
                .getPosition(getString(habitPriorityApp.resourceId))
            spinnerPriority.setSelection(spinnerPosition)
            val checkedRadioButtonId =
                habitItemMapper.mapHabitTypeToRadioButton(habit.type, binding)
            radioGroup.check(checkedRadioButtonId)
            setUpColorViews(habit.color)
        }
    }

    private fun setUpColorViews(@ColorInt color: Int) {
        with(binding) {
            val colorRgbHsv = ColorRgbHsv.fromColor(color)
            currentColor.setBackgroundColor(color)
            habitItemViewModel.saveCurrentColor(color)
            tvCurrentColorRgb.text =
                getString(string.rgb_color, colorRgbHsv.red, colorRgbHsv.green, colorRgbHsv.blue)
            tvCurrentColorHsv.text =
                getString(
                    string.hsv_color,
                    colorRgbHsv.hue,
                    colorRgbHsv.saturation,
                    colorRgbHsv.value
                )
        }
    }

    override fun getTitleResId(): Int = string.habit_item

    companion object {

        private const val HABIT_ITEM_ID = "habit item id"
        private const val EMPTY_STRING = ""

        fun createArgs(habitItemId: Int) =
            Bundle().apply {
                putInt(HABIT_ITEM_ID, habitItemId)
            }
    }
}