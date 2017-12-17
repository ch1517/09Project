package com.example.doqtq.a09project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent messageListIntent = new Intent(MessageActivity.this, MessageListActivity.class);
        startActivity(messageListIntent);
    }
}
