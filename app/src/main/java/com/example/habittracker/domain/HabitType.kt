package com.example.habittracker.domain

import android.os.Parcelable
import com.example.habittracker.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType(val resourceId: Int) : Parcelable {
    BAD(R.string.bad_habit_type),
    GOOD(R.string.good_habit_type)
}
