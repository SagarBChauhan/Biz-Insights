package com.bizinsights;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.bizinsights.utility.FormValidation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisterActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_first_name)
    AppCompatEditText et_first_name;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_last_name)
    AppCompatEditText et_last_name;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_address)
    AppCompatEditText et_address;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_email)
    AppCompatEditText et_email;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_mobile)
    AppCompatEditText et_mobile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    AppCompatEditText et_password;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_confirm_password)
    AppCompatEditText et_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.tv_login, R.id.btn_register})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_register:
                if (isFormValidate()) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("first_name", Objects.requireNonNull(et_first_name.getText()).toString().trim());
                        params.put("last_name", Objects.requireNonNull(et_last_name.getText()).toString().trim());
                        params.put("address", Objects.requireNonNull(et_address.getText()).toString().trim());
                        params.put("password", Objects.requireNonNull(et_password.getText()).toString().trim());
                        params.put("email_id", Objects.requireNonNull(et_email.getText()).toString().trim());
                        params.put("mobile_no", Objects.requireNonNull(et_mobile.getText()).toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StringEntity entity = null;
                    try {
                        entity = new StringEntity(params.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    client.post(this, getAbsoluteUrl(getString(R.string.register)), entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Logger.d("Status Code: " + "\nResponse: " + Arrays.toString(responseBody) + "\nHeaders: " + Arrays.toString(headers));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Logger.e("Status Code: " + statusCode + "\nMessage: " + error.getMessage() + "\nResponse: " + Arrays.toString(responseBody));
                        }
                    });
                } else {
                    Toast.makeText(this, getString(R.string.msg_registration_failed), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return getString(R.string.base_url) + relativeUrl;
    }

    private boolean isFormValidate() {
        if (new FormValidation().checkEmptyEditText(et_first_name)) {
            et_first_name.setError(getString(R.string.msg_first_name_empty));
            et_first_name.requestFocus();
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_last_name)) {
            et_last_name.setError(getString(R.string.msg_last_name_empty));
            et_last_name.requestFocus();
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_address)) {
            et_address.setError(getString(R.string.msg_address_empty));
            et_address.requestFocus();
            return false;
        }
        if (new FormValidation().checkEmptyEditText(et_email)) {
            et_email.setError(getString(R.string.msg_email_empty));
            et_email.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkEmail(et_email)) {
                et_email.setError(getString(R.string.msg_email_invalid));
                et_email.requestFocus();
                return false;
            }
        }
        if (new FormValidation().checkEmptyEditText(et_mobile)) {
            et_mobile.setError(getString(R.string.msg_mobile_no_empty));
            et_mobile.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkMobileNumber(et_mobile)) {
                et_mobile.setError(getString(R.string.msg_mobile_no_invalid));
                et_mobile.requestFocus();
                return false;
            }
        }
        if (new FormValidation().checkEmptyEditText(et_password)) {
            et_password.setError(getString(R.string.msg_password_empty));
            et_password.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkPassword(et_password)) {
                et_password.setError(getString(R.string.msg_password_invalid));
                et_password.requestFocus();
                return false;
            }
        }
        if (new FormValidation().checkEmptyEditText(et_confirm_password)) {
            et_confirm_password.setError(getString(R.string.msg_confirm_password_empty));
            et_confirm_password.requestFocus();
            return false;
        } else {
            if (!new FormValidation().checkPassword(et_confirm_password)) {
                et_confirm_password.setError(getString(R.string.msg_confirm_password_invalid));
                et_confirm_password.requestFocus();
                return false;
            }
        }
        if (!new FormValidation().checkConfirmPassword(et_password, et_confirm_password)) {
            et_confirm_password.setError(getString(R.string.msg_password_not_matched));
            et_confirm_password.requestFocus();
            return false;
        }
        return true;
    }
}