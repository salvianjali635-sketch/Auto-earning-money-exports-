package com.example

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.LoginOtpDialog
import com.example.ui.screens.AutoAdScreen
import com.example.ui.screens.BandwidthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.AutoEarnTheme
import com.example.ui.theme.CanvasDark
import com.example.ui.theme.NeonMint
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AutoEarnTheme {
                val isKeepScreenAwake by viewModel.isKeepScreenAwake.collectAsStateWithLifecycle()

                // Keep screen awake flag management
                LaunchedEffect(isKeepScreenAwake) {
                    if (isKeepScreenAwake) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                AutoEarnAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AutoEarnAppContent(viewModel: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showLoginDialog by remember { mutableStateOf(false) }

    val balance by viewModel.balance.collectAsStateWithLifecycle()
    val isKeepScreenAwake by viewModel.isKeepScreenAwake.collectAsStateWithLifecycle()
    val isAutoEarnActive by viewModel.isAutoEarnActive.collectAsStateWithLifecycle()
    val isBandwidthSharingActive by viewModel.isBandwidthSharingActive.collectAsStateWithLifecycle()
    val bandwidthNetworkMode by viewModel.bandwidthNetworkMode.collectAsStateWithLifecycle()
    val bandwidthDailyLimitMb by viewModel.bandwidthDailyLimitMb.collectAsStateWithLifecycle()
    val bandwidthSharedMbToday by viewModel.bandwidthSharedMbToday.collectAsStateWithLifecycle()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()

    val isAutoAdActive by viewModel.isAutoAdActive.collectAsStateWithLifecycle()
    val currentAd by viewModel.currentAd.collectAsStateWithLifecycle()
    val adCountdown by viewModel.adCountdown.collectAsStateWithLifecycle()
    val isAdPlaying by viewModel.isAdPlaying.collectAsStateWithLifecycle()
    val adRewardAmount by viewModel.adRewardAmount.collectAsStateWithLifecycle()
    val isBackgroundAdActive by viewModel.isBackgroundAdActive.collectAsStateWithLifecycle()
    val adsWatchedToday by viewModel.adsWatchedToday.collectAsStateWithLifecycle()

    val sessionUptimeSeconds by viewModel.sessionUptimeSeconds.collectAsStateWithLifecycle()
    val sessionEarnings by viewModel.sessionEarnings.collectAsStateWithLifecycle()
    val todayEarnings by viewModel.todayEarnings.collectAsStateWithLifecycle()
    val dailyTrends by viewModel.dailyTrends.collectAsStateWithLifecycle()
    val hourlyFlow = viewModel.hourlyFlow
    val efficiencyScore = viewModel.calculateEfficiencyScore()

    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val userPhone by viewModel.userPhone.collectAsStateWithLifecycle()
    val kycVerified by viewModel.kycVerified.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    if (showLoginDialog) {
        LoginOtpDialog(
            onDismiss = { showLoginDialog = false },
            onLoginSuccess = { phone, otp ->
                viewModel.loginWithOtp(phone, otp)
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CanvasDark),
        containerColor = CanvasDark,
        bottomBar = {
            NavigationBar(
                containerColor = SlateCard,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .drawBehind {
                        drawLine(
                            color = SlateBorder,
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Dashboard",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Dashboard",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CanvasDark,
                        selectedTextColor = NeonMint,
                        indicatorColor = NeonMint,
                        unselectedIconColor = Color.White.copy(alpha = 0.40f),
                        unselectedTextColor = Color.White.copy(alpha = 0.40f)
                    ),
                    modifier = Modifier.testTag("nav_dashboard")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Bandwidth",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Bandwidth",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CanvasDark,
                        selectedTextColor = NeonMint,
                        indicatorColor = NeonMint,
                        unselectedIconColor = Color.White.copy(alpha = 0.40f),
                        unselectedTextColor = Color.White.copy(alpha = 0.40f)
                    ),
                    modifier = Modifier.testTag("nav_bandwidth")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Tv,
                            contentDescription = "Auto Ads",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Auto Ads",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CanvasDark,
                        selectedTextColor = NeonMint,
                        indicatorColor = NeonMint,
                        unselectedIconColor = Color.White.copy(alpha = 0.40f),
                        unselectedTextColor = Color.White.copy(alpha = 0.40f)
                    ),
                    modifier = Modifier.testTag("nav_ads")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Wallet",
                            fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CanvasDark,
                        selectedTextColor = NeonMint,
                        indicatorColor = NeonMint,
                        unselectedIconColor = Color.White.copy(alpha = 0.40f),
                        unselectedTextColor = Color.White.copy(alpha = 0.40f)
                    ),
                    modifier = Modifier.testTag("nav_wallet")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(
                    balance = balance,
                    isKeepScreenAwake = isKeepScreenAwake,
                    isAutoEarnActive = isAutoEarnActive,
                    isBandwidthSharingActive = isBandwidthSharingActive,
                    networkStatus = networkStatus,
                    bandwidthSharedMb = bandwidthSharedMbToday,
                    isAutoAdActive = isAutoAdActive,
                    adCountdown = adCountdown,
                    adsWatchedToday = adsWatchedToday,
                    sessionUptimeSeconds = sessionUptimeSeconds,
                    sessionEarnings = sessionEarnings,
                    todayEarnings = todayEarnings,
                    isLoggedIn = isLoggedIn,
                    userPhone = userPhone,
                    kycVerified = kycVerified,
                    transactions = transactions,
                    dailyTrends = dailyTrends,
                    hourlyFlow = hourlyFlow,
                    efficiencyScore = efficiencyScore,
                    onToggleKeepScreenAwake = { viewModel.toggleKeepScreenAwake() },
                    onToggleAutoEarn = { viewModel.toggleAutoEarn() },
                    onToggleBandwidth = { viewModel.toggleBandwidthSharing() },
                    onToggleAutoAd = { viewModel.toggleAutoAd() },
                    onClaimDailyBonus = { viewModel.claimDailyBonus() },
                    onOpenLoginDialog = { showLoginDialog = true },
                    onNavigateToWallet = { selectedTab = 3 },
                    onLogout = { viewModel.logout() }
                )
                1 -> BandwidthScreen(
                    isBandwidthSharingActive = isBandwidthSharingActive,
                    networkMode = bandwidthNetworkMode,
                    dailyLimitMb = bandwidthDailyLimitMb,
                    sharedMbToday = bandwidthSharedMbToday,
                    networkStatus = networkStatus,
                    onToggleBandwidth = { viewModel.toggleBandwidthSharing() },
                    onSelectNetworkMode = { viewModel.setBandwidthNetworkMode(it) },
                    onSelectDailyLimit = { viewModel.setBandwidthLimit(it) }
                )
                2 -> AutoAdScreen(
                    currentAd = currentAd,
                    adCountdown = adCountdown,
                    isAdPlaying = isAdPlaying,
                    isAutoAdActive = isAutoAdActive,
                    adRewardAmount = adRewardAmount,
                    isBackgroundAdActive = isBackgroundAdActive,
                    adsWatchedToday = adsWatchedToday,
                    onToggleAutoAd = { viewModel.toggleAutoAd() },
                    onTogglePlayback = { viewModel.toggleAdPlayback() },
                    onSkipAd = { viewModel.skipCurrentAd() },
                    onSelectRewardAmount = { viewModel.setAdRewardAmount(it) },
                    onToggleBackgroundAd = { viewModel.toggleBackgroundAd() }
                )
                3 -> WalletScreen(
                    balance = balance,
                    transactions = transactions,
                    onWithdraw = { amt, method, dest ->
                        viewModel.withdraw(amt, method, dest)
                    }
                )
            }
        }
    }
}
