package com.example.model

data class SponsorAd(
    val id: String,
    val brandName: String,
    val headline: String,
    val description: String,
    val rewardAmount: Double,
    val durationSeconds: Int,
    val category: String,
    val ctaText: String,
    val verifiedBadge: Boolean = true
)
