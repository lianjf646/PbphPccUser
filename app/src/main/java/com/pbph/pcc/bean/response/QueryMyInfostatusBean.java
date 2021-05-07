package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/22.
 */

public class QueryMyInfostatusBean extends BaseResponseBean {

    /**
     * data : {"userStatus":"0","orderSwitch":"0"}
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
         * userStatus : 0
         * orderSwitch : 0
         */

        private String userStatus;
        private String orderSwitch;
        private Float starLevel;

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getOrderSwitch() {
            return orderSwitch;
        }

        public void setOrderSwitch(String orderSwitch) {
            this.orderSwitch = orderSwitch;
        }

        public Float getStarLevel() {
            return starLevel;
        }

        public void setStarLevel(Float starLevel) {
            this.starLevel = starLevel;
        }
    }
}
