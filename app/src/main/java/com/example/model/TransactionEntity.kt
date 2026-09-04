package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: String, // EarningType name
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "COMPLETED", // COMPLETED, PROCESSING, FAILED
    val referenceId: String,
    val paymentMethod: String? = null
)
