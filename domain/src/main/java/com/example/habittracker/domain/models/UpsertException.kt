package com.example.habittracker.domain.models

abstract class UpsertException(var message: String)

class HabitAlreadyExistsException(message: String) :
    UpsertException("A habit with name $message already exists")

class UnknownSqlException(message: String) : UpsertException("SQL error: $message")
