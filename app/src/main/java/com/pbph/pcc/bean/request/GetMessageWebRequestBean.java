package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;


public class GetMessageWebRequestBean extends BaseRequestBean {
    private String userId;

    public GetMessageWebRequestBean(String userId) {
        this.userId = userId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
