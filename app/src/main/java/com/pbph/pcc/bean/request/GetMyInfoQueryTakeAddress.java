package com.pbph.pcc.bean.request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/6.
 */

public class GetMyInfoQueryTakeAddress extends BaseRequestBean {
    private int userid;
    private String schoolId;

    public GetMyInfoQueryTakeAddress(String userid, String schoolId) {
        this.userid = Integer.valueOf(userid);
        this.schoolId = schoolId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userid);
            object.put("schoolId", schoolId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
