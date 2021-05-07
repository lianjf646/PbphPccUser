package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/22.
 */

public class LoginResponseBean extends BaseResponseBean {

    /**
     * code : 200
     * data : {"token":"83195310CFA72818ED0CC477ADFF177DC9FF7E5CD9A03E352E55587C","userId":20}
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
         * token : 83195310CFA72818ED0CC477ADFF177DC9FF7E5CD9A03E352E55587C
         * userId : 20
         */

        private String token;
        private String userId;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
