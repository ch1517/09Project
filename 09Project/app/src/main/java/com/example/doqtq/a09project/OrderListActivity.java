package com.example.doqtq.a09project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {
    private OrderListAdapter adapter;
    private ListView listView;
    private String idx;
    private Button deliveryBtn;
    int ordernum2;
    private int ordernum;
    int state;
    public static int count_fcm=0;
    public static Button orderNumBtn;
    String deliverystr;
    int delivery_count=1; // 운송장 번호 count;

    @Override
    protected void onResume() {
        super.onResume();
        orderListCallfunc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        listView = (ListView)findViewById(R.id.orderList);
        deliveryBtn = (Button)findViewById(R.id.deliveryBtn);
        orderNumBtn = (Button) findViewById(R.id.orderNumBtn);
        Intent intent = getIntent();
        idx = intent.getStringExtra("idx");
        ordernum = intent.getIntExtra("ordernum",0);

        orderNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                            } else {
                                Log.d("Errer","can't end");
                            }
                        } catch (Exception e){
                            Log.d("qtqtqt",e.getMessage());
                        }
                        Log.d("orderNumBtn","onclick!!");
                        orderListCallfunc();
                        adapter.notifyDataSetChanged();
                        adapter.notifyDataSetInvalidated();
                    }

                };

                OrderEndRequest orderListRequest = new OrderEndRequest(idx+"", ordernum+"",ordernum2+"", responseListener);
                RequestQueue queue = Volley.newRequestQueue(OrderListActivity.this);
                queue.add(orderListRequest);
                if(ordernum>ordernum2)
                    Toast.makeText(getApplicationContext(),"인원이 다 모이지 않았어요 :(",Toast.LENGTH_SHORT).show();

            }
        });
        deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deliveryBtn.getText().toString().equals("배송 시작")){
                    new AlertDialog.Builder(OrderListActivity.this)
                            .setTitle("배송 알림을 시작할까요?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("responseOrder",response);
                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        Log.d("qwerqwer",response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            dialog.dismiss();
                                            Toast.makeText(OrderListActivity.this,"공지 완료",Toast.LENGTH_LONG).show();
                                        }else{

                                        }
                                    } catch (Exception e) {
                                        Log.d("responseOrder",e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            };
                            DeliveryRequest deliveryRequest = new DeliveryRequest(idx,
                                    "",delivery_count+"", responseListener);
                            RequestQueue queue = Volley.newRequestQueue(OrderListActivity.this);
                            queue.add(deliveryRequest);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                            .create()
                            .show();
                } else {
                    participationDialog();
                }
            }
        }) ;


    }
    void orderListCallfunc(){
        //  서버로 부터 리스트 값 받아오기
        Log.d("OrderListActivity","orderListCallfunc");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ididid",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    JSONObject object = jsonArray.getJSONObject(0);
                    ordernum2 = Integer.parseInt(object.getString("ordernum2"));
                    int count=1;
                    String name, memo,id, delivery;

                    adapter = new OrderListAdapter(Integer.parseInt(idx),ordernum,ordernum2,getBaseContext());
                    int i=0;
                    while(count<jsonArray.length()) {
                        object = jsonArray.getJSONObject(count);
                        name = object.getString("uname");
                        memo = object.getString("memo");
                        state = object.getInt("state");
                        id = object.getString("uid");
                        delivery = object.getString("delivery");
                        if (delivery != "null" && state==2){
                            i++;
                        }
                        adapter.addItem(name,memo,state,id,delivery);
                        count++;
                    }
                    Log.d("oooooo",i+"    "+ordernum2);
                    if(i==ordernum2){ // 1이면 공지, 0이면 배송시작
                        Log.d("ㅇㅇㅇ 맞음","ㅇㅇㅇ");
                        if(ordernum2==0){

                        }
                        else {
                            deliveryBtn.setText("배송 시작");

                            orderNumBtn.setBackgroundResource(R.drawable.statebtn);
                            orderNumBtn.setTextColor(0xFF000000);

                            deliveryBtn.setBackgroundResource(R.drawable.btnimage);
                            deliveryBtn.setTextColor(0xFFFFFFFF);

                            delivery_count = 0;
                        }
                    }
                    if(ordernum!=ordernum2){
                        orderNumBtn.setText("모집인원 : "+ordernum2+"/"+ordernum);
                    } else {
                        orderNumBtn.setText("모집완료("+ordernum+")");
                    }
                    listView.setAdapter(adapter);
                    Log.d("adapter",adapter.getItem(0)+"");
                } catch (Exception e){

                    Log.d("qtqtqt",e.getMessage());
                }
            }

        };

        orderListRequest orderListRequest = new orderListRequest(idx+"", responseListener);
        RequestQueue queue = Volley.newRequestQueue(OrderListActivity.this);
        queue.add(orderListRequest);

    }
    class OrderEndRequest extends StringRequest {
        final static private String URL = "http://ekfms35.dothome.co.kr/OrderEnd.php";
        private Map<String, String> parameters;
        public OrderEndRequest(String idx, String ordernum, String ordernum2, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);
            parameters = new HashMap<>();
            parameters.put("idx", idx);
            parameters.put("ordernum", ordernum);
            parameters.put("ordernum2",ordernum2);
        }

        @Override
        protected Map<String, String> getParams() {
            return parameters;
        }
    }
    class orderListRequest extends StringRequest {

        final static private String URL = "http://ekfms35.dothome.co.kr/OrderState.php";
        private Map<String, String> parameters;

        public orderListRequest(String idx, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);
            parameters = new HashMap<>();
            parameters.put("idx", idx);
        }

        @Override
        protected Map<String, String> getParams() {
            return parameters;
        }
    }
    void participationDialog(){
        final Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_delivery,(ViewGroup)findViewById(R.id.layout_root));

        final AlertDialog.Builder aDialog = new AlertDialog.Builder(OrderListActivity.this);
        aDialog.setTitle("공지");
        aDialog.setView(layout);
        aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EditText memoEditText = layout.findViewById(R.id.memoeditText);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseOrder",response);
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("qwerqwer",response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                dialog.dismiss();
                                Toast.makeText(OrderListActivity.this,"공지 완료",Toast.LENGTH_LONG).show();
                            }else{

                            }
                        } catch (Exception e) {
                            Log.d("responseOrder",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                };
                DeliveryRequest deliveryRequest = new DeliveryRequest(idx,
                        memoEditText.getText().toString(),delivery_count+"", responseListener);
                RequestQueue queue = Volley.newRequestQueue(OrderListActivity.this);
                queue.add(deliveryRequest);
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
    public void deliveryDialog(final String name,final String id, final String idx){
        final Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_transport_number,(ViewGroup)findViewById(R.id.layout_root));

        TextView nameTextView = (TextView)layout.findViewById(R.id.nameTextView2);
        Log.d("namename",name);
        nameTextView.setText(name);

        final AlertDialog.Builder aDialog = new AlertDialog.Builder(OrderListActivity.this);

        aDialog.setTitle("운송장 번호 입력");
        aDialog.setView(layout);
        aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                EditText deliveryEditText = layout.findViewById(R.id.deliveryEditText);
                deliverystr = deliveryEditText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseresponse",response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(OrderListActivity.this,"운송장번호 등록 완료",Toast.LENGTH_SHORT).show();
                            }else{

                            }
                        } catch (Exception e) {
                            Log.d("FCM",e.getMessage());
                        }
                        orderListCallfunc();
                        adapter.notifyDataSetChanged();
                    }
                };
                TransportNumberRequest transportNumberRequest = new TransportNumberRequest(id, name, idx, deliverystr,responseListener);
                RequestQueue queue = Volley.newRequestQueue(OrderListActivity.this);
                queue.add(transportNumberRequest);

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

    class TransportNumberRequest extends StringRequest {
        final static private String URL = "http://ekfms35.dothome.co.kr/TransportNumber.php";
        private Map<String, String> parameters;

        public TransportNumberRequest(String id, String name, String idx, String delivery,Response.Listener<String> listener) {
            super(Request.Method.POST, URL, listener, null);
            parameters=new HashMap<>();
            parameters.put("id",id);
            parameters.put("name",name);
            parameters.put("idx",idx);
            parameters.put("delivery",delivery);
        }
        @Override
        protected Map<String, String> getParams(){
            return parameters;
        }
    }
}