package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DailyTrendItem
import com.example.ui.HourlyFlowItem
import com.example.ui.theme.CyberGold
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonMintDark
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardVariant
import com.example.ui.theme.SlateSubCard
import com.example.ui.theme.SlateTrack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.NetworkStatus

@Composable
fun EarningsAnalyticsDashboard(
    sessionUptimeSeconds: Long,
    sessionEarnings: Double,
    todayEarnings: Double,
    isUptimeActive: Boolean,
    isBandwidthActive: Boolean,
    networkStatus: NetworkStatus,
    bandwidthSharedMb: Double,
    isAutoAdActive: Boolean,
    adCountdown: Int,
    adsWatchedToday: Int,
    dailyTrends: List<DailyTrendItem>,
    hourlyFlow: List<HourlyFlowItem>,
    efficiencyScore: Int,
    modifier: Modifier = Modifier
) {
    var selectedDayIndex by remember { mutableStateOf(dailyTrends.lastIndex.coerceAtLeast(0)) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("earnings_analytics_dashboard"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SlateBorder),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = "Analytics",
                    tint = NeonMint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Earnings & Session Analytics",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Live telemetry, active engines & historical trends",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
            }
        }

        // Sleek Efficiency Score Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SlateCard)
                .border(1.dp, SlateBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Efficiency",
                            tint = NeonMint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EFFICIENCY SCORE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }
                    Text(
                        text = "$efficiencyScore%",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = NeonMint,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                }

                // Sleek Gradient Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(SlateTrack)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(efficiencyScore / 100f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(NeonMint, CyberGold)
                                )
                            )
                    )
                }
            }
        }

        // 1. Current Earnings Live Breakdown Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = SlateBorder
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CURRENT SESSION BREAKDOWN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonMint.copy(alpha = 0.12f))
                            .border(1.dp, NeonMint.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "LIVE TICKING",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonMint,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Session Gain",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "+₹${String.format("%.3f", sessionEarnings)}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = NeonMint,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Today's Total",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "₹${String.format("%.2f", todayEarnings)}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Source split bar (Uptime: Gold, Bandwidth: Cyan, Ads: Mint)
                Text(
                    text = "Earning Source Split",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.35f)
                            .fillMaxHeight()
                            .background(CyberGold)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        modifier = Modifier
                            .weight(0.40f)
                            .fillMaxHeight()
                            .background(ElectricCyan)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        modifier = Modifier
                            .weight(0.25f)
                            .fillMaxHeight()
                            .background(NeonMint)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(CyberGold, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Uptime 35%", fontSize = 11.sp, color = TextSecondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(ElectricCyan, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Net Share 40%", fontSize = 11.sp, color = TextSecondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(NeonMint, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ads 25%", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }

        // 2. Active Sessions Telemetry (Efficiency, Device Uptime, Bandwidth, Ads Stream)
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
                        text = "ACTIVE SESSIONS TELEMETRY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Efficiency Gauge Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateCardVariant)
                            .border(1.dp, NeonMint.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Efficiency",
                                tint = if (efficiencyScore > 60) NeonMint else CyberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$efficiencyScore% Efficiency",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (efficiencyScore > 60) NeonMint else CyberGold,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Telemetry Row 1: Device Uptime & Speed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Device Uptime Tile
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SlateSubCard)
                            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SlateBorder),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Uptime",
                                        tint = CyberGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                PulsingIndicator(color = CyberGold, isActive = isUptimeActive)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = formatTime(sessionUptimeSeconds),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (isUptimeActive) "Uptime Active (~₹18/h)" else "Uptime Paused",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    // Bandwidth Stream Tile
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SlateSubCard)
                            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SlateBorder),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Wifi,
                                        contentDescription = "Net Stream",
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                PulsingIndicator(color = ElectricCyan, isActive = isBandwidthActive)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "${networkStatus.transferSpeedKbps.toInt()} KB/s",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${String.format("%.1f", bandwidthSharedMb)} MB • ${networkStatus.activePeers} Peers",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Telemetry Row 2: Auto Ads Session
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SlateSubCard)
                        .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SlateBorder),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = "Ads Stream",
                                    tint = NeonMint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Auto-Ad Stream",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = "$adsWatchedToday Ads watched today (+₹${adsWatchedToday}.00 earned)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        // Countdown Ring / Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonMint.copy(alpha = 0.12f))
                                .border(1.dp, NeonMint.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isAutoAdActive) "Next in ${adCountdown}s" else "Paused",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonMint,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // 3. Historical Trends (7-Day Interactive Bar Chart & Selected Day Telemetry)
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
                        text = "7-DAY EARNINGS HISTORY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Tap bar to inspect",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bar Chart
                val maxEarning = (dailyTrends.maxOfOrNull { it.earnings } ?: 50.0).coerceAtLeast(1.0)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    dailyTrends.forEachIndexed { index, item ->
                        val isSelected = index == selectedDayIndex
                        val barHeightFraction = (item.earnings / maxEarning).toFloat().coerceIn(0.15f, 1f)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedDayIndex = index }
                                .padding(horizontal = 2.dp)
                        ) {
                            // Value above bar
                            Text(
                                text = "₹${item.earnings.toInt()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = if (isSelected) NeonMint else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // The Bar
                            Box(
                                modifier = Modifier
                                    .width(if (isSelected) 22.dp else 16.dp)
                                    .fillMaxHeight(barHeightFraction)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        if (isSelected) {
                                            Brush.verticalGradient(listOf(NeonMint, NeonMint.copy(alpha = 0.6f)))
                                        } else {
                                            Brush.verticalGradient(listOf(SlateBorder, SlateCardVariant))
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 1.dp else 0.dp,
                                        color = if (isSelected) NeonMint else Color.Transparent,
                                        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Day Label
                            Text(
                                text = item.dayLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Selected Day Details Card
                val selectedDay = dailyTrends.getOrNull(selectedDayIndex) ?: dailyTrends.last()
                Card(
                    colors = CardDefaults.cardColors(containerColor = SlateCardVariant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Details: ${selectedDay.dayLabel} (${selectedDay.fullDate})",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = "Total: ₹${String.format("%.2f", selectedDay.earnings)}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = NeonMint,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Uptime", fontSize = 10.sp, color = TextMuted)
                                Text("${selectedDay.uptimeMinutes} mins", fontSize = 12.sp, color = CyberGold, fontWeight = FontWeight.SemiBold)
                            }
                            Column {
                                Text("Net Shared", fontSize = 10.sp, color = TextMuted)
                                Text("${selectedDay.sharedMb.toInt()} MB", fontSize = 12.sp, color = ElectricCyan, fontWeight = FontWeight.SemiBold)
                            }
                            Column {
                                Text("Ads Streamed", fontSize = 10.sp, color = TextMuted)
                                Text("${selectedDay.adsWatched} ads", fontSize = 12.sp, color = NeonMint, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hourly Flow Bars
                Text(
                    text = "HOURLY PEAK FLOW (DISTRIBUTION)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    hourlyFlow.forEach { flow ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${flow.label} (${flow.period})",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                                Text(
                                    text = "₹${String.format("%.2f", flow.earnings)} (${flow.percentage}%)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { flow.percentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (flow.percentage >= 35) NeonMint else CyberGold,
                                trackColor = SlateBorder,
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(seconds: Long): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return if (hrs > 0) {
        String.format("%02d:%02d:%02d", hrs, mins, secs)
    } else {
        String.format("%02d:%02d", mins, secs)
    }
}
