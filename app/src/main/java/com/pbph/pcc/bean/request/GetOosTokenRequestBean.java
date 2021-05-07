package com.pbph.pcc.bean.request;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/26.
 */

public class GetOosTokenRequestBean extends BaseRequestBean {

    @Override
    public JSONObject toJson() {
        return getJson(new JSONObject());
    }
}
