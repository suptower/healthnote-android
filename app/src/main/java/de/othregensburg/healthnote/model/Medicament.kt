package de.othregensburg.healthnote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "med_table")
data class Medicament(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
) : Parcelable
