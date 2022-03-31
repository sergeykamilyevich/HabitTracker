package com.example.habittracker.data

class HabitAlreadyExistsException(var name: String) :
    Exception("A habit with name $name already exists")