package de.othregensburg.healthnote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import de.othregensburg.healthnote.data.SettingsDatabase
import de.othregensburg.healthnote.model.Settings
import de.othregensburg.healthnote.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Settings>>
    private val repository: SettingsRepository

    init {
        val settingsDao = SettingsDatabase.getDatabase(application).settingsDao()
        repository = SettingsRepository(settingsDao)
        readAllData = repository.readAllData
    }

    fun addSetting(settings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSetting(settings)
        }
    }

    fun updateSetting(settings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSetting(settings)
        }
    }

    fun deleteSetting(settings: Settings) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSetting(settings)
        }
    }

    fun deleteAllSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllSettings()
            repository.setSeq()
        }
    }
}