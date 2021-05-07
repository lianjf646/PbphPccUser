package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetFigureWebRequestBean extends BaseRequestBean {

    private String schoolId;

    public GetFigureWebRequestBean(String schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("schoolId", schoolId);
            object.put("figurePosition", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
