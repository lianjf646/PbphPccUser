package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/26.
 */

public class SendRedRequestBean extends BaseRequestBean {
    private String userId;
    private String capital;

    public SendRedRequestBean(String userId, String capital) {
        this.userId = userId;
        this.capital = capital;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userId);
            object.put("capital", capital);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
