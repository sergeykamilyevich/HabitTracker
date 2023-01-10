package com.example.habittracker.feature_habits.data.db.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habittracker.feature_habits.data.db.models.HabitDbModel
import com.example.habittracker.feature_habits.data.db.models.HabitDoneDbModel

@Database(
    entities = [
        HabitDbModel::class,
        HabitDoneDbModel::class
    ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object {

        private var instance: AppDataBase? = null
        private val lock = Any()
        private const val DB_NAME = "habit.db"

        fun getInstance(application: Application): AppDataBase {
            instance?.let {
                return it
            }
            synchronized(lock) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()
                instance = db
                return db
            }
        }
    }
}