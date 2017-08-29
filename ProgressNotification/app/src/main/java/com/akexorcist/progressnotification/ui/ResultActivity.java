package com.akexorcist.progressnotification.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.akexorcist.progressnotification.R;
import com.akexorcist.progressnotification.data.ApplyPremiumAccountResult;
import com.akexorcist.progressnotification.service.ApplyPremiumAccountService;

public class ResultActivity extends AppCompatActivity {
    private TextView tvUserId;
    private TextView tvAccountStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        bindView();
        setupView();
        restoreArguments();
    }

    private void bindView() {
        tvUserId = findViewById(R.id.tv_user_id);
        tvAccountStatus = findViewById(R.id.tv_account_status);
    }

    private void setupView() {
    }

    private void restoreArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String userId = bundle.getString(ApplyPremiumAccountService.EXTRA_USER_ID);
            ApplyPremiumAccountResult result = bundle.getParcelable(ApplyPremiumAccountService.EXTRA_APPLY_PREMIUM_ACCOUNT_RESULT);
            setUserId(userId);
            setApplyPremiumAccountResult(result != null && result.isActive());
        }
    }

    private void setUserId(String userId) {
        tvUserId.setText(userId);
    }

    private void setApplyPremiumAccountResult(boolean isActive) {
        int messageResId = isActive ? R.string.your_status_is_active : R.string.your_status_is_inactive;
        tvAccountStatus.setText(messageResId);
    }
}
