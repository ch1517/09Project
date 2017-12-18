package com.example.doqtq.a09project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class MessageActivity extends AppCompatActivity {
    String Tag = "MessageActivity";
    private ListView listView;
    private EditText editText;
    private Button sendButton;
    private String userName;
    private MessageAdapter adapter;
    private String toUserID;
    private ArrayList<ChatData> sendArrayList = new ArrayList<>();
    private ArrayList<ChatData> reciveArrayList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();
        toUserID = intent.getStringExtra("toUserId");
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText);
        sendButton = (Button) findViewById(R.id.addBtn);
        setTitle(toUserID);
        adapter = new MessageAdapter();

        listView.setAdapter(adapter);

        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick","onclick");
                ChatData chatData = new ChatData(LoginUser.loginUser.getId(), editText.getText().toString(), toUserID, System.currentTimeMillis());  // 유저 이름과 메세지로 chatData 만들기
                sendArrayList.add(chatData);
                databaseReference.child(LoginUser.loginUser.getId()).child(toUserID).push().setValue(chatData);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                editText.setText("");
            }
        });

        databaseReference.child(LoginUser.loginUser.getId()).child(toUserID).addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                adapter.addItem(chatData);  // adapter에 추가합니다.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        databaseReference.child(toUserID).child(LoginUser.loginUser.getId()).addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                adapter.addItem(chatData);  // adapter에 추가합니다.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent messageListIntent = new Intent(MessageActivity.this, MessageListActivity.class);
        startActivity(messageListIntent);
        finish();
    }
}

class MessageAdapter extends BaseAdapter {
    String Tag = "MessageAdapter";
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ChatData> chatDataArrayList = new ArrayList<ChatData>() ;
    // ListViewAdapter의 생성자
    public MessageAdapter() {
        Log.d(Tag,"생성자");
    }
    public void clear(){
        chatDataArrayList.clear();
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        Log.d(Tag,"count"+chatDataArrayList.size());
        return chatDataArrayList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        Collections.sort(chatDataArrayList, new Comparator<ChatData>() {
            @Override
            public int compare(ChatData o1, ChatData o2) {
                return (int)(o1.getTime() - o2.getTime());
            }

        });
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_chatdatalist, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView messageTextView = (TextView) convertView.findViewById(R.id.textView) ;
        String name = chatDataArrayList.get(position).getUserName();
        Log.d("yyyyy",LoginUser.loginUser.getId());
        Log.d("yyyyy",name);
        if(name.equals(LoginUser.loginUser.getId())){
            // ID가 사용자 ID랑 같으면
            messageTextView.setGravity(Gravity.RIGHT);
        } else {
            messageTextView.setGravity(Gravity.LEFT);

        }
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ChatData chatDataItem = chatDataArrayList.get(position);

        messageTextView.setText(chatDataItem.getMessage());

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
        return chatDataArrayList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(ChatData chatData) {
        Log.d(Tag,chatData.getMessage());
        chatDataArrayList.add(chatData);
    }
}



