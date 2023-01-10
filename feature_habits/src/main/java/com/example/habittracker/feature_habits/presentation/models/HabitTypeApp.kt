package com.example.habittracker.feature_habits.presentation.models

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.habittracker.core.domain.models.HabitType
import com.example.habittracker.ui_kit.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitTypeApp(@StringRes val resourceId: Int, val intType: Int) : Parcelable {
    BAD(R.string.bad_habit_type, 0),
    GOOD(R.string.good_habit_type, 1);

    fun toNonNullableHabitType() = HabitType.valueOf(this.name)

    companion object {
        fun toHabitType(habitTypeApp: HabitTypeApp?) =
            if (habitTypeApp != null) HabitType.valueOf(habitTypeApp.name) else null

        fun fromHabitType(habitType: HabitType?) =
            if (habitType != null) valueOf(habitType.name) else null

        fun fromNonNullableHabitType(habitType: HabitType) = valueOf(habitType.name)
    }
}
