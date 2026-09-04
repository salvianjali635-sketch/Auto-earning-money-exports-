package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("autoearn_prefs", Context.MODE_PRIVATE)

    private val _balance = MutableStateFlow(
        prefs.getFloat("balance", 24.50f).toDouble()
    )
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _isKeepScreenAwake = MutableStateFlow(
        prefs.getBoolean("keep_screen_awake", true)
    )
    val isKeepScreenAwake: StateFlow<Boolean> = _isKeepScreenAwake.asStateFlow()

    private val _isAutoEarnActive = MutableStateFlow(
        prefs.getBoolean("auto_earn_active", true)
    )
    val isAutoEarnActive: StateFlow<Boolean> = _isAutoEarnActive.asStateFlow()

    private val _isBandwidthSharingActive = MutableStateFlow(
        prefs.getBoolean("bandwidth_sharing_active", true)
    )
    val isBandwidthSharingActive: StateFlow<Boolean> = _isBandwidthSharingActive.asStateFlow()

    private val _bandwidthNetworkMode = MutableStateFlow(
        prefs.getString("bandwidth_network_mode", "WIFI_AND_MOBILE") ?: "WIFI_AND_MOBILE"
    )
    val bandwidthNetworkMode: StateFlow<String> = _bandwidthNetworkMode.asStateFlow()

    private val _bandwidthDailyLimitMb = MutableStateFlow(
        prefs.getInt("bandwidth_daily_limit_mb", 1024)
    )
    val bandwidthDailyLimitMb: StateFlow<Int> = _bandwidthDailyLimitMb.asStateFlow()

    private val _bandwidthSharedMbToday = MutableStateFlow(
        prefs.getFloat("bandwidth_shared_mb_today", 342.8f).toDouble()
    )
    val bandwidthSharedMbToday: StateFlow<Double> = _bandwidthSharedMbToday.asStateFlow()

    private val _isAutoAdActive = MutableStateFlow(
        prefs.getBoolean("auto_ad_active", true)
    )
    val isAutoAdActive: StateFlow<Boolean> = _isAutoAdActive.asStateFlow()

    private val _adRewardAmount = MutableStateFlow(
        prefs.getFloat("ad_reward_amount", 1.00f).toDouble()
    )
    val adRewardAmount: StateFlow<Double> = _adRewardAmount.asStateFlow()

    private val _isBackgroundAdActive = MutableStateFlow(
        prefs.getBoolean("background_ad_active", true)
    )
    val isBackgroundAdActive: StateFlow<Boolean> = _isBackgroundAdActive.asStateFlow()

    private val _adsWatchedToday = MutableStateFlow(
        prefs.getInt("ads_watched_today", 14)
    )
    val adsWatchedToday: StateFlow<Int> = _adsWatchedToday.asStateFlow()

    private val _lastDailyClaimTimestamp = MutableStateFlow(
        prefs.getLong("last_daily_claim_ts", 0L)
    )
    val lastDailyClaimTimestamp: StateFlow<Long> = _lastDailyClaimTimestamp.asStateFlow()

    // Auth State
    private val _isLoggedIn = MutableStateFlow(
        prefs.getBoolean("is_logged_in", false)
    )
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userPhone = MutableStateFlow(
        prefs.getString("user_phone", "") ?: ""
    )
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _userName = MutableStateFlow(
        prefs.getString("user_name", "Verified Earner") ?: "Verified Earner"
    )
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _kycVerified = MutableStateFlow(
        prefs.getBoolean("kyc_verified", false)
    )
    val kycVerified: StateFlow<Boolean> = _kycVerified.asStateFlow()

    fun updateBalance(newBalance: Double) {
        val rounded = Math.round(newBalance * 100.0) / 100.0
        _balance.value = rounded
        prefs.edit().putFloat("balance", rounded.toFloat()).apply()
    }

    fun creditBalance(amount: Double) {
        updateBalance(_balance.value + amount)
    }

    fun debitBalance(amount: Double): Boolean {
        if (_balance.value >= amount) {
            updateBalance(_balance.value - amount)
            return true
        }
        return false
    }

    fun setKeepScreenAwake(awake: Boolean) {
        _isKeepScreenAwake.value = awake
        prefs.edit().putBoolean("keep_screen_awake", awake).apply()
    }

    fun setAutoEarnActive(active: Boolean) {
        _isAutoEarnActive.value = active
        prefs.edit().putBoolean("auto_earn_active", active).apply()
    }

    fun setBandwidthSharingActive(active: Boolean) {
        _isBandwidthSharingActive.value = active
        prefs.edit().putBoolean("bandwidth_sharing_active", active).apply()
    }

    fun setBandwidthNetworkMode(mode: String) {
        _bandwidthNetworkMode.value = mode
        prefs.edit().putString("bandwidth_network_mode", mode).apply()
    }

    fun setBandwidthDailyLimitMb(limit: Int) {
        _bandwidthDailyLimitMb.value = limit
        prefs.edit().putInt("bandwidth_daily_limit_mb", limit).apply()
    }

    fun addBandwidthShared(mb: Double) {
        val newShared = _bandwidthSharedMbToday.value + mb
        _bandwidthSharedMbToday.value = newShared
        prefs.edit().putFloat("bandwidth_shared_mb_today", newShared.toFloat()).apply()
    }

    fun setAutoAdActive(active: Boolean) {
        _isAutoAdActive.value = active
        prefs.edit().putBoolean("auto_ad_active", active).apply()
    }

    fun setAdRewardAmount(amount: Double) {
        _adRewardAmount.value = amount
        prefs.edit().putFloat("ad_reward_amount", amount.toFloat()).apply()
    }

    fun setBackgroundAdActive(active: Boolean) {
        _isBackgroundAdActive.value = active
        prefs.edit().putBoolean("background_ad_active", active).apply()
    }

    fun incrementAdsWatched() {
        val count = _adsWatchedToday.value + 1
        _adsWatchedToday.value = count
        prefs.edit().putInt("ads_watched_today", count).apply()
    }

    fun setDailyClaimTimestamp(ts: Long) {
        _lastDailyClaimTimestamp.value = ts
        prefs.edit().putLong("last_daily_claim_ts", ts).apply()
    }

    fun setLogin(phone: String, name: String = "AutoEarner") {
        _isLoggedIn.value = true
        _userPhone.value = phone
        _userName.value = name
        _kycVerified.value = true
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_phone", phone)
            .putString("user_name", name)
            .putBoolean("kyc_verified", true)
            .apply()
    }

    fun logout() {
        _isLoggedIn.value = false
        _userPhone.value = ""
        _kycVerified.value = false
        prefs.edit()
            .putBoolean("is_logged_in", false)
            .putString("user_phone", "")
            .putBoolean("kyc_verified", false)
            .apply()
    }
}
