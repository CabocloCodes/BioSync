package com.biosync.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.biosync.app.util.Converters

@Entity(tableName = "tasks")
@TypeConverters(Converters::class)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val mentalEffort: MentalEffort,
    val tags: List<String> = emptyList(),
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val isCompleted: Boolean = false,
    val priority: Int = 0
)

enum class MentalEffort(val label: String, val labelPt: String) {
    LOW("Low", "Baixo"),
    MEDIUM("Medium", "Médio"),
    HIGH("High", "Alto")
}

enum class EnergyLevel(val label: String, val labelPt: String) {
    LOW("Low Energy", "Energia Baixa"),
    MEDIUM("Medium Energy", "Energia Média"),
    HIGH("High Energy", "Energia Alta");

    fun suggestedEfforts(): List<MentalEffort> {
        return when (this) {
            LOW -> listOf(MentalEffort.LOW)
            MEDIUM -> listOf(MentalEffort.LOW, MentalEffort.MEDIUM)
            HIGH -> listOf(MentalEffort.LOW, MentalEffort.MEDIUM, MentalEffort.HIGH)
        }
    }
}
