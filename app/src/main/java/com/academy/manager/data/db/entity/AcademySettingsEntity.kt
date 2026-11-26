package com.academy.manager.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "academy_settings")
data class AcademySettingsEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val address: String,
    val phone: String,
    val logoPath: String?
)
