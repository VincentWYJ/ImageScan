package com.dylan_wang.autostart;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        finish();

        Intent intent = new Intent(getApplicationContext(), Service1.class);
        startService(intent);

    }
}
