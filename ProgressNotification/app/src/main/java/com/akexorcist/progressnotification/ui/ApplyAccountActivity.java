package com.akexorcist.progressnotification.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akexorcist.progressnotification.R;
import com.akexorcist.progressnotification.service.ApplyPremiumAccountService;

public class ApplyAccountActivity extends AppCompatActivity {
    private Button btnStartService;
    private EditText etUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_account);

        bindView();
        setupView();
        restoreArguments();
    }

    private void bindView() {
        btnStartService = findViewById(R.id.btn_start_service);
        etUserId = findViewById(R.id.et_user_id);
    }

    private void setupView() {
        btnStartService.setOnClickListener(onApplyPremiumAccountClick());
    }

    private void restoreArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String userId = bundle.getString(ApplyPremiumAccountService.EXTRA_USER_ID);
            if (!TextUtils.isEmpty(userId)) {
                setupUserId(userId);
            }
        }
    }

    private View.OnClickListener onApplyPremiumAccountClick() {
        return view -> {
            String userId = etUserId.getText().toString();
            if (TextUtils.isEmpty(userId)) {
                showUserIdInvalidMessage();
            } else {
                startApplyPremiumAccountService(userId);
                finish();
            }
        };
    }

    private void startApplyPremiumAccountService(String userId) {
        Intent intent = new Intent(ApplyAccountActivity.this, ApplyPremiumAccountService.class);
        intent.setAction(ApplyPremiumAccountService.ACTION_START);
        intent.putExtra(ApplyPremiumAccountService.EXTRA_USER_ID, userId);
        startService(intent);
    }

    private void setupUserId(String userId) {
        etUserId.setText(userId);
    }

    private void showUserIdInvalidMessage() {
        Toast.makeText(this, R.string.user_id_is_invalid, Toast.LENGTH_SHORT).show();
    }
}
