package com.example.doqtq.a09project;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by doqtq on 2017-11-29.
 */

public class WritingRequest extends StringRequest {

    final static private String URL = "http://ekfms35.dothome.co.kr/WritingAdd.php";
    private Map<String, String> parameters;

    public WritingRequest(String id, String title, String price, String ordernum, String content, String userfile, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        Log.d("ididid",id);
        parameters = new HashMap<>();
        parameters.put("id",id);
        parameters.put("title", title);
        parameters.put("price", price);
        parameters.put("ordernum", ordernum);
        parameters.put("content", content);
        parameters.put("userfile", userfile);

    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
