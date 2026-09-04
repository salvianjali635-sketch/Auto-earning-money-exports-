package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SponsorAd
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

@Composable
fun AutoAdScreen(
    currentAd: SponsorAd,
    adCountdown: Int,
    isAdPlaying: Boolean,
    isAutoAdActive: Boolean,
    adRewardAmount: Double,
    isBackgroundAdActive: Boolean,
    adsWatchedToday: Int,
    onToggleAutoAd: () -> Unit,
    onTogglePlayback: () -> Unit,
    onSkipAd: () -> Unit,
    onSelectRewardAmount: (Double) -> Unit,
    onToggleBackgroundAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalAdDuration = currentAd.durationSeconds.coerceAtLeast(1)
    val progressFraction = (1f - (adCountdown.toFloat() / totalAdDuration)).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "ad_progress")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("auto_ad_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Auto-Ad Stream Player",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Continuous looping sponsor ads with instant cash credits",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonMint.copy(alpha = 0.15f))
                        .border(1.dp, NeonMint.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = "Ads Player",
                        tint = NeonMint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Live Video / Ad Stream Player Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonMint.copy(alpha = 0.6f)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Video player frame simulation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(SlateCardVariant, Color(0xFF0D1424))
                                )
                            )
                            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Circular Countdown Ring
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { animatedProgress },
                                    modifier = Modifier.size(68.dp),
                                    color = NeonMint,
                                    trackColor = SlateBorder,
                                    strokeWidth = 5.dp
                                )
                                Text(
                                    text = "${adCountdown}s",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = currentAd.brandName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = currentAd.headline,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NeonMint,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        // Top right reward badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeonMint.copy(alpha = 0.2f))
                                .border(1.dp, NeonMint.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+₹${String.format("%.2f", adRewardAmount)}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonMint,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Ad description text
                    Text(
                        text = currentAd.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Player Controls Row (Pause/Play, Skip, External CTA)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onTogglePlayback,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAdPlaying) SlateCardVariant else NeonMint,
                                contentColor = if (isAdPlaying) TextPrimary else Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("ad_play_pause_button")
                        ) {
                            Icon(
                                imageVector = if (isAdPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isAdPlaying) "Pause" else "Play",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAdPlaying) "Pause" else "Resume",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onSkipAd,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("skip_ad_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = "Skip Ad",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Skip Ad",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Reward Tier Setting (User Controls: ₹0.50, ₹1.00, ₹2.00)
        item {
            Text(
                text = "FIXED REWARD PER AD (TARGET CREDIT)",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val rewardOptions = listOf(0.50, 1.00, 2.00)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rewardOptions.forEach { amt ->
                    val isSelected = adRewardAmount == amt
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonMint.copy(alpha = 0.15f) else SlateCardVariant)
                            .border(
                                1.dp,
                                if (isSelected) NeonMint else SlateBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectRewardAmount(amt) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "₹${String.format("%.2f", amt)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (isSelected) NeonMint else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (amt == 0.50) "5s Ads" else if (amt == 1.00) "10s Ads" else "15s Ads",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Background Ad Mode Switch
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Background Ad Stream Mode",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Keep earning ad rewards even when screen turns off",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Switch(
                        checked = isBackgroundAdActive,
                        onCheckedChange = { onToggleBackgroundAd() },
                        modifier = Modifier.testTag("toggle_background_ad_mode"),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonMint,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = SlateCardVariant
                        )
                    )
                }
            }
        }

        // Today's Ad Streaming Stats
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$adsWatchedToday",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Text(
                            text = "Ads Watched Today",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                            .background(SlateBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "₹${String.format("%.2f", adsWatchedToday * adRewardAmount)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = NeonMint,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Text(
                            text = "Earned from Ads",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
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
