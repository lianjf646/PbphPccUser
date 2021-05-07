package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class QueryTakeAddressRequestBean extends BaseRequestBean {
    private int userid;

    public QueryTakeAddressRequestBean(String userid) {
        this.userid = Integer.valueOf(userid);
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
