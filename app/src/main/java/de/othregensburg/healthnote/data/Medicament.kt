package de.othregensburg.healthnote.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "med_table")
data class Medicament(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
