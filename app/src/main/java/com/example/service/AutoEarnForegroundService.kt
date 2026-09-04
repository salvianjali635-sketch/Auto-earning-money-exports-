package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.AutoEarnApp
import com.example.MainActivity
import com.example.R
import com.example.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AutoEarnForegroundService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    companion object {
        const val CHANNEL_ID = "autoearn_running_channel"
        const val NOTIFICATION_ID = 101
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                return START_NOT_STICKY
            }
            else -> {
                startForegroundServiceCompat()
                startEarningLoop()
            }
        }
        return START_STICKY
    }

    private fun startForegroundServiceCompat() {
        val notification = buildNotification("AutoEarn Engine Active", "Earning continuously in background...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun startEarningLoop() {
        serviceScope.launch {
            val app = application as? AutoEarnApp ?: return@launch
            val repo = app.repository
            val dao = app.database.transactionDao()

            var counter = 0
            while (isActive) {
                delay(5000L) // tick every 5 seconds
                counter++

                val isUptimeActive = repo.isAutoEarnActive.value
                val isBandwidthActive = repo.isBandwidthSharingActive.value
                val isAdActive = repo.isAutoAdActive.value && repo.isBackgroundAdActive.value

                var tickCredit = 0.0
                if (isUptimeActive) {
                    tickCredit += 0.025 // ~₹18/hr
                }

                if (isBandwidthActive) {
                    // simulate 0.8 MB shared every 5s
                    repo.addBandwidthShared(0.8)
                    tickCredit += 0.024 // ₹30/GB -> ~0.024 per ~0.8MB
                }

                if (isAdActive && counter % 6 == 0) { // every 30s background ad
                    repo.incrementAdsWatched()
                    tickCredit += repo.adRewardAmount.value
                }

                if (tickCredit > 0) {
                    repo.creditBalance(tickCredit)
                }

                // Update notification text periodically
                if (counter % 3 == 0) {
                    val balanceStr = String.format("%.2f", repo.balance.value)
                    val sharedStr = String.format("%.1f", repo.bandwidthSharedMbToday.value)
                    val adsStr = repo.adsWatchedToday.value.toString()
                    val text = "Balance: ₹$balanceStr | Net: ${sharedStr}MB | Ads: $adsStr"
                    val notification = buildNotification("AutoEarn Running", text)
                    val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    nm.notify(NOTIFICATION_ID, notification)
                }
            }
        }
    }

    private fun buildNotification(title: String, text: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "AutoEarn Background Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows live passive earning status and network metrics"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
