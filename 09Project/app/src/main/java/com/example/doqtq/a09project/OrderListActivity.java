package com.example.doqtq.a09project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderListActivity extends AppCompatActivity {

    private OrderListAdapter adapter;
    private ListView listView;
    private String idx;
    int ordernum2;
    private int ordernum;
    int state;
    public static TextView ordernumTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        listView = (ListView)findViewById(R.id.orderList);
        Intent intent = getIntent();
        idx = intent.getStringExtra("idx");
        ordernum = intent.getIntExtra("ordernum",0);
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
                    String name, memo,id;

                    adapter = new OrderListAdapter(Integer.parseInt(idx),ordernum,ordernum2);
                    while(count<jsonArray.length()) {
                        object = jsonArray.getJSONObject(count);
                        name = object.getString("uname");
                        memo = object.getString("memo");
                        state = object.getInt("state");
                        id = object.getString("uid");
                        adapter.addItem(name,memo,state,id);
                        count++;
                    }
                    ordernumTextView.setText(ordernum2+"/"+ordernum);
                    listView.setAdapter(adapter);
                } catch (Exception e){

                    Log.d("qtqtqt",e.getMessage());
                }
            }
        };

        orderListRequest orderListRequest = new orderListRequest(idx+"", responseListener);
        RequestQueue queue = Volley.newRequestQueue(OrderListActivity.this);
        queue.add(orderListRequest);

        ordernumTextView = (TextView)findViewById(R.id.orderTextView);



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



}

