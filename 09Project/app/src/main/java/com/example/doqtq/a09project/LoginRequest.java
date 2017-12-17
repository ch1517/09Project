package com.example.doqtq.a09project;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by doqtq on 2017-11-28.
 */

public class LoginRequest extends StringRequest {

    final static private String URL = "http://ekfms35.dothome.co.kr/UserLogin.php";
    private Map<String, String> parameters;

    public LoginRequest(String id, String pw, String token, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pw", pw);
        parameters.put("token", token);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
