package com.example.habittracker.domain.models

sealed class UpsertException(private val message: String) { //TODO rename to DbException
    fun message(): String = message
}

class HabitAlreadyExistsException(message: String) : UpsertException(message)

class SqlException(message: String) : UpsertException(message)
