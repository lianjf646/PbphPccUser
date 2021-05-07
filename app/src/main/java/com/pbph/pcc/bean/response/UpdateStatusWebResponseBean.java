package com.pbph.pcc.bean.response;

public class UpdateStatusWebResponseBean extends BaseResponseBean {


    /**
     * code : 200
     * data : {"result":"0"}
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
         * result : 0
         */

        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
