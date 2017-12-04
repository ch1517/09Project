package com.example.doqtq.a09project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class ContentActivity extends AppCompatActivity {
    Board board;
    int writerMode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        final Intent intent = getIntent();

        board = (Board)intent.getSerializableExtra("board");
        writerMode = intent.getIntExtra("writerMode",0);

        ImageView photoImageView = findViewById(R.id.photoimageView);
        TextView titleTextView = findViewById(R.id.titletextView1);
        TextView priceTextView = findViewById(R.id.priceTextView1);
        TextView idTextView = findViewById(R.id.idTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView contentTextView = findViewById(R.id.contentTextView);

        Glide.with(this).load("http://ekfms35.dothome.co.kr"+board.getPhoto()).into(photoImageView);

        Button participationBtn = (Button) findViewById(R.id.participationBtn); // 공구참여 버튼 0
        Button messageBtn = (Button)findViewById(R.id.messageBtn); // 메세지 버튼 0
        Button orderListBtn = (Button)findViewById(R.id.orderListBtn); // 진행현황 버튼 1

        if(writerMode==0){
            participationBtn.setVisibility(View.VISIBLE);
            participationBtn.bringToFront();
            messageBtn.setVisibility(View.VISIBLE);
            messageBtn.bringToFront();
            orderListBtn.setVisibility(View.GONE);
        } else{
            participationBtn.setEnabled(false);
            // Viewgone하면 스크롤뷰가 밑으로 내려와서 setEnabled 사용
            // 어차피 orderListBtn이 가림
            messageBtn.setVisibility(View.GONE);
            orderListBtn.bringToFront();
            orderListBtn.setVisibility(View.VISIBLE);
        }


        titleTextView.setText(board.getTitle());
        priceTextView.setText(board.getPrice()+"");
        dateTextView.setText(board.getDate());
        idTextView.setText(board.getId());
        contentTextView.setText(board.getContent());

        participationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginUser.loginUser==null){
                    Intent loginIntent = new Intent(ContentActivity.this, LoginActivity.class);
                    loginIntent.putExtra("mode",2); // 공구 모드
                    startActivity(loginIntent);
                    finish();
                }else{
                    participationDialog();
                }
            }
        });

        orderListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderListIntent = new Intent(ContentActivity.this, OrderListActivity.class);
                orderListIntent.putExtra("idx",board.getIdx());
                orderListIntent.putExtra("ordernum",board.getOrdernum());
                startActivity(orderListIntent);
            }
        });
    }
    void participationDialog(){
        final Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_participation,(ViewGroup)findViewById(R.id.layout_root));

        final AlertDialog.Builder aDialog = new AlertDialog.Builder(ContentActivity.this);
        aDialog.setTitle("공구 참여");
        aDialog.setView(layout);
        aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EditText nameEditText = layout.findViewById(R.id.nameInputeditText);
                EditText memoEditText = layout.findViewById(R.id.memoeditText);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                dialog.dismiss();
                                Toast.makeText(ContentActivity.this,"공구 참여 완료",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ParticipationRequest participationRequest = new ParticipationRequest(
                        nameEditText.getText().toString(),LoginUser.loginUser.getId(),board.getIdx(),
                        memoEditText.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(ContentActivity.this);
                queue.add(participationRequest);
            }
        });
        aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = aDialog.create();
        ad.show();
    }
}
