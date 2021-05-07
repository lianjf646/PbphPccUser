package com.pbph.pcc.bean.request;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateStatusWebRequestBean extends BaseRequestBean {

    private String status;//0拒绝 1接受
    private String recordId;
    private String messageId;

    public UpdateStatusWebRequestBean(String status, String recordId, String messageId) {
        this.status = status;
        this.recordId = recordId;
        this.messageId = messageId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("status", status);
            object.put("recordId", recordId);
            object.put("messageId", messageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
