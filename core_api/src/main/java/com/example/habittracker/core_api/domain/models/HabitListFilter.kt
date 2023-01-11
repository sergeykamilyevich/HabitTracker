package com.example.habittracker.core_api.domain.models

data class HabitListFilter(
    var orderBy: HabitListOrderBy,
    var search: String
)
