package de.othregensburg.healthnote.repository

import androidx.lifecycle.LiveData
import de.othregensburg.healthnote.data.SettingsDao
import de.othregensburg.healthnote.model.Settings

class SettingsRepository(private val settingsDao: SettingsDao) {

    val readAllData: LiveData<List<Settings>> = settingsDao.readAllData()

    suspend fun addSetting(settings: Settings) {
        settingsDao.addSettings(settings)
    }

    suspend fun updateSetting(settings: Settings) {
        settingsDao.updateSettings(settings)
    }

    suspend fun deleteSetting(settings: Settings) {
        settingsDao.deleteSetting(settings)
    }

    suspend fun deleteAllSettings() {
        settingsDao.deleteAllSettings()
    }

    suspend fun setSeq() {
        settingsDao.setSeq()
    }
}