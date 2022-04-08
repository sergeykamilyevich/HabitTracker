package com.example.habittracker.domain.models

class HabitAlreadyExistsException(var name: String) :
    Exception("A habit with name $name already exists")