package com.example.habittracker.presentation.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitItemBinding
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.Habit.Companion.UNDEFINED_ID
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.domain.models.Time
import com.example.habittracker.presentation.color.ColorPicker
import com.example.habittracker.presentation.mappers.HabitItemMapper
import com.example.habittracker.presentation.models.ColorRgbHsv
import com.example.habittracker.presentation.models.HabitPriorityApp
import com.example.habittracker.presentation.view_models.HabitItemViewModel
import com.example.habittracker.presentation.view_models.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject
import kotlin.random.Random

class HabitItemFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitItemBinding? = null
    private val binding: FragmentHabitItemBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitItemBinding is null")

    @Inject
    lateinit var habitItemMapper: HabitItemMapper //TODO split or merge?

    @Inject
    lateinit var colorPicker: ColorPicker
    private val colors by lazy { colorPicker.getColors() }
    private val gradientColors by lazy { colorPicker.getGradientColors() }

    @Inject
    lateinit var viewModel: HabitItemViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    private var habitItemId: Int = UNDEFINED_ID
    private val spinnerAdapter by lazy {
        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            HabitPriority.values().map {
                when (it) {
                    HabitPriority.LOW -> getString(R.string.low_priority)
                    HabitPriority.NORMAL -> getString(R.string.normal_priority)
                    HabitPriority.HIGH -> getString(R.string.high_priority)
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habitItemId = arguments?.getInt(HABIT_ITEM_ID, UNDEFINED_ID)
            ?: throw RuntimeException("Unknown habitItemId")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)
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
        chooseScreenMode()
        setupSpinnerAdapter()
        setupViewModelObservers()
        setupTextChangeListeners()
        setupColorScrollView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun chooseScreenMode() {
        if (habitItemId == UNDEFINED_ID) launchAddMode() else launchEditMode()
    }

    private fun setupColorScrollView() {
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
        binding.tiedName.addTextChangedListener {
            viewModel.validateName(it)
        }

        binding.tiedDescription.addTextChangedListener {
            viewModel.validateDescription(it)
        }

        binding.tiedRecurrenceNumber.addTextChangedListener {
            viewModel.validateRecurrenceNumber(it)
        }

        binding.tiedRecurrencePeriod.addTextChangedListener {
            viewModel.validateRecurrencePeriod(it)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.canCloseScreen.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
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
        viewModel.errorInputDescription.observe(viewLifecycleOwner) {
            handleInputError(it, binding.tiedDescription)
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
        viewModel.getHabitItem(habitItemId)
        viewModel.habit.observe(viewLifecycleOwner) {
            setupFields(it)
        }
        binding.btnSave.setOnClickListener {
            viewModel.editHabitItem(habitItemMapper.mapViewToHabitItem(binding))
        }
    }

    private fun createRandomTestHabits() {
        for (i in 1..15) {
            viewModel.addHabitItem(
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
        R.string.low_priority
    }

    private fun handleInputError(error: Boolean, textInputEditText: TextInputEditText) {
        textInputEditText.error = if (error) getString(R.string.invalid_input) else null
        setStatusBtnSave()
    }

    private fun setStatusBtnSave() {
        binding.btnSave.isEnabled = !(viewModel.errorInputRecurrenceNumber.value == true
                || viewModel.errorInputRecurrencePeriod.value == true
                || viewModel.errorInputName.value == true
                || viewModel.errorInputDescription.value == true)
    }

    private fun setupSpinnerAdapter() {
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

    private fun setupFields(habit: Habit) {
        with(binding) {
            tiedName.setText(habit.name)
            tiedDescription.setText(habit.description)
            val habitPriorityApp = HabitPriorityApp.fromHabitPriority(habit.priority)
            val spinnerPosition = spinnerAdapter
                .getPosition(getString(habitPriorityApp.resourceId))
            spinnerPriority.setSelection(spinnerPosition)
            val checkedRadioButtonId =
                habitItemMapper.mapHabitTypeToRadioButton(habit.type, binding)
            radioGroup.check(checkedRadioButtonId)
            tiedRecurrenceNumber.setText(habit.recurrenceNumber.toString())
            tiedRecurrencePeriod.setText(habit.recurrencePeriod.toString())
            setupColorViews(habit.color)
        }
    }

    private fun setupColorViews(colorInt: Int) {
        with(binding) {
            val colorRgbHsv = ColorRgbHsv.fromColor(colorInt)
            currentColor.setBackgroundColor(colorInt)
            tvCurrentColorRgb.text =
                getString(R.string.rgb_color, colorRgbHsv.red, colorRgbHsv.green, colorRgbHsv.blue)
            tvCurrentColorHsv.text =
                getString(
                    R.string.hsv_color,
                    colorRgbHsv.hue,
                    colorRgbHsv.saturation,
                    colorRgbHsv.value
                )
        }
    }

    override fun getTitleResId(): Int = R.string.habit_item

    companion object {

        private const val HABIT_ITEM_ID = "habit item id"

        fun createArgs(habitItemId: Int) =
            Bundle().apply {
                putInt(HABIT_ITEM_ID, habitItemId)
            }
    }
}