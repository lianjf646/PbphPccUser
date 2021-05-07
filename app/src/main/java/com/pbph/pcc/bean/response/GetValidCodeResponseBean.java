package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetValidCodeResponseBean extends BaseResponseBean {
    /**
     * msg : 成功
     * code : 200
     * data : {"code":"877078"}
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
         * code : 877078
         */

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }


}
