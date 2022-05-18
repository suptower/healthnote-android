package de.othregensburg.healthnote.data

import androidx.lifecycle.LiveData

class MedicamentRepository(private val medicamentDao: MedicamentDao) {

    val readAllData: LiveData<List<Medicament>> = medicamentDao.readAllData()

    suspend fun addMed(medicament: Medicament){
        medicamentDao.addMed(medicament)
    }
}