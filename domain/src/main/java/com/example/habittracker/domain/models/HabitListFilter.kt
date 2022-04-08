package com.example.habittracker.domain.models

data class HabitListFilter(
    var orderBy: HabitListOrderBy,
    var search: String
)
