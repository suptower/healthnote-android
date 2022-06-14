package de.othregensburg.healthnote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "settings_table")
data class Settings(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val usePIN: Boolean,
    val code: String,
    val useBiometrics: Boolean
) : Parcelable