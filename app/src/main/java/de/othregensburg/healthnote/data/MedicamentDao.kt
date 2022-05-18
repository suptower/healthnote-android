package de.othregensburg.healthnote.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicamentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMed(medicament: Medicament)

    @Query("SELECT * FROM med_table ORDER BY id")
    fun readAllData(): LiveData<List<Medicament>>
}