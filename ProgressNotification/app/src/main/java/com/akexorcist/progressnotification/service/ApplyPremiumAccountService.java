package com.akexorcist.progressnotification.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.akexorcist.progressnotification.R;
import com.akexorcist.progressnotification.data.AccountNetworkManager;
import com.akexorcist.progressnotification.data.ApplyPremiumAccountException;
import com.akexorcist.progressnotification.data.ApplyPremiumAccountResult;
import com.akexorcist.progressnotification.ui.ApplyAccountActivity;
import com.akexorcist.progressnotification.ui.ResultActivity;

public class ApplyPremiumAccountService extends Service {
    public static final int NOTIFICATION_ID_PROGRESS = 1512;
    public static final int NOTIFICATION_ID_RESULT = 1513;

    public static final String GROUP_ID = "content_loading";
    public static final String ACTION_START = "com.akexorcist.service.start";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT = "apply_premium_account_result";

    public ApplyPremiumAccountService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_START.equalsIgnoreCase(action)) {
                String userId = intent.getStringExtra(EXTRA_USER_ID);
                callWebService(userId);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void callWebService(final String userId) {
        AccountNetworkManager.applyPremiumAccount(userId, new AccountNetworkManager.ServiceCallback() {
            @Override
            public void onApplyPremiumAccountStarted() {
                showProgressNotification();
            }

            @Override
            public void onApplyPremiumAccountResult(ApplyPremiumAccountResult result) {
                stop();
                if (isAppForeground()) {
                    openSuccessResultScreen(userId, result);
                } else {
                    showServiceSucceedNotification(userId, result);
                }
            }

            @Override
            public void onApplyPremiumAccountException(ApplyPremiumAccountException exception) {
                stop();
                if (isAppForeground()) {
                    openFailureResultScreen(userId);
                    showFailureResultMessage();
                } else {
                    showServiceFailedNotification(exception.getUserId());
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showServiceSucceedNotification(String userId, ApplyPremiumAccountResult applyPremiumAccountResult) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT, applyPremiumAccountResult);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), GROUP_ID)
                .setSmallIcon(R.drawable.ic_request_done_small)
                .setContentTitle(getString(R.string.notification_your_request_is_done))
                .setContentText(getString(R.string.notification_click_here_to_resume_the_app))
                .setTicker(getString(R.string.notification_registered))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_RESULT, notification);
    }

    private void showServiceFailedNotification(String userId) {
        Intent intent = new Intent(this, ApplyAccountActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), GROUP_ID)
                .setSmallIcon(R.drawable.ic_request_failed_small)
                .setContentTitle(getString(R.string.notification_your_request_has_been_denied))
                .setContentText(getString(R.string.notification_click_here_to_try_again_in_the_app))
                .setTicker(getString(R.string.notification_request_failed))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_RESULT, notification);
    }

    private void showProgressNotification() {
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), GROUP_ID)
                .setSmallIcon(R.drawable.ic_apply_request_small)
                .setContentText(getString(R.string.notification_please_wait_for_awhile))
                .setContentTitle(getString(R.string.notification_submitting_your_request))
                .setTicker(getString(R.string.notification_submitting_your_request))
                .setProgress(0, 100, true)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        startForeground(NOTIFICATION_ID_PROGRESS, notification);
    }

    private void stop() {
        stopForeground(true);
    }

    private void openSuccessResultScreen(String userId, ApplyPremiumAccountResult result) {
        Intent intent = new Intent(ApplyPremiumAccountService.this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT, result);
        startActivity(intent);
    }

    private void openFailureResultScreen(String userId) {
        Intent intent = new Intent(ApplyPremiumAccountService.this, ApplyAccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    private void showFailureResultMessage() {
        Toast.makeText(ApplyPremiumAccountService.this, R.string.your_request_have_been_denied, Toast.LENGTH_SHORT).show();
    }

    private boolean isAppForeground() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
    }
}
