package com.example.habittracker.domain.models

open class UpsertException(override var message: String) :
    RuntimeException(message)

class HabitAlreadyExistsException(override var message: String) :
    UpsertException("A habit with name $message already exists")

class UnknownSqlException(override var message: String) : UpsertException("SQL error: $message")
