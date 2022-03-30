package com.example.habittracker.domain.entities

data class HabitListFilter(
    var orderBy: HabitListOrderBy,
    var search: String
)
