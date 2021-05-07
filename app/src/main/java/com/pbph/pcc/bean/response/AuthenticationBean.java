package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/10/19.
 */

public class AuthenticationBean extends BaseResponseBean {

    /**
     * code : 200
     * data : {"authStatus":"1"}
     * msg : 成功
     */


    private DataBean data;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * authStatus : 1
         */

        private String authStatus;

        public String getAuthStatus() {
            return authStatus;
        }

        public void setAuthStatus(String authStatus) {
            this.authStatus = authStatus;
        }
    }
}
