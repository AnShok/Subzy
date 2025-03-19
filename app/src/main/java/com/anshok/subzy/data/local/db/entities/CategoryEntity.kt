package com.anshok.subzy.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // Название категории (например, "Развлечения")
    val budget: Double? = null, // Бюджет для категории (опционально)
    val color: Int // Цвет категории (для отображения в UI)
)