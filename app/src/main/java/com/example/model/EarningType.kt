package com.example.model

enum class EarningType(val displayName: String, val badgeText: String) {
    SCREEN_UPTIME("Screen Uptime", "Passive"),
    BANDWIDTH_SHARING("Bandwidth Sharing", "Net Share"),
    AD_REWARD("Sponsor Ad", "Rewarded"),
    DAILY_BONUS("Daily Check-in", "Bonus"),
    LOGIN_BONUS("Welcome Bonus", "Reward"),
    WITHDRAWAL("Cashout", "Withdraw")
}
