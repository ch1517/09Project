package com.example.doqtq.a09project;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signupBtn = (Button)findViewById(R.id.signupBtn);
        Intent intent = getIntent();
        final int mode = intent.getIntExtra("mode",0);
        final EditText idText = (EditText) findViewById(R.id.ideditText);
        final EditText pwText = (EditText)findViewById(R.id.pweditText);
        final Button loginButton = (Button)findViewById(R.id.loginButton);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = idText.getText().toString();
                final String pw = pwText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                final String id = jsonResponse.getString("id");
                                final String name = jsonResponse.getString("name");
                                final String phone = jsonResponse.getString("phone");
                                final String address = jsonResponse.getString("address");

                                if(mode == 2) // 공구 참여 화면
                                    finish();
                                else{
                                    if(mode == 1) {// 글쓰기 화면
                                        Intent intent = new Intent(LoginActivity.this, WritingActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                    }
                                }
                                LoginUser.loginUser = new UserInfo(id,name,phone,address);
                                LoginActivity.this.finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하였습니다.")
                                        .setNegativeButton("다시 시도",null)
                                        .create()
                                        .show();
                            }
                        } catch (Exception e){

                            Log.d("qtqtqt",e.getMessage());
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(id, pw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
