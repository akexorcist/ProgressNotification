package com.akexorcist.progressnotification.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.Toast
import com.akexorcist.progressnotification.R
import com.akexorcist.progressnotification.data.AccountNetworkManager
import com.akexorcist.progressnotification.data.ApplyPremiumAccountException
import com.akexorcist.progressnotification.data.ApplyPremiumAccountResult
import com.akexorcist.progressnotification.ui.ApplyAccountActivity
import com.akexorcist.progressnotification.ui.ResultActivity

class ApplyPremiumAccountService : Service() {
    companion object {
        const val NOTIFICATION_ID_PROGRESS = 1512
        const val NOTIFICATION_ID_RESULT = 1513

        const val CHANNEL_ID = "content_loading"
        const val CHANNEL_NAME = "Content Loading"
        const val ACTION_START = "com.akexorcist.service.start"
        const val EXTRA_USER_ID = "user_id"
        const val EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT = "apply_premium_account_result"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (ACTION_START == action) {
                val userId = intent.getStringExtra(EXTRA_USER_ID)
                callWebService(userId)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationGroupIfNeed()
    }

    override fun onBind(intent: Intent): IBinder? = null


    private fun callWebService(userId: String) {
        AccountNetworkManager.applyPremiumAccount(userId, object : AccountNetworkManager.ServiceCallback {
            override fun onApplyPremiumAccountStarted() {
                showProgressNotification()
            }

            override fun onApplyPremiumAccountResult(result: ApplyPremiumAccountResult) {
                stop()
                if (isAppForeground()) {
                    openSuccessResultScreen(userId, result)
                } else {
                    showServiceSucceedNotification(userId, result)
                }
            }

            override fun onApplyPremiumAccountException(exception: ApplyPremiumAccountException) {
                stop()
                if (isAppForeground()) {
                    openFailureResultScreen(userId)
                    showFailureResultMessage()
                } else {
                    showServiceFailedNotification(exception.userId)
                }
            }
        })
    }

    private fun showServiceSucceedNotification(userId: String?, applyPremiumAccountResult: ApplyPremiumAccountResult?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(EXTRA_USER_ID, userId)
        intent.putExtra(EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT, applyPremiumAccountResult)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_request_done_small)
                .setContentTitle(getString(R.string.notification_your_request_is_done))
                .setContentText(getString(R.string.notification_click_here_to_resume_the_app))
                .setTicker(getString(R.string.notification_registered))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_RESULT, notification)
    }

    private fun showServiceFailedNotification(userId: String?) {
        val intent = Intent(this, ApplyAccountActivity::class.java)
        intent.putExtra(EXTRA_USER_ID, userId)
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_request_failed_small)
                .setContentTitle(getString(R.string.notification_your_request_has_been_denied))
                .setContentText(getString(R.string.notification_click_here_to_try_again_in_the_app))
                .setTicker(getString(R.string.notification_request_failed))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_RESULT, notification)
    }

    private fun showProgressNotification() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_apply_request_small)
                .setContentText(getString(R.string.notification_please_wait_for_awhile))
                .setContentTitle(getString(R.string.notification_submitting_your_request))
                .setTicker(getString(R.string.notification_submitting_your_request))
                .setProgress(0, 100, true)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        startForeground(NOTIFICATION_ID_PROGRESS, notification)
    }

    private fun stop() {
        stopForeground(true)
    }

    private fun openSuccessResultScreen(userId: String?, result: ApplyPremiumAccountResult?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(EXTRA_USER_ID, userId)
        intent.putExtra(EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT, result)
        startActivity(intent)
    }

    private fun openFailureResultScreen(userId: String?) {
        val intent = Intent(this, ApplyAccountActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(EXTRA_USER_ID, userId)
        startActivity(intent)
    }

    private fun showFailureResultMessage() {
        Toast.makeText(this, R.string.your_request_have_been_denied, Toast.LENGTH_SHORT).show()
    }

    private fun isAppForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
    }

    private fun createNotificationGroupIfNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                channel.enableVibration(true)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
