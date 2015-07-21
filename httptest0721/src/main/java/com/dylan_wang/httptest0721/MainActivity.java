package com.dylan_wang.httptest0721;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView success;
    String name;
    String phone;
    EditText name_view;
    EditText phone_view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_view=(EditText) findViewById(R.id.name);
        phone_view=(EditText) findViewById(R.id.phone);
        success=(TextView) findViewById(R.id.success);
    }

    public void getInfo(){
        name=name_view.getText().toString();
        phone=phone_view.getText().toString();
    }

    public void get_save(View v){
        getInfo();
        success.setText(service.get_save(name,phone));
    }

    public void post_save(View v){
        getInfo();
        success.setText(service.post_save(name,phone));
    }

    public void httpclient_postsave(View v){
        getInfo();
        success.setText(service.httpclient_postsave(name,phone));
    }

    public void httpclient_getsave(View v){
        getInfo();
        success.setText(service.httpclient_getsave(name,phone));
    }
}