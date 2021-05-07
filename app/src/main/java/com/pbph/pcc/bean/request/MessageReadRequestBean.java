package com.pbph.pcc.bean.request;

import android.support.annotation.NonNull;

import com.pbph.pcc.bean.request.BaseRequestBean;
import com.pbph.pcc.bean.response.GetMessageWebResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageReadRequestBean extends BaseRequestBean {

    public String arrayString;

    public MessageReadRequestBean(@NonNull GetMessageWebResponseBean bean) {
        List<GetMessageWebResponseBean.DataBean> list = bean.getData();
//        array = new JSONArray();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < list.size(); i++) {
            GetMessageWebResponseBean.DataBean dataBean = list.get(i);
            if ("0".equals(dataBean.getMessageIsRead())) {
                stringBuilder.append(dataBean.getMessageId()).append(",");
//                try {
//                    JSONObject object = new JSONObject();
//                    object.put("messageId", list.get(i).getMessageId());
//                    array.put(object);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }

        if (stringBuilder.length() > 1) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        stringBuilder.append("]");
        arrayString = stringBuilder.toString();
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
//            [sdf,sdf,sdf,sdf]
            object.put("messageId", arrayString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getJson(object);
    }
}
