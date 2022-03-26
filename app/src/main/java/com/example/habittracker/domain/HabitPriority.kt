package com.example.habittracker.domain

import com.example.habittracker.R

enum class HabitPriority(val resourceId: Int) {
    LOWEST(R.string.lowest_priority),
    LOW(R.string.low_priority),
    NORMAL(R.string.normal_priority),
    HIGH(R.string.high_priority),
    HIGHEST(R.string.highest_priority)
}
