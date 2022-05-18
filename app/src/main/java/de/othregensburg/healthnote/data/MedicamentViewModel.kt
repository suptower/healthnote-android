package de.othregensburg.healthnote.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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
}