package de.othregensburg.healthnote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import de.othregensburg.healthnote.data.MedicamentDatabase
import de.othregensburg.healthnote.repository.MedicamentRepository
import de.othregensburg.healthnote.model.Medicament
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicamentViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Medicament>>
    private val repository: MedicamentRepository

    init {
        val medicamentDao = MedicamentDatabase.getDatabase(application).medicamentDao()
        repository = MedicamentRepository(medicamentDao)
        readAllData = repository.readAllData
    }

    fun addMed(medicament: Medicament){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMed(medicament)
        }
    }

    fun updateMed(medicament: Medicament) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMed(medicament)
        }
    }

    fun deleteMed(medicament: Medicament) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMed(medicament)
        }
    }

    fun deleteAllMeds() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMeds()
        }
    }
}