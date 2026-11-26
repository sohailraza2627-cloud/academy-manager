package com.academy.manager.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "school_classes",
    indices = [Index(value = ["name"])]
)
data class SchoolClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val section: String,
    val monthlyFee: Long,
    val isActive: Boolean = true
) {
    val displayName: String
        get() = if (section.isBlank()) name else "$name ($section)"
}
