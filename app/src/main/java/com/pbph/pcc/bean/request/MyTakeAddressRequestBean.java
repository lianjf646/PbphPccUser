package com.pbph.pcc.bean.request;

import android.support.annotation.NonNull;

import com.pbph.pcc.bean.request.BaseRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/22.
 */

public class MyTakeAddressRequestBean extends BaseRequestBean {
    private int schoolId;
    private int addrTypeId;

    public MyTakeAddressRequestBean(@NonNull int schoolId, @NonNull int addrTypeId) {
        this.schoolId = schoolId;
        this.addrTypeId = addrTypeId;
    }

    public MyTakeAddressRequestBean() {
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getAddrTypeId() {
        return addrTypeId;
    }

    public void setAddrTypeId(int addrTypeId) {
        this.addrTypeId = addrTypeId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("addrTypeId", addrTypeId);
            object.put("schoolId", schoolId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
