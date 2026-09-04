package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyberGold
import com.example.ui.theme.NeonMint
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LoginOtpDialog(
    onDismiss: () -> Unit,
    onLoginSuccess: (phone: String, otp: String) -> Boolean
) {
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) } // 1: phone, 2: otp
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var generatedDemoOtp by remember { mutableStateOf("749216") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(listOf(NeonMint.copy(alpha = 0.6f), SlateBorder)),
                    shape = RoundedCornerShape(24.dp)
                ),
            color = SlateCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(NeonMint.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Security",
                                tint = NeonMint,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (step == 1) "Fast Mobile Login" else "Verify OTP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Welcome Bonus Banner
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberGold.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "Bonus",
                            tint = CyberGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Instant ₹25.00 Welcome Cash Bonus credited upon verification!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (step == 1) {
                    Text(
                        text = "Enter your 10-digit mobile number to login securely without password.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                phoneNumber = it
                                errorMessage = null
                            }
                        },
                        label = { Text("Mobile Number") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = "Phone", tint = NeonMint)
                        },
                        prefix = { Text("+91 ", color = TextPrimary, fontWeight = FontWeight.Bold) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonMint,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = NeonMint,
                            focusedContainerColor = SlateCardVariant,
                            unfocusedContainerColor = SlateCardVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("phone_input")
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (phoneNumber.length == 10) {
                                generatedDemoOtp = "${(100000..999999).random()}"
                                step = 2
                                errorMessage = null
                            } else {
                                errorMessage = "Please enter a valid 10-digit number"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("send_otp_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonMint,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Send 6-Digit OTP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    Text(
                        text = "Enter the 6-digit code sent to +91 $phoneNumber",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick test chip for easy entry
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SlateCardVariant)
                            .border(1.dp, NeonMint.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable {
                                otpCode = generatedDemoOtp
                                errorMessage = null
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Auto-Fill Code: ",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = generatedDemoOtp,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NeonMint,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = {
                            if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                otpCode = it
                                errorMessage = null
                            }
                        },
                        label = { Text("6-Digit OTP") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "OTP", tint = NeonMint)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonMint,
                            unfocusedBorderColor = SlateBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = NeonMint,
                            focusedContainerColor = SlateCardVariant,
                            unfocusedContainerColor = SlateCardVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("otp_input")
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (otpCode.length == 6) {
                                val success = onLoginSuccess(phoneNumber, otpCode)
                                if (success) {
                                    onDismiss()
                                } else {
                                    errorMessage = "Invalid OTP code"
                                }
                            } else {
                                errorMessage = "Please enter complete 6-digit OTP"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("verify_otp_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonMint,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Verify & Claim ₹25.00 Bonus",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Change Number",
                            modifier = Modifier.clickable { step = 1 },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}
