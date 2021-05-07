package com.pbph.pcc.bean.response;

import com.pbph.pcc.bean.ABean;

/**
 * Created by Administrator on 2017/9/22.
 */

public class BaseResponseBean extends ABean {

    public String code;

    public String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
