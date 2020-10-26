package com.bizinsights;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bizinsights.adapters.UserListAdapter;
import com.bizinsights.models.User;
import com.bizinsights.utility.Globals;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

@SuppressLint("NonConstantResourceId")
public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    UserListAdapter userListAdapter;
    StringEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setupToolbar();
        getDataResponse();
    }

    @OnClick({R.id.toolbar_btn_logout})
    public void onClickAction(View view) {
        if (view.getId() == R.id.toolbar_btn_logout) {
            new AlertDialog.Builder(DashboardActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure want logout?")
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(getString(R.string.logout), (dialog, which) -> {
                        Globals globals = (Globals) getApplicationContext();
                        globals.setLoginData(null);
                        if (globals.getLoginData() == null) {
                            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void getDataResponse() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = getString(R.string.base_url) + getString(R.string.getUsersist);
        JSONObject params = new JSONObject();
        try {
            params.put("page_no", 1);
            params.put("page_record", 50);
            entity = new StringEntity(params.toString());
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(DashboardActivity.this, url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Logger.json(response.toString());
                User user = new Gson().fromJson(response.toString(), User.class);
                userListAdapter = new UserListAdapter(user.data.rows);
                recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
                recyclerView.setAdapter(userListAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Logger.e("ERROR: " + errorResponse.toString());
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar_title.setText(R.string.user_list);
    }
}