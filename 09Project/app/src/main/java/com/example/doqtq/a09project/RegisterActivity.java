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

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText nameeditText = (EditText)findViewById(R.id.nameeditText);
        final EditText ideditText = (EditText)findViewById(R.id.ideditText);
        final EditText pweditText = (EditText)findViewById(R.id.pweditText);
        final EditText addresseditText = (EditText)findViewById(R.id.addresseditText);
        final EditText phoneNumeditText = (EditText)findViewById(R.id.phoneNumeditText);

        final EditText confirmPweditText = (EditText)findViewById(R.id.confirmPweditText);

        Button registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ideditText.getText().toString();
                String pw = pweditText.getText().toString();
                String name = nameeditText.getText().toString();
                String phone = phoneNumeditText.getText().toString();
                String address = addresseditText.getText().toString();

                String confirmPw = confirmPweditText.getText().toString();
                Log.d("id",id);
                if(!confirmPw.equals(pw)){
                    Toast.makeText(getApplicationContext(),"비밀번호와 비밀번호확인 불일치",Toast.LENGTH_SHORT).show();
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인",null)
                                        .create()
                                        .show();
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원 등록에 실패했습니다.")
                                        .setPositiveButton("확인",null)
                                        .create()
                                        .show();

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(id,pw,name,phone,address,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });



    }
}
