package com.example.habittracker.domain

class HabitAlreadyExistsException(var name: String) :
    Exception("A habit with name $name already exists")