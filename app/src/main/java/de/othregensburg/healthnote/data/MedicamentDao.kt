package de.othregensburg.healthnote.data

import androidx.lifecycle.LiveData
import androidx.room.*
import de.othregensburg.healthnote.model.Medicament

@Dao
interface MedicamentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMed(medicament: Medicament)

    @Update
    suspend fun updateMed(med: Medicament)

    @Delete
    suspend fun deleteMed(medicament: Medicament)

    @Query("DELETE FROM med_table")
    suspend fun deleteAllMeds()

    @Query("UPDATE sqlite_sequence SET SEQ = 0 WHERE NAME = 'med_table'")
    suspend fun setSeq()

    @Query("SELECT * FROM med_table ORDER BY id")
    fun readAllData(): LiveData<List<Medicament>>


}