package de.othregensburg.healthnote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.othregensburg.healthnote.model.Settings

@Database(entities = [Settings::class], version = 1, exportSchema = false)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: SettingsDatabase? = null

        fun getDatabase(context: Context): SettingsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SettingsDatabase::class.java,
                    "settings_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}