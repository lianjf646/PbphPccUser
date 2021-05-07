package com.pbph.pcc.bean.request;

import android.support.annotation.NonNull;

import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.ABean;
import com.pbph.pcc.tools.MD5;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class BaseRequestBean extends ABean {

    protected JSONObject getJson(@NonNull JSONObject object) {
        return getPublicRequestData(object, PccApplication.getUserid(), PccApplication.httpRequestData.getToken(), PccApplication.getImei());
    }

    public abstract JSONObject toJson();

    private JSONObject getPublicRequestData(JSONObject data, String userid, String token, String deviceId) {
        JSONObject object = new JSONObject();
        try {
            object.put("userid", userid);
            object.put("token", token);
            object.put("sign", MD5.encrypt(data.toString()).toUpperCase());
            object.put("timestamp", System.currentTimeMillis());
            object.put("type", "1");
            object.put("deviceId", deviceId);
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
