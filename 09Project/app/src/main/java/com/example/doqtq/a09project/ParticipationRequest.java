package com.example.doqtq.a09project;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by doqtq on 2017-12-01.
 */

public class ParticipationRequest extends StringRequest {
    final static private String URL = "http://ekfms35.dothome.co.kr/OrderAdd.php";
    private Map<String, String> parameters;

    public ParticipationRequest(String name, String id, String idx, String memo, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters=new HashMap<>();
        parameters.put("name",name);
        parameters.put("id",id);
        parameters.put("idx",idx);
        parameters.put("memo", memo);
        parameters.put("token",LoginUser.loginUser.getToken());
    }
    @Override
    protected Map<String, String> getParams(){
        return parameters;
    }
}
