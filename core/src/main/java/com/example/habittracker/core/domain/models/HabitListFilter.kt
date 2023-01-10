package com.example.habittracker.core.domain.models

data class HabitListFilter(
    var orderBy: HabitListOrderBy,
    var search: String
)
