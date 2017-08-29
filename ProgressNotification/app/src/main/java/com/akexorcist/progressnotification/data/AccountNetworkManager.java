package com.akexorcist.progressnotification.data;

import android.os.Handler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Akexorcist on 8/25/2017 AD.
 */

public class AccountNetworkManager {
    public static void applyPremiumAccount(final String userId, final ServiceCallback callback) {
        // Just mock the response
        new Handler().postDelayed(() -> {
                    if (((int) ((Math.random() * 10000)) % 2) != 0) {
                        if (callback != null) {
                            ApplyPremiumAccountResult result = new ApplyPremiumAccountResult(true);
                            callback.onApplyPremiumAccountResult(result);
                        }
                    } else {
                        if (callback != null) {
                            ApplyPremiumAccountException exception = new ApplyPremiumAccountException(userId);
                            callback.onApplyPremiumAccountException(exception);
                        }
                    }
                }, TimeUnit.SECONDS.toMillis(5)
        );

        if (callback != null) {
            callback.onApplyPremiumAccountStarted();
        }
    }

    public interface ServiceCallback {
        void onApplyPremiumAccountStarted();

        void onApplyPremiumAccountResult(ApplyPremiumAccountResult result);

        void onApplyPremiumAccountException(ApplyPremiumAccountException exception);
    }
}
