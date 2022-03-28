package com.example.habittracker.domain

data class HabitListFilter(
    var orderBy: HabitListOrderBy,
    var search: String
)
