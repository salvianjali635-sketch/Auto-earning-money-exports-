package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.TransactionEntity
import com.example.ui.components.GlassCard
import com.example.ui.theme.ButtonWhite
import com.example.ui.theme.CanvasDark
import com.example.ui.theme.CyberGold
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateBorderLight
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateCardVariant
import com.example.ui.theme.SlateSubCard
import com.example.ui.theme.SlateTrack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    balance: Double,
    transactions: List<TransactionEntity>,
    onWithdraw: (amount: Double, method: String, destination: String) -> Pair<Boolean, String>,
    modifier: Modifier = Modifier
) {
    var selectedMethodTab by remember { mutableIntStateOf(0) } // 0: UPI, 1: Bank, 2: Paytm
    val methodTitles = listOf("UPI", "Bank Account", "Paytm")

    var upiId by remember { mutableStateOf("") }
    var bankAccountNo by remember { mutableStateOf("") }
    var bankIfsc by remember { mutableStateOf("") }
    var paytmPhone by remember { mutableStateOf("") }

    val quickAmounts = listOf(20.0, 50.0, 100.0, 200.0)
    var selectedAmount by remember { mutableStateOf(20.0) }
    var customAmountText by remember { mutableStateOf("20") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var receiptData by remember { mutableStateOf<Pair<String, Double>?>(null) }

    var selectedHistoryFilter by remember { mutableStateOf("ALL") }

    val minWithdrawal = 20.0
    val progressFraction = (balance / minWithdrawal).toFloat().coerceIn(0f, 1f)

    // Digital Receipt Modal
    if (receiptData != null) {
        val (refId, amount) = receiptData!!
        Dialog(onDismissRequest = { receiptData = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, SlateBorderLight, RoundedCornerShape(24.dp)),
                color = SlateCard
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeonMint.copy(alpha = 0.15f))
                            .border(1.dp, NeonMint.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = NeonMint,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Withdrawal Successful!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Funds sent to your destination account",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SlateSubCard)
                            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Amount Paid", color = TextSecondary, fontSize = 13.sp)
                                Text("₹${String.format("%.2f", amount)}", color = NeonMint, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Reference ID", color = TextSecondary, fontSize = 13.sp)
                                Text(refId, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Status", color = TextSecondary, fontSize = 13.sp)
                                Text("COMPLETED", color = NeonMint, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { receiptData = null },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonWhite,
                            contentColor = CanvasDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Done", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("wallet_screen"),
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
                        text = "Wallet & Cashout",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Instant transfers via UPI, Bank Account & Paytm",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SlateSubCard)
                        .border(1.dp, SlateBorder, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = NeonMint,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Available Balance Card & Payout Threshold
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SlateBorder
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AVAILABLE FOR CASHOUT",
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
                                text = "INSTANT CASHOUT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonMint,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = NeonMint,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.2f", balance),
                            style = MaterialTheme.typography.displaySmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Minimum payout progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (balance >= minWithdrawal) "Withdrawal Unlocked!" else "Need ₹${String.format("%.2f", minWithdrawal - balance)} more to cashout",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (balance >= minWithdrawal) NeonMint else CyberGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "Min ₹${minWithdrawal.toInt()}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(SlateTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressFraction)
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
        }

        // Cashout Form
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "SELECT CASHOUT METHOD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Method Tabs
                    TabRow(
                        selectedTabIndex = selectedMethodTab,
                        containerColor = SlateSubCard,
                        contentColor = NeonMint,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedMethodTab]),
                                color = NeonMint
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
                    ) {
                        methodTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedMethodTab == index,
                                onClick = { selectedMethodTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selectedMethodTab == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedMethodTab == index) NeonMint else TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input field based on selected method
                    when (selectedMethodTab) {
                        0 -> {
                            OutlinedTextField(
                                value = upiId,
                                onValueChange = { upiId = it; errorMessage = null },
                                label = { Text("UPI ID (GPay / PhonePe / Paytm)") },
                                placeholder = { Text("e.g. 9876543210@paytm") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.QrCode, contentDescription = "UPI", tint = NeonMint)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonMint,
                                    unfocusedBorderColor = SlateBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedContainerColor = SlateSubCard,
                                    unfocusedContainerColor = SlateSubCard
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("upi_input")
                            )
                        }
                        1 -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = bankAccountNo,
                                    onValueChange = { bankAccountNo = it; errorMessage = null },
                                    label = { Text("Bank Account Number") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    leadingIcon = {
                                        Icon(Icons.Default.AccountBalance, contentDescription = "Bank", tint = NeonMint)
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NeonMint,
                                        unfocusedBorderColor = SlateBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        focusedContainerColor = SlateSubCard,
                                        unfocusedContainerColor = SlateSubCard
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("bank_account_input")
                                )
                                OutlinedTextField(
                                    value = bankIfsc,
                                    onValueChange = { bankIfsc = it.uppercase(); errorMessage = null },
                                    label = { Text("IFSC Code") },
                                    placeholder = { Text("e.g. SBIN0001234") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NeonMint,
                                        unfocusedBorderColor = SlateBorder,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        focusedContainerColor = SlateSubCard,
                                        unfocusedContainerColor = SlateSubCard
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("bank_ifsc_input")
                                )
                            }
                        }
                        2 -> {
                            OutlinedTextField(
                                value = paytmPhone,
                                onValueChange = { paytmPhone = it; errorMessage = null },
                                label = { Text("Paytm Registered Mobile") },
                                placeholder = { Text("10-digit mobile number") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                leadingIcon = {
                                    Icon(Icons.Default.PhoneAndroid, contentDescription = "Paytm", tint = NeonMint)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonMint,
                                    unfocusedBorderColor = SlateBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedContainerColor = SlateSubCard,
                                    unfocusedContainerColor = SlateSubCard
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("paytm_input")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Amount Chips
                    Text(
                        text = "QUICK AMOUNT CHIPS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickAmounts.forEach { amt ->
                            val isSelected = selectedAmount == amt
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) NeonMint.copy(alpha = 0.15f) else SlateSubCard)
                                    .border(
                                        1.dp,
                                        if (isSelected) NeonMint else SlateBorder,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        selectedAmount = amt
                                        customAmountText = amt.toInt().toString()
                                    }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                ) {
                                    Text(
                                        text = "₹${amt.toInt()}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (isSelected) NeonMint else TextPrimary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Withdraw CTA (High-Contrast White button)
                    Button(
                        onClick = {
                            val method = methodTitles[selectedMethodTab]
                            val destination = when (selectedMethodTab) {
                                0 -> upiId
                                1 -> "$bankAccountNo ($bankIfsc)"
                                else -> paytmPhone
                            }

                            if (destination.isBlank()) {
                                errorMessage = "Please provide payment destination details"
                                return@Button
                            }

                            val (success, result) = onWithdraw(selectedAmount, method, destination)
                            if (success) {
                                receiptData = Pair(result, selectedAmount)
                                errorMessage = null
                            } else {
                                errorMessage = result
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonWhite,
                            contentColor = CanvasDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_withdrawal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Withdraw",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Withdraw ₹${selectedAmount.toInt()} Now",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Passbook / Room Database History Header with Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TRANSACTION PASSBOOK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("ALL", "EARNINGS", "CASHOUT").forEach { filter ->
                        val isSelected = selectedHistoryFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonMint.copy(alpha = 0.15f) else SlateSubCard)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonMint else SlateBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedHistoryFilter = filter }
                        ) {
                            Text(
                                text = filter,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) NeonMint else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        }

        // Passbook List Items
        val filteredList = transactions.filter { tx ->
            when (selectedHistoryFilter) {
                "EARNINGS" -> tx.type != "WITHDRAWAL"
                "CASHOUT" -> tx.type == "WITHDRAWAL"
                else -> true
            }
        }

        if (filteredList.isEmpty()) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No transactions found",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                        )
                    }
                }
            }
        } else {
            items(filteredList) { tx ->
                val isWithdrawal = tx.type == "WITHDRAWAL"
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isWithdrawal) Color(0xFFFF4D6D).copy(alpha = 0.15f) else NeonMint.copy(alpha = 0.15f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isWithdrawal) Icons.Default.CreditCard else Icons.Default.AccountBalanceWallet,
                                    contentDescription = tx.type,
                                    tint = if (isWithdrawal) Color(0xFFFF4D6D) else NeonMint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = tx.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = "${SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(tx.timestamp))} • ${tx.referenceId}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Text(
                            text = if (isWithdrawal) "-₹${String.format("%.2f", tx.amount)}" else "+₹${String.format("%.2f", tx.amount)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = if (isWithdrawal) Color(0xFFFF4D6D) else NeonMint,
                                fontWeight = FontWeight.Bold
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
