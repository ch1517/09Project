package com.example.doqtq.a09project;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by doqtq on 2017-12-15.
 */

public class DeliveryRequest extends StringRequest {

    final static private String URL = "http://ekfms35.dothome.co.kr/fcm/PushNotification.php";
    private Map<String, String> parameters;

    public DeliveryRequest(String idx, String message, String delivery_count, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("idx", idx);
        parameters.put("message", message);
        parameters.put("count",delivery_count);
        Log.d("countcount",delivery_count);

        Log.d("messagemessage",message);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
