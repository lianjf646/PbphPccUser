package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetValidCodeRequestBean extends BaseRequestBean {
    private String username = "";

    public GetValidCodeRequestBean(String username) {
        this.username = username;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userPhone", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
