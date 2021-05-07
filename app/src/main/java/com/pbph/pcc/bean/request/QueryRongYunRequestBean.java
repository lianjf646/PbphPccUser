package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class QueryRongYunRequestBean extends BaseRequestBean {

    private String userid;

    public QueryRongYunRequestBean(String userid) {
        this.userid = userid;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userid);
            object.put("status", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
