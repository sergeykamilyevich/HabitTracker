package com.example.habittracker.db_impl.data.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habittracker.db_api.data.db.models.HabitDbModel
import com.example.habittracker.db_api.data.db.models.HabitDoneDbModel
import com.example.habittracker.db_api.data.db.room.HabitDao

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

        fun getInstance(context: Context): AppDataBase {
            instance?.let {
                return it
            }
            synchronized(lock) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()
                instance = db
                return db
            }
        }
    }
}