package de.othregensburg.healthnote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.othregensburg.healthnote.model.Medicament

@Database(entities = [Medicament::class], version = 1, exportSchema = false)
abstract class MedicamentDatabase: RoomDatabase() {
    abstract fun medicamentDao(): MedicamentDao

    companion object{
        @Volatile
        private var INSTANCE: MedicamentDatabase? = null

        fun getDatabase(context: Context): MedicamentDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicamentDatabase::class.java,
                    "medicament_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}