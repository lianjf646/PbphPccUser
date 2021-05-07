package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetAppAuthorizeResonseBean extends BaseResponseBean {


    /**
     * code : 2029
     * data : {}
     * msg : 未签约
     */


    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
    }
}
