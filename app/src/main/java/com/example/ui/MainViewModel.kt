package com.example.ui

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.AutoEarnApp
import com.example.model.EarningType
import com.example.model.SponsorAd
import com.example.model.TransactionEntity
import com.example.service.AutoEarnForegroundService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class DailyTrendItem(
    val dayLabel: String,
    val fullDate: String,
    val earnings: Double,
    val uptimeMinutes: Int,
    val sharedMb: Double,
    val adsWatched: Int
)

data class HourlyFlowItem(
    val period: String,
    val label: String,
    val percentage: Int,
    val earnings: Double
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as AutoEarnApp
    private val repo = app.repository
    private val dao = app.database.transactionDao()
    private val networkMonitor = app.networkMonitor

    // Repository flows
    val balance: StateFlow<Double> = repo.balance
    val isKeepScreenAwake: StateFlow<Boolean> = repo.isKeepScreenAwake
    val isAutoEarnActive: StateFlow<Boolean> = repo.isAutoEarnActive
    val isBandwidthSharingActive: StateFlow<Boolean> = repo.isBandwidthSharingActive
    val bandwidthNetworkMode: StateFlow<String> = repo.bandwidthNetworkMode
    val bandwidthDailyLimitMb: StateFlow<Int> = repo.bandwidthDailyLimitMb
    val bandwidthSharedMbToday: StateFlow<Double> = repo.bandwidthSharedMbToday
    val isAutoAdActive: StateFlow<Boolean> = repo.isAutoAdActive
    val adRewardAmount: StateFlow<Double> = repo.adRewardAmount
    val isBackgroundAdActive: StateFlow<Boolean> = repo.isBackgroundAdActive
    val adsWatchedToday: StateFlow<Int> = repo.adsWatchedToday
    val isLoggedIn: StateFlow<Boolean> = repo.isLoggedIn
    val userPhone: StateFlow<String> = repo.userPhone
    val userName: StateFlow<String> = repo.userName
    val kycVerified: StateFlow<Boolean> = repo.kycVerified

    val networkStatus = networkMonitor.networkStatus

    val transactions: StateFlow<List<TransactionEntity>> = dao.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Live session states
    private val _sessionUptimeSeconds = MutableStateFlow(1420L)
    val sessionUptimeSeconds: StateFlow<Long> = _sessionUptimeSeconds.asStateFlow()

    private val _sessionEarnings = MutableStateFlow(4.85)
    val sessionEarnings: StateFlow<Double> = _sessionEarnings.asStateFlow()

    private val _todayEarnings = MutableStateFlow(32.40)
    val todayEarnings: StateFlow<Double> = _todayEarnings.asStateFlow()

    // Ads state
    private val sponsorCatalog = listOf(
        SponsorAd(
            id = "ad_1",
            brandName = "CyberCloud AI",
            headline = "Decentralized GPU Cloud",
            description = "Rent high-performance computing power at 70% lower cost.",
            rewardAmount = 1.00,
            durationSeconds = 8,
            category = "Cloud & AI",
            ctaText = "Explore Nodes"
        ),
        SponsorAd(
            id = "ad_2",
            brandName = "FinPay Global",
            headline = "Instant Zero-Fee Transfers",
            description = "Send money internationally with zero platform markup & real-time UPI.",
            rewardAmount = 1.00,
            durationSeconds = 10,
            category = "Fintech",
            ctaText = "Open Account"
        ),
        SponsorAd(
            id = "ad_3",
            brandName = "ShieldVPN Pro",
            headline = "Next-Gen Quantum Encryption",
            description = "Protect your browsing, bypass geo-restrictions with 10Gbps servers.",
            rewardAmount = 1.00,
            durationSeconds = 7,
            category = "Security",
            ctaText = "Protect Now"
        ),
        SponsorAd(
            id = "ad_4",
            brandName = "CryptoStaker X",
            headline = "Automated Yield Aggregator",
            description = "Earn up to 14.5% APY with insured automated smart contract vaults.",
            rewardAmount = 1.00,
            durationSeconds = 9,
            category = "Web3",
            ctaText = "Start Staking"
        )
    )

    private var currentAdIndex = 0
    private val _currentAd = MutableStateFlow(sponsorCatalog[0])
    val currentAd: StateFlow<SponsorAd> = _currentAd.asStateFlow()

    private val _adCountdown = MutableStateFlow(sponsorCatalog[0].durationSeconds)
    val adCountdown: StateFlow<Int> = _adCountdown.asStateFlow()

    private val _isAdPlaying = MutableStateFlow(true)
    val isAdPlaying: StateFlow<Boolean> = _isAdPlaying.asStateFlow()

    // Daily trends data
    private val _dailyTrends = MutableStateFlow(generateHistoricalTrends())
    val dailyTrends: StateFlow<List<DailyTrendItem>> = _dailyTrends.asStateFlow()

    // Hourly flow
    val hourlyFlow = listOf(
        HourlyFlowItem("06:00 - 12:00", "Morning", 22, 7.15),
        HourlyFlowItem("12:00 - 18:00", "Afternoon", 38, 12.35),
        HourlyFlowItem("18:00 - 23:00", "Peak Hours", 32, 10.40),
        HourlyFlowItem("23:00 - 06:00", "Night Uptime", 8, 2.50)
    )

    private var earningTickerJob: Job? = null
    private var adTickerJob: Job? = null

    init {
        seedInitialTransactionsIfNeeded()
        startForegroundService()
        startMainTicker()
        startAdLoop()
    }

    private fun startForegroundService() {
        try {
            val intent = Intent(app, AutoEarnForegroundService::class.java).apply {
                action = AutoEarnForegroundService.ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                app.startForegroundService(intent)
            } else {
                app.startService(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startMainTicker() {
        earningTickerJob?.cancel()
        earningTickerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L) // 1 second tick for live UI
                _sessionUptimeSeconds.value += 1

                val isUptime = repo.isAutoEarnActive.value
                val isBandwidth = repo.isBandwidthSharingActive.value

                var tickEarnings = 0.0
                if (isUptime) {
                    tickEarnings += 0.005 // ~₹18/hr
                }
                if (isBandwidth && networkMonitor.networkStatus.value.isConnected) {
                    tickEarnings += 0.006 // data sharing increment
                    repo.addBandwidthShared(0.04) // ~40KB/sec
                }

                if (tickEarnings > 0) {
                    _sessionEarnings.value += tickEarnings
                    _todayEarnings.value += tickEarnings
                    repo.creditBalance(tickEarnings)
                }

                if (_sessionUptimeSeconds.value % 5 == 0L) {
                    networkMonitor.updateStatus(isBandwidth)
                }
            }
        }
    }

    private fun startAdLoop() {
        adTickerJob?.cancel()
        adTickerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                if (repo.isAutoAdActive.value && _isAdPlaying.value) {
                    if (_adCountdown.value > 1) {
                        _adCountdown.value -= 1
                    } else {
                        // Ad completed!
                        completeCurrentAd()
                    }
                }
            }
        }
    }

    private fun completeCurrentAd() {
        val reward = repo.adRewardAmount.value
        repo.creditBalance(reward)
        repo.incrementAdsWatched()
        _sessionEarnings.value += reward
        _todayEarnings.value += reward

        // Insert record
        viewModelScope.launch {
            dao.insertTransaction(
                TransactionEntity(
                    amount = reward,
                    type = EarningType.AD_REWARD.name,
                    title = "Reward: ${_currentAd.value.brandName}",
                    referenceId = "AD-${UUID.randomUUID().toString().take(8).uppercase()}"
                )
            )
        }

        // Advance to next ad
        currentAdIndex = (currentAdIndex + 1) % sponsorCatalog.size
        val nextAd = sponsorCatalog[currentAdIndex]
        _currentAd.value = nextAd
        _adCountdown.value = nextAd.durationSeconds
    }

    fun skipCurrentAd() {
        currentAdIndex = (currentAdIndex + 1) % sponsorCatalog.size
        val nextAd = sponsorCatalog[currentAdIndex]
        _currentAd.value = nextAd
        _adCountdown.value = nextAd.durationSeconds
    }

    fun toggleAdPlayback() {
        _isAdPlaying.value = !_isAdPlaying.value
    }

    fun toggleKeepScreenAwake() {
        repo.setKeepScreenAwake(!repo.isKeepScreenAwake.value)
    }

    fun toggleAutoEarn() {
        repo.setAutoEarnActive(!repo.isAutoEarnActive.value)
    }

    fun toggleBandwidthSharing() {
        val newState = !repo.isBandwidthSharingActive.value
        repo.setBandwidthSharingActive(newState)
        networkMonitor.updateStatus(newState)
    }

    fun setBandwidthNetworkMode(mode: String) {
        repo.setBandwidthNetworkMode(mode)
    }

    fun setBandwidthLimit(limitMb: Int) {
        repo.setBandwidthDailyLimitMb(limitMb)
    }

    fun toggleAutoAd() {
        repo.setAutoAdActive(!repo.isAutoAdActive.value)
    }

    fun setAdRewardAmount(amount: Double) {
        repo.setAdRewardAmount(amount)
    }

    fun toggleBackgroundAd() {
        repo.setBackgroundAdActive(!repo.isBackgroundAdActive.value)
    }

    fun claimDailyBonus(): Boolean {
        val bonus = 5.00
        repo.creditBalance(bonus)
        repo.setDailyClaimTimestamp(System.currentTimeMillis())
        _todayEarnings.value += bonus
        viewModelScope.launch {
            dao.insertTransaction(
                TransactionEntity(
                    amount = bonus,
                    type = EarningType.DAILY_BONUS.name,
                    title = "Daily Check-in Reward",
                    referenceId = "DB-${UUID.randomUUID().toString().take(8).uppercase()}"
                )
            )
        }
        return true
    }

    fun loginWithOtp(phone: String, otp: String): Boolean {
        if (phone.length == 10 && otp.length == 6) {
            val wasLoggedInBefore = repo.isLoggedIn.value
            repo.setLogin(phone, "User_${phone.takeLast(4)}")
            if (!wasLoggedInBefore) {
                // Grant welcome bonus of ₹25.00
                val welcomeBonus = 25.00
                repo.creditBalance(welcomeBonus)
                _todayEarnings.value += welcomeBonus
                viewModelScope.launch {
                    dao.insertTransaction(
                        TransactionEntity(
                            amount = welcomeBonus,
                            type = EarningType.LOGIN_BONUS.name,
                            title = "Welcome Bonus (OTP Verified)",
                            referenceId = "WB-${UUID.randomUUID().toString().take(8).uppercase()}"
                        )
                    )
                }
            }
            return true
        }
        return false
    }

    fun logout() {
        repo.logout()
    }

    fun withdraw(amount: Double, method: String, destination: String): Pair<Boolean, String> {
        if (amount < 20.0) {
            return Pair(false, "Minimum cashout threshold is ₹20.00")
        }
        if (repo.balance.value < amount) {
            return Pair(false, "Insufficient balance (Available: ₹${String.format("%.2f", repo.balance.value)})")
        }

        val success = repo.debitBalance(amount)
        if (success) {
            val refId = "TXN-${UUID.randomUUID().toString().take(8).uppercase()}"
            viewModelScope.launch {
                dao.insertTransaction(
                    TransactionEntity(
                        amount = amount,
                        type = EarningType.WITHDRAWAL.name,
                        title = "Withdrawal via $method ($destination)",
                        referenceId = refId,
                        status = "COMPLETED",
                        paymentMethod = method
                    )
                )
            }
            return Pair(true, refId)
        }
        return Pair(false, "Transaction failed")
    }

    private fun seedInitialTransactionsIfNeeded() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            dao.insertTransaction(
                TransactionEntity(
                    amount = 10.00,
                    type = EarningType.LOGIN_BONUS.name,
                    title = "Starter Bonus Credit",
                    timestamp = now - 3600000 * 5,
                    referenceId = "INIT-78492"
                )
            )
            dao.insertTransaction(
                TransactionEntity(
                    amount = 8.50,
                    type = EarningType.BANDWIDTH_SHARING.name,
                    title = "Bandwidth Shared: 280 MB",
                    timestamp = now - 3600000 * 3,
                    referenceId = "NET-99120"
                )
            )
            dao.insertTransaction(
                TransactionEntity(
                    amount = 6.00,
                    type = EarningType.AD_REWARD.name,
                    title = "Sponsored Stream Pack (6 Ads)",
                    timestamp = now - 3600000 * 1,
                    referenceId = "AD-33104"
                )
            )
        }
    }

    private fun generateHistoricalTrends(): List<DailyTrendItem> {
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Today")
        val earnings = listOf(18.50, 24.20, 31.00, 28.40, 36.80, 42.50, 32.40)
        val uptimes = listOf(75, 110, 140, 125, 160, 190, 142)
        val sharedMbs = listOf(350.0, 480.0, 620.0, 540.0, 710.0, 890.0, 342.8)
        val ads = listOf(8, 12, 16, 14, 19, 22, 14)

        return days.mapIndexed { index, day ->
            DailyTrendItem(
                dayLabel = day,
                fullDate = "Aug ${28 + index}, 2026",
                earnings = earnings[index],
                uptimeMinutes = uptimes[index],
                sharedMb = sharedMbs[index],
                adsWatched = ads[index]
            )
        }
    }

    fun calculateEfficiencyScore(): Int {
        var score = 0
        if (repo.isAutoEarnActive.value) score += 35
        if (repo.isBandwidthSharingActive.value && networkMonitor.networkStatus.value.isConnected) score += 35
        if (repo.isAutoAdActive.value && _isAdPlaying.value) score += 30
        return score
    }
}
