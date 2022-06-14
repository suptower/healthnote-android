package de.othregensburg.healthnote.data

import androidx.lifecycle.LiveData
import androidx.room.*
import de.othregensburg.healthnote.model.Settings

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSettings(settings: Settings)

    @Update
    suspend fun updateSettings(settings: Settings)

    @Delete
    suspend fun deleteSetting(settings: Settings)

    @Query("DELETE FROM settings_table")
    suspend fun deleteAllSettings()

    @Query("UPDATE sqlite_sequence SET SEQ = 0 WHERE NAME = 'settings_table'")
    suspend fun setSeq()

    @Query("SELECT * FROM settings_table ORDER BY id")
    fun readAllData(): LiveData<List<Settings>>
}