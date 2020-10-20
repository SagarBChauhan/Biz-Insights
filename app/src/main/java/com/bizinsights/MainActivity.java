package com.bizinsights;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.bizinsights.utility.FormValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.et_username)
    AppCompatEditText et_username;
    @BindView(R.id.et_password)
    AppCompatEditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_register, R.id.btn_login})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_login:
                if (isFormValidate()) {
                    Toast.makeText(this, getString(R.string.msg_login_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.msg_login_failed), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isFormValidate() {
        if (new FormValidation().checkEmptyEditText(et_username)) {
            et_username.requestFocus();
            et_username.setError(getString(R.string.msg_username_empty));
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_password)) {
            et_password.requestFocus();
            et_password.setError(getString(R.string.msg_password_empty));
            return false;
        } else {
            if (!new FormValidation().checkPassword(et_password)) {
                et_password.requestFocus();
                et_password.setError(getString(R.string.password_requirement));
                return false;
            }
        }
        return true;
    }
}