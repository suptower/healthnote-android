package de.othregensburg.healthnote.repository

import androidx.lifecycle.LiveData
import de.othregensburg.healthnote.data.MedicamentDao
import de.othregensburg.healthnote.model.Medicament

class MedicamentRepository(private val medicamentDao: MedicamentDao) {

    val readAllData: LiveData<List<Medicament>> = medicamentDao.readAllData()

    suspend fun addMed(medicament: Medicament){
        medicamentDao.addMed(medicament)
    }

    suspend fun updateMed(medicament: Medicament) {
        medicamentDao.updateMed(medicament)
    }
}