package com.example.habittracker.domain.models

sealed class DbException(private val message: String) { //TODO rename to DbException
    fun message(): String = message
}

class HabitAlreadyExistsException(message: String) : DbException(message)

class SqlException(message: String) : DbException(message)
