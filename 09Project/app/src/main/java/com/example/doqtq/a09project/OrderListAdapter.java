package com.example.doqtq.a09project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.internal.Utils;

/**
 * Created by doqtq on 2017-12-02.
 */

public class OrderListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<OrderListItem> orderListItemArrayList = new ArrayList<OrderListItem>() ;
    private String id;
    private Context context;
    private int idx,ordernum,ordernum2;
    // ListViewAdapter의 생성자
    public OrderListAdapter(int idx, int ordernum, int ordernum2, Context context) {
        orderListItemArrayList.clear();
        this.idx = idx;
        this.ordernum = ordernum;
        this.ordernum2 = ordernum2;
        this.context = context;
    }
    void clear(){
        orderListItemArrayList.clear();
    }
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return orderListItemArrayList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_order, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView2);
        TextView memoTextView = (TextView) convertView.findViewById(R.id.memoTextView);
        final Button stateBtn = (Button)convertView.findViewById(R.id.confirmBtn);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final OrderListItem orderListItem = orderListItemArrayList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(orderListItem.getName());
        memoTextView.setText("["+orderListItem.getMemo()+"]");
        id = orderListItem.getId();
        if(orderListItemArrayList.get(pos).getState()==0){
            //state == 0
            stateBtn.setText("입금대기");
            stateBtn.setBackgroundResource(R.drawable.btnimage);
            stateBtn.setTextColor(0xFFFFFFFF);
        } else if (orderListItemArrayList.get(pos).getState()==1) {
            //state == 1
            stateBtn.setText("입금완료");
            stateBtn.setTextColor(0xFF000000);
            stateBtn.setBackgroundResource(R.drawable.statebtn);
        } else if (orderListItemArrayList.get(pos).getState()==2) {
            //state == 2
            stateBtn.setText("운송장 번호 입력");
            stateBtn.setBackgroundResource(R.drawable.btnimage);
            stateBtn.setTextColor(0xFFFFFFFF);
        } else{
            //state == 3
            stateBtn.setText("운송장 번호 입력완료");
            stateBtn.setBackgroundResource(R.drawable.statebtn);
            stateBtn.setTextColor(0xFFFFFFFF);
        }
        stateBtn.invalidate();
        stateBtn.setTag(position);

        stateBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseresponse",response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                int stateDB = jsonResponse.getInt("state");
                                ordernum2 = jsonResponse.getInt("ordernum2");
                                Log.d("받은 State",stateDB+"");
                                orderListItemArrayList.get(pos).setState(stateDB);

                                OrderListActivity.orderNumBtn.setText("모집인원 : "+ordernum2+"/"+ordernum);
                            } else{
                                Toast.makeText(context, "마감",Toast.LENGTH_SHORT).show();
                                // 공구 인원 다 참
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        OrderListAdapter.this.notifyDataSetChanged();
                    }
                };
                String delivery="";
                OderListReqiestextends oderListReqiestextends = new OderListReqiestextends(orderListItemArrayList.get(pos).getId(), idx, orderListItemArrayList.get(pos).getName(),
                        orderListItemArrayList.get(pos).getState(),ordernum,ordernum2,responseListener);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(oderListReqiestextends);
                if(orderListItemArrayList.get(pos).getState()==2){
                    ((OrderListActivity)context).deliveryDialog(orderListItemArrayList.get(pos).getName(),orderListItemArrayList.get(pos).getId(),idx+"");
                }
                OrderListAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }


    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return orderListItemArrayList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String name, String memo, int state, String id, String delivery) {
        OrderListItem item = new OrderListItem();
        item.setName(name);
        item.setMemo(memo);
        item.setState(state);
        item.setId(id);
        item.setDelivery(delivery);
        orderListItemArrayList.add(item);
        Log.d("delivery",delivery);
        Log.d("주는 State",orderListItemArrayList.get(0).getId());
    }

    class OderListReqiestextends extends StringRequest {
        final static private String URL = "http://ekfms35.dothome.co.kr/DepositState.php";
        private Map<String, String> parameters;

        public OderListReqiestextends(String id,int idx,String name, int state, int ordernum, int ordernum2, Response.Listener<String> listener) {
            super(Request.Method.POST, URL, listener, null);
            parameters=new HashMap<>();
            Log.d("StateState", state+"");
            parameters.put("id",id+"");
            parameters.put("name",name+"");
            parameters.put("idx",idx+"");
            parameters.put("state",state+"");
            parameters.put("ordernum", ordernum+"");
            parameters.put("ordernum2", ordernum2+"");
            Log.d("OderListReqiestextends", state+" "+id+" "+ name + state+ " ");
        }
        @Override
        protected Map<String, String> getParams(){
            return parameters;
        }
    }

}

