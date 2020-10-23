package com.bizinsights;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bizinsights.adapters.UserListAdapter;
import com.bizinsights.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    private void getDataResponse() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = getString(R.string.base_url) + getString(R.string.getUsersist);
        JSONObject params = new JSONObject();
        try {
            params.put("page_no", 2);
            params.put("page_record", 8);
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