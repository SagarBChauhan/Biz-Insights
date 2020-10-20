package com.bizinsights;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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
                break;
        }
    }
}