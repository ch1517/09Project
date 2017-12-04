package com.example.doqtq.a09project;

import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONObject;

public class WritingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        final EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
        final EditText priceEditText = (EditText)findViewById(R.id.priceEditText);
        final EditText orderNumEditText = (EditText)findViewById(R.id.orderNumEditText);
        final EditText contentEditext = (EditText)findViewById(R.id.contenteditextView);

        Button addButton = (Button)findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleEditText.getText().toString();
                final String price = priceEditText.getText().toString();
                final String ordernum = orderNumEditText.getText().toString();
                final String content = contentEditext.getText().toString();
                final String userfile = "jjj";


                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            Log.d("success",success+"");
                            if(success) {
                                Log.d("ddd","성공!");
                                Log.d("sss",response);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.d("error",e.getMessage());
                        }
                    }
                };

                WritingRequest writingRequest = new WritingRequest(LoginUser.loginUser.getId(),title, price,ordernum,content,userfile, responseListener);
                RequestQueue queue = Volley.newRequestQueue(WritingActivity.this);
                queue.add(writingRequest);
                finish();
            }


        });
    }
}
