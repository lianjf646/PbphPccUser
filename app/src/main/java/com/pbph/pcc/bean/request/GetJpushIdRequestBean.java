package com.pbph.pcc.bean.request;

import com.pbph.pcc.application.PccApplication;
import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetJpushIdRequestBean extends BaseRequestBean {

    private String userPhone;
    private String jpushId;
    private String clientType;
    private String deviceId;
    private String type;

    public GetJpushIdRequestBean(String userPhone, String jpushId) {
        this.userPhone = userPhone;
        this.jpushId = jpushId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("userPhone", userPhone);
            object.put("jpushId", jpushId);
            object.put("clientType", "0");
            object.put("deviceId", PccApplication.getImei());
            object.put("type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
