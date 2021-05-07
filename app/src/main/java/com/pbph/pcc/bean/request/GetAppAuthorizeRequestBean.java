package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetAppAuthorizeRequestBean extends BaseRequestBean {

    private String unionId;
    private String userId;

    public GetAppAuthorizeRequestBean(String unionId, String userId) {
        this.unionId = unionId;
        this.userId = userId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("unionId", unionId);
            object.put("userId", userId);
//            object.put("figurePosition", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
