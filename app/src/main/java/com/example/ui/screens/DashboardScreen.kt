package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.TransactionEntity
import com.example.ui.DailyTrendItem
import com.example.ui.HourlyFlowItem
import com.example.ui.components.EarningsAnalyticsDashboard
import com.example.ui.components.EngineToggleRow
import com.example.ui.components.GlassCard
import com.example.ui.components.PulsingIndicator
import com.example.ui.theme.ButtonWhite
import com.example.ui.theme.CanvasDark
import com.example.ui.theme.CyberGold
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardGradientEnd
import com.example.ui.theme.SlateCardVariant
import com.example.ui.theme.SlateSubCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.NetworkStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    balance: Double,
    isKeepScreenAwake: Boolean,
    isAutoEarnActive: Boolean,
    isBandwidthSharingActive: Boolean,
    networkStatus: NetworkStatus,
    bandwidthSharedMb: Double,
    isAutoAdActive: Boolean,
    adCountdown: Int,
    adsWatchedToday: Int,
    sessionUptimeSeconds: Long,
    sessionEarnings: Double,
    todayEarnings: Double,
    isLoggedIn: Boolean,
    userPhone: String,
    kycVerified: Boolean,
    transactions: List<TransactionEntity>,
    dailyTrends: List<DailyTrendItem>,
    hourlyFlow: List<HourlyFlowItem>,
    efficiencyScore: Int,
    onToggleKeepScreenAwake: () -> Unit,
    onToggleAutoEarn: () -> Unit,
    onToggleBandwidth: () -> Unit,
    onToggleAutoAd: () -> Unit,
    onClaimDailyBonus: () -> Unit,
    onOpenLoginDialog: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var bonusClaimedMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sleek Interface Top Bar & Identity
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Sleek gradient icon tile
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(NeonMint, NeonMintDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_autoearn_logo),
                            contentDescription = "AutoEarn Logo",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "AutoEarn",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                letterSpacing = (-0.3).sp
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        if (isAutoEarnActive) NeonMint else TextMuted,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAutoEarnActive) "ENGINE ACTIVE" else "ENGINE PAUSED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isAutoEarnActive) NeonMint else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Keep Screen Awake toggle button
                    IconButton(
                        onClick = onToggleKeepScreenAwake,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SlateCard)
                            .border(1.dp, if (isKeepScreenAwake) CyberGold.copy(alpha = 0.5f) else SlateBorderLight, CircleShape)
                            .testTag("keep_awake_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SettingsBrightness,
                            contentDescription = "Keep Screen Awake",
                            tint = if (isKeepScreenAwake) CyberGold else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Login / KYC status chip
                    if (isLoggedIn) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SlateCard)
                                .border(1.dp, SlateBorderLight, RoundedCornerShape(20.dp))
                                .clickable { onLogout() }
                                .testTag("user_profile_chip")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = "KYC Verified",
                                    tint = NeonMint,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Verified",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonMint,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = onOpenLoginDialog,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonMint,
                                contentColor = CanvasDark
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            modifier = Modifier.testTag("login_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Login,
                                contentDescription = "Login",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Login +₹25",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Sleek Interface Master Balance Card (rounded-32px, gradient, 2-grid stats)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(SlateCard, SlateCardGradientEnd)
                        )
                    )
                    .border(1.dp, SlateBorder, RoundedCornerShape(32.dp))
                    .padding(22.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL BALANCE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                letterSpacing = 1.4.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(NeonMint.copy(alpha = 0.10f))
                                .border(1.dp, NeonMint.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = "Earning Rate",
                                    tint = NeonMint,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "+₹0.005/sec",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonMint,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Large Balance Amount
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "₹",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Light,
                                fontSize = 28.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.2f", balance),
                            style = MaterialTheme.typography.displaySmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Light,
                                fontSize = 40.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            modifier = Modifier.testTag("total_balance_text")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2-Column Grid: Today's Gain & Uptime
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Stat Box 1: Today
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(SlateSubCard)
                                .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "TODAY",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "+₹${String.format("%.2f", todayEarnings)}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = CyberGold,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        }

                        // Stat Box 2: Uptime
                        val uptimeHours = sessionUptimeSeconds / 3600
                        val uptimeMins = (sessionUptimeSeconds % 3600) / 60
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(SlateSubCard)
                                .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "UPTIME",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${uptimeHours}h ${uptimeMins}m",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Daily Bonus & Cashout CTA Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onClaimDailyBonus()
                                bonusClaimedMessage = "Claimed ₹5.00 daily bonus!"
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorderLight),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = CyberGold,
                                containerColor = SlateSubCard
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("claim_bonus_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = "Daily Bonus",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Daily +₹5.00",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        // Sleek High-Contrast White CTA Button
                        Button(
                            onClick = onNavigateToWallet,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonWhite,
                                contentColor = CanvasDark
                            ),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                                .testTag("cashout_button")
                        ) {
                            Text(
                                text = "Withdraw Funds",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Withdraw",
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    if (bonusClaimedMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = bonusClaimedMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(color = CyberGold)
                        )
                    }
                }
            }
        }

        // Quick Engine Controls Header
        item {
            Text(
                text = "AUTOMATED EARNING ENGINES",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // 3 Engine Toggles
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Engine 1: Screen Uptime
                EngineToggleRow(
                    title = "Screen Uptime Passive Engine",
                    subtitle = if (isAutoEarnActive) "Credits money automatically while active (~₹18/h)" else "Engine paused",
                    icon = Icons.Default.Smartphone,
                    iconColor = CyberGold,
                    isActive = isAutoEarnActive,
                    onToggle = onToggleAutoEarn,
                    testTag = "toggle_uptime_engine"
                )

                // Engine 2: Bandwidth Sharing
                EngineToggleRow(
                    title = "Bandwidth Sharing Studio",
                    subtitle = if (isBandwidthSharingActive) "Sharing excess data at ₹30.00 / 1 GB" else "Data sharing paused",
                    icon = Icons.Default.Wifi,
                    iconColor = ElectricCyan,
                    isActive = isBandwidthSharingActive,
                    onToggle = onToggleBandwidth,
                    testTag = "toggle_bandwidth_engine"
                )

                // Engine 3: Auto Ads Stream
                EngineToggleRow(
                    title = "Auto-Ad Stream Player",
                    subtitle = if (isAutoAdActive) "Continuous ads with ₹1.00 reward each" else "Ad streamer paused",
                    icon = Icons.Default.PlayArrow,
                    iconColor = NeonMint,
                    isActive = isAutoAdActive,
                    onToggle = onToggleAutoAd,
                    testTag = "toggle_ads_engine"
                )
            }
        }

        // Complete Earnings Analytics Dashboard Component (Current Earnings, Active Sessions, Historical Trends)
        item {
            EarningsAnalyticsDashboard(
                sessionUptimeSeconds = sessionUptimeSeconds,
                sessionEarnings = sessionEarnings,
                todayEarnings = todayEarnings,
                isUptimeActive = isAutoEarnActive,
                isBandwidthActive = isBandwidthSharingActive,
                networkStatus = networkStatus,
                bandwidthSharedMb = bandwidthSharedMb,
                isAutoAdActive = isAutoAdActive,
                adCountdown = adCountdown,
                adsWatchedToday = adsWatchedToday,
                dailyTrends = dailyTrends,
                hourlyFlow = hourlyFlow,
                efficiencyScore = efficiencyScore
            )
        }

        // Recent Passbook Transactions
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RECENT PASSBOOK ACTIVITY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onNavigateToWallet() }
                        ) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NeonMint,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "View Wallet",
                                tint = NeonMint,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (transactions.isEmpty()) {
                        Text(
                            text = "No transactions yet. Keep app open to earn!",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    } else {
                        transactions.take(4).forEach { tx ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                if (tx.type == "WITHDRAWAL") Color(0xFFFF4D6D) else NeonMint,
                                                CircleShape
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = tx.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = TextPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(tx.timestamp)),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextMuted,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = if (tx.type == "WITHDRAWAL") "-₹${String.format("%.2f", tx.amount)}" else "+₹${String.format("%.2f", tx.amount)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (tx.type == "WITHDRAWAL") Color(0xFFFF4D6D) else NeonMint,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
