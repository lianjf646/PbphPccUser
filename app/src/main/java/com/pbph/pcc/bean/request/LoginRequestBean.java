package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/21.
 */

public class LoginRequestBean extends BaseRequestBean {

    public String username = "";
    public String validCode = "";
    public String deviceId = "";

    public LoginRequestBean(String username, String validCode, String deviceId) {
        this.username = username;
        this.validCode = validCode;
        this.deviceId = deviceId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userPhone", username);
            object.put("validCode", validCode);
            object.put("deviceId", deviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
