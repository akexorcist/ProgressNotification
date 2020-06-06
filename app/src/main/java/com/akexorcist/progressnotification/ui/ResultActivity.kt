package com.akexorcist.progressnotification.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.progressnotification.R
import com.akexorcist.progressnotification.data.ApplyPremiumAccountResult
import com.akexorcist.progressnotification.service.ApplyPremiumAccountService
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        setupView()
        restoreArguments()
    }

    private fun setupView() {}

    private fun restoreArguments() {
        val bundle = intent.extras
        if (bundle != null) {
            val userId: String? = bundle.getString(ApplyPremiumAccountService.EXTRA_USER_ID)
            val result: ApplyPremiumAccountResult? = bundle.getParcelable(ApplyPremiumAccountService.EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT)
            setUserId(userId)
            setApplyPremiumAccountResult(result != null && result.isActive)
        }
    }

    private fun setUserId(userId: String?) {
        tvUserId?.text = userId
    }

    private fun setApplyPremiumAccountResult(isActive: Boolean) {
        val messageResId = if (isActive) R.string.your_status_is_active else R.string.your_status_is_inactive
        tvAccountStatus?.setText(messageResId)
    }
}