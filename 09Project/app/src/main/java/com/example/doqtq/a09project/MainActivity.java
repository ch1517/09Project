package com.example.doqtq.a09project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    ArrayList<Board> boardArrayList = new ArrayList<Board>();
    ListViewAdapter adapter = new ListViewAdapter();
    ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
        new BackgroundTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new BackgroundTask().execute();
        listView = (ListView)findViewById(R.id.listView);

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String writerStr = item.getBoard().getId();

                Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                int writerMode = 0;
                if(LoginUser.loginUser == null){
                    Toast.makeText(getApplicationContext(),"작성자 아님",Toast.LENGTH_SHORT).show();
                } else {
                    if(LoginUser.loginUser.getId().equals(writerStr)) {
                        Toast.makeText(getApplicationContext(), "작성자 맞음", Toast.LENGTH_SHORT).show();
                        writerMode = 1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "작성자 아님", Toast.LENGTH_SHORT).show();
                    }
                }
                intent.putExtra("writerMode", writerMode);
                intent.putExtra("board", item.getBoard());
                intent.putExtra("ordernum",item.getBoard().getOrdernum());
                intent.putExtra("ordernum2", item.getBoard().getOrdernum2());

                startActivity(intent);
                // TODO : use item data.
            }
        }) ;



    }

    class BackgroundTask extends AsyncTask<Void, Void, String>{
        String target;
        private ProgressDialog mDlg = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            boardArrayList.clear();
            adapter.clear();
            target = "http://ekfms35.dothome.co.kr/ListShow.php";
            mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDlg.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
           try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("DB",result);
            mDlg.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count=0;
                String idx, id, title, content, photo, date;
                int price, ordernum, ordernum2, hit;
                while(count<jsonArray.length()){

                    Log.d("for문",count+"");
                    JSONObject object = jsonArray.getJSONObject(count);
                    idx = object.getString("idx");
                    id = object.getString("id");
                    title = object.getString("title");
                    price = object.getInt("price");
                    content = object.getString("content");
                    photo = object.getString("photo");
                    ordernum = object.getInt("ordernum");
                    ordernum2 = object.getInt("ordernum2");
                    hit = object.getInt("hit");
                    date = object.getString("wdate");
                    Board board = new Board(idx,id,title,price,content,photo,ordernum, ordernum2, hit,date);
                    adapter.addItem(board);
                    boardArrayList.add(board);
                    count++;
                }
                listView.setAdapter(adapter);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.writing09:
                if(LoginUser.loginUser == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("mode", 1); // 로그인화면(글 작성으로 넘어가는)
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, WritingActivity.class);
                    startActivity(intent);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

