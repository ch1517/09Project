package com.example.doqtq.a09project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WritingActivity extends AppCompatActivity implements View.OnClickListener{
    // LOG
    private String TAGLOG = "MainActivityLoG";

    // 이미지넣는 뷰와 업로드하기위환 버튼
    private ImageView ivUploadImage;
    private Button addButton;

    // 서버로 업로드할 파일관련 변수
    public String uploadFilePath;
    public String uploadFileName;
    private int REQ_CODE_PICK_PICTURE = 1;

    String title;
    String price;
    String ordernum;
    String content;

    // 파일을 업로드 하기 위한 변수 선언
    private int serverResponseCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        // 변수 초기화
        InitVariable();
        final EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
        final EditText priceEditText = (EditText)findViewById(R.id.priceEditText);
        final EditText orderNumEditText = (EditText)findViewById(R.id.orderNumEditText);
        final EditText contentEditext = (EditText)findViewById(R.id.contenteditextView);

        addButton = (Button)findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                title = titleEditText.getText().toString();
                price = priceEditText.getText().toString();
                ordernum = orderNumEditText.getText().toString();
                content = contentEditext.getText().toString();

                if (uploadFilePath != null) {
                    UploadImageToServer uploadimagetoserver = new UploadImageToServer();
                    String s = "http://ekfms35.dothome.co.kr/ImageUploadToServer.php";
                    uploadimagetoserver.execute(s);
                } else {
                    Toast.makeText(getApplicationContext(), "You didn't insert any image", Toast.LENGTH_SHORT).show();
                    Log.i(TAGLOG,"You didn't insert any image");
                }
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

                String userfile = uploadFileName;
                WritingRequest writingRequest = new WritingRequest(LoginUser.loginUser.getId(),title, price,ordernum,content,userfile, responseListener);
                RequestQueue queue = Volley.newRequestQueue(WritingActivity.this);
                queue.add(writingRequest);
                finish();
            }


        });
    }
    // 초기화
    private void InitVariable() {
        // 이미지를 넣을 뷰
        ivUploadImage = (ImageView) findViewById(R.id.iv_uplolad_image);
        ivUploadImage.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_PICK_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String path = getPath(uri);
                String name = getName(uri);

                uploadFilePath = path;
                uploadFileName = name;

                Log.i(TAGLOG, "[onActivityResult] uploadFilePath:" + uploadFilePath + ", uploadFileName:" + uploadFileName);

                Bitmap bit = BitmapFactory.decodeFile(path);
                ivUploadImage.setImageBitmap(bit);
            }
        }
    }
    // 실제 경로 찾기
    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // 파일명 찾기
    private String getName(Uri uri) {
        String[] projection = {MediaStore.Images.ImageColumns.DISPLAY_NAME};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    // uri 아이디 찾기
    private String getUriId(Uri uri) {
        String[] projection = {MediaStore.Images.ImageColumns._ID};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_uplolad_image:
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // images on the SD card.

                // 결과를 리턴하는 Activity 호출
                startActivityForResult(i, REQ_CODE_PICK_PICTURE);
                break;
            case R.id.addButton:
                if (uploadFilePath != null) {
                    UploadImageToServer uploadimagetoserver = new UploadImageToServer();
                    String s = "http://ekfms35.dothome.co.kr/ImageUploadToServer.php";
                    uploadimagetoserver.execute(s);
                } else {
                    Toast.makeText(getApplicationContext(), "You didn't insert any image", Toast.LENGTH_SHORT).show();
                    Log.i(TAGLOG,"You didn't insert any image");
                }
                break;
        }
    }
    private class UploadImageToServer extends AsyncTask<String, String, String> {

        ProgressDialog mProgressDialog;
        String fileName = uploadFilePath;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
//        int maxBufferSize = 1 * 10240 * 10240;
        int maxBufferSize = 1 * 12000 * 12000;
        File sourceFile = new File(uploadFilePath);

        @Override
        protected void onPreExecute() {
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(WritingActivity.this);
            mProgressDialog.setTitle("Loading...");
            mProgressDialog.setMessage("Image uploading...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... serverUrl) {
            if (!sourceFile.isFile()) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.i(TAGLOG, "[UploadImageToServer] Source File not exist :" + uploadFilePath);
                    }
                });
                return null;
            } else {
                try {
                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(serverUrl[0]);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);
                    Log.i(TAGLOG, "fileName: " + fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    // 사용자 이름으로 폴더를 생성하기 위해 사용자 이름을 서버로 전송한다.
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"data1\"" + lineEnd);

                    dos.writeBytes("Content-Disposition: form-data; title=\"title\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; price=\"price\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; ordernum=\"ordernum\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; content=\"content\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; content=\"content\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes("newImage");
                    dos.writeBytes(lineEnd);

                    // 이미지 전송

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size

                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i(TAGLOG, "[UploadImageToServer] HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                    if (serverResponseCode == 200) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "File Upload Completed", Toast.LENGTH_SHORT).show();
                                Log.i(TAGLOG,"File Upload Completed");
                            }
                        });
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();


                } catch (MalformedURLException ex) {

                    ex.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WritingActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                            Log.i(TAGLOG,"MalformedURLException");
                        }
                    });
                    Log.i(TAGLOG, "[UploadImageToServer] error: " + ex.getMessage(), ex);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WritingActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                            Log.i(TAGLOG,"Got Exception : see logcat ");
                        }
                    });
                    Log.i(TAGLOG, "[UploadImageToServer] Upload file to server Exception Exception : " + e.getMessage(), e);
                }
                Log.i(TAGLOG, "[UploadImageToServer] Finish");
                return null;
            } // End else block
        }

        @Override

        protected void onPostExecute(String s) {
            mProgressDialog.dismiss();

        }

    }
}
