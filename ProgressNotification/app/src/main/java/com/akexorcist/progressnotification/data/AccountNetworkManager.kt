package com.akexorcist.progressnotification.data

import android.os.Handler

import java.util.concurrent.TimeUnit

/**
 * Created by Akexorcist on 8/25/2017 AD.
 */

object AccountNetworkManager {
    fun applyPremiumAccount(userId: String, callback: ServiceCallback?) {
        // Just mock the response
        Handler().postDelayed({
            if ((Math.random() * 10000).toInt() % 2 != 0) {
                callback?.onApplyPremiumAccountResult(ApplyPremiumAccountResult(true))
            } else {
                callback?.onApplyPremiumAccountException(ApplyPremiumAccountException(userId))
            }
        }, TimeUnit.SECONDS.toMillis(5))
        callback?.onApplyPremiumAccountStarted()
    }

    interface ServiceCallback {
        fun onApplyPremiumAccountStarted()

        fun onApplyPremiumAccountResult(result: ApplyPremiumAccountResult)

        fun onApplyPremiumAccountException(exception: ApplyPremiumAccountException)
    }
}
