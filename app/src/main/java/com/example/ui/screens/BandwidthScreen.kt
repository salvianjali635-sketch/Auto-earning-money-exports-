package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.GlassCard
import com.example.ui.components.PulsingIndicator
import com.example.ui.theme.CyberGold
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.ConnectionType
import com.example.util.NetworkStatus

@Composable
fun BandwidthScreen(
    isBandwidthSharingActive: Boolean,
    networkMode: String,
    dailyLimitMb: Int,
    sharedMbToday: Double,
    networkStatus: NetworkStatus,
    onToggleBandwidth: () -> Unit,
    onSelectNetworkMode: (String) -> Unit,
    onSelectDailyLimit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dailyLimitOptions = listOf(500, 1024, 2048, 5120)
    val limitText = if (dailyLimitMb == 0) "Unlimited" else if (dailyLimitMb >= 1024) "${dailyLimitMb / 1024} GB" else "$dailyLimitMb MB"
    val progressFraction = if (dailyLimitMb > 0) (sharedMbToday / dailyLimitMb).toFloat().coerceIn(0f, 1f) else 0.45f
    val earnedFromBandwidth = (sharedMbToday / 1024.0) * 30.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("bandwidth_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Title Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Bandwidth Sharing Studio",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Monetize unused internet traffic at ₹30.00 / 1 GB",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElectricCyan.copy(alpha = 0.15f))
                        .border(1.dp, ElectricCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Bandwidth",
                        tint = ElectricCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Master Switch Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isBandwidthSharingActive) ElectricCyan else SlateBorder
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isBandwidthSharingActive) "Sharing Active" else "Sharing Suspended",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                PulsingIndicator(
                                    color = ElectricCyan,
                                    isActive = isBandwidthSharingActive && networkStatus.isConnected
                                )
                            }
                            Text(
                                text = if (isBandwidthSharingActive) "Relaying verified traffic nodes" else "Turn on to start credit generation",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Switch(
                            checked = isBandwidthSharingActive,
                            onCheckedChange = { onToggleBandwidth() },
                            modifier = Modifier.testTag("bandwidth_master_switch"),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = ElectricCyan,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = SlateCardVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live Telemetry Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Node Speed
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SlateCardVariant)
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Speed, contentDescription = "Speed", tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Transfer Rate", fontSize = 11.sp, color = TextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${networkStatus.transferSpeedKbps.toInt()} KB/s",
                                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        // Peers
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SlateCardVariant)
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.NetworkCheck, contentDescription = "Peers", tint = CyberGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Active Peers", fontSize = 11.sp, color = TextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${networkStatus.activePeers} Nodes",
                                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        // Latency
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SlateCardVariant)
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Bolt, contentDescription = "Ping", tint = NeonMint, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Latency", fontSize = 11.sp, color = TextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${networkStatus.latencyMs} ms",
                                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Data Shared & Progress Card
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY'S BANDWIDTH PROGRESS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "+₹${String.format("%.2f", earnedFromBandwidth)} Earned",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeonMint,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "${String.format("%.1f", sharedMbToday)} MB",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Text(
                            text = "Limit: $limitText",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ElectricCyan,
                        trackColor = SlateBorder,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }

        // Network Mode Selection (Wi-Fi Only vs Wi-Fi + Mobile)
        item {
            Text(
                text = "NETWORK SELECTION MODE",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Wi-Fi Only Chip
                val isWifiOnly = networkMode == "WIFI_ONLY"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isWifiOnly) ElectricCyan.copy(alpha = 0.15f) else SlateCardVariant)
                        .border(
                            1.dp,
                            if (isWifiOnly) ElectricCyan else SlateBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectNetworkMode("WIFI_ONLY") }
                        .testTag("mode_wifi_only")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Wi-Fi Only",
                            tint = if (isWifiOnly) ElectricCyan else TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Wi-Fi Only",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isWifiOnly) TextPrimary else TextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = "Protects SIM data",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Both Wi-Fi & Mobile Data
                val isBoth = networkMode == "WIFI_AND_MOBILE"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isBoth) ElectricCyan.copy(alpha = 0.15f) else SlateCardVariant)
                        .border(
                            1.dp,
                            if (isBoth) ElectricCyan else SlateBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectNetworkMode("WIFI_AND_MOBILE") }
                        .testTag("mode_both_networks")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SignalCellularAlt,
                            contentDescription = "Mobile & Wi-Fi",
                            tint = if (isBoth) ElectricCyan else TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Wi-Fi & 4G/5G",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isBoth) TextPrimary else TextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = "Maximum earnings",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Daily Limit Selection
        item {
            Text(
                text = "DAILY DATA CAP (LIMIT)",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dailyLimitOptions.forEach { limit ->
                    val isSelected = dailyLimitMb == limit
                    val label = if (limit >= 1024) "${limit / 1024} GB" else "$limit MB"

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) ElectricCyan.copy(alpha = 0.15f) else SlateCardVariant)
                            .border(
                                1.dp,
                                if (isSelected) ElectricCyan else SlateBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onSelectDailyLimit(limit) }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) ElectricCyan else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        }

        // Security & Privacy Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateCardVariant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(NeonMint.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VpnLock,
                            contentDescription = "Security",
                            tint = NeonMint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Encrypted Residential Node",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Zero access to your personal files, browsing history, or passwords. Strictly web-index proxy.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
