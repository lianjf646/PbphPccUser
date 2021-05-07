package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/25.
 */

public class SetSchoolInfoBean extends BaseResponseBean {

    /**
     * data : {"receivePhone":"","receiveName":"","receiveSex":"","raddressName":"","schoolId":"","receiveId":""}
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
         * receivePhone :
         * receiveName :
         * receiveSex :
         * raddressName :
         * schoolId :
         * receiveId :
         */

        private String receivePhone;
        private String receiveName;
        private String receiveSex;
        private String raddressName;
        private String schoolId;
        private String receiveId;

        public String getReceivePhone() {
            return receivePhone;
        }

        public void setReceivePhone(String receivePhone) {
            this.receivePhone = receivePhone;
        }

        public String getReceiveName() {
            return receiveName;
        }

        public void setReceiveName(String receiveName) {
            this.receiveName = receiveName;
        }

        public String getReceiveSex() {
            return receiveSex;
        }

        public void setReceiveSex(String receiveSex) {
            this.receiveSex = receiveSex;
        }

        public String getRaddressName() {
            return raddressName;
        }

        public void setRaddressName(String raddressName) {
            this.raddressName = raddressName;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getReceiveId() {
            return receiveId;
        }

        public void setReceiveId(String receiveId) {
            this.receiveId = receiveId;
        }
    }
}
