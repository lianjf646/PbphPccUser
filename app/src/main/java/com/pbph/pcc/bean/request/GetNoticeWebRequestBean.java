package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetNoticeWebRequestBean extends BaseRequestBean {

    private String userId;

    public GetNoticeWebRequestBean(String userId) {
        this.userId = userId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userId);
            object.put("figurePosition", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
