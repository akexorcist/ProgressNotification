package com.akexorcist.progressnotification.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.akexorcist.progressnotification.R
import com.akexorcist.progressnotification.service.ApplyPremiumAccountService
import kotlinx.android.synthetic.main.activity_apply_account.*

class ApplyAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_account)

        setupView()
        restoreArguments()
    }

    private fun setupView() {
        btnStartService?.setOnClickListener { onApplyPremiumAccountClick() }
    }

    private fun restoreArguments() {
        val bundle = intent.extras
        if (bundle != null) {
            val userId = bundle.getString(ApplyPremiumAccountService.EXTRA_USER_ID)
            if (!TextUtils.isEmpty(userId)) {
                setupUserId(userId)
            }
        }
    }

    private fun onApplyPremiumAccountClick() {
        val userId = etUserId?.text.toString()
        if (TextUtils.isEmpty(userId)) {
            showUserIdInvalidMessage()
        } else {
            startApplyPremiumAccountService(userId)
            finish()
        }
    }

    private fun startApplyPremiumAccountService(userId: String?) {
        val intent = Intent(this, ApplyPremiumAccountService::class.java)
        intent.action = ApplyPremiumAccountService.ACTION_START
        intent.putExtra(ApplyPremiumAccountService.EXTRA_USER_ID, userId)
        startService(intent)
    }

    private fun setupUserId(userId: String?) {
        etUserId?.setText(userId)
    }

    private fun showUserIdInvalidMessage() {
        Toast.makeText(this, R.string.user_id_is_invalid, Toast.LENGTH_SHORT).show()
    }
}
