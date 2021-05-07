package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/22.
 */

public class UUIDResultBean extends BaseResponseBean {

    /**
     * data : {"uuId":"1507529820943"}
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
         * uuId : 1507529820943
         */

        private String uuId;

        public String getUuId() {
            return uuId;
        }

        public void setUuId(String uuId) {
            this.uuId = uuId;
        }
    }
}
