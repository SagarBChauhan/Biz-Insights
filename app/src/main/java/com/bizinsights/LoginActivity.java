package com.bizinsights;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.bizinsights.models.LoginDataModel;
import com.bizinsights.utility.FormValidation;
import com.bizinsights.utility.Globals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_username)
    AppCompatEditText et_username;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_password)
    AppCompatEditText et_password;

    LoginDataModel loginDataModel;
    Gson gson;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        ButterKnife.bind(this);

        loginDataModel = new LoginDataModel();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        globals = (Globals) getApplicationContext();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.tv_register, R.id.btn_login})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_login:
                if (isFormValidate()) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    JSONObject params = new JSONObject();
                    try {
                        params.put("username", Objects.requireNonNull(et_username.getText()).toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        params.put("password", Objects.requireNonNull(et_password.getText()).toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    StringEntity entity = null;
                    try {
                        entity = new StringEntity(params.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    client.post(this, getAbsoluteUrl(getString(R.string.login)), entity, "application/json", new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Logger.json(response.toString());
                            loginDataModel = gson.fromJson(response.toString(), LoginDataModel.class);
                            globals.setLoginData(loginDataModel);
                            if (globals.getLoginData() != null) {
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Logger.e("Login", "" + errorResponse.toString());
                            Logger.e("Login", "status code " + statusCode);
                        }
                    });
                } else {
                    Toast.makeText(this, getString(R.string.msg_login_failed), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return getString(R.string.base_url) + relativeUrl;
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