package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/10/17.
 */

public class QueryAgentBean extends BaseResponseBean {


    /**
     * msg : 成功
     * code : 200
     * data : {"authIdCardImg":"","authName":"卡卡西","authSex":"0","authStudentCardImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/1510291543901.jpg","authStudentId":"2222222","authSchoolId":"18","authPhone":"15636899875","authSchoolName":"测试学校11-6"}
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
         * authIdCardImg :
         * authName : 卡卡西
         * authSex : 0
         * authStudentCardImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/1510291543901.jpg
         * authStudentId : 2222222
         * authSchoolId : 18
         * authPhone : 15636899875
         * authSchoolName : 测试学校11-6
         */

        private String authIdCardImg;
        private String authName;
        private String authSex;
        private String authStudentCardImg;
        private String authStudentId;
        private String authSchoolId;
        private String authPhone;
        private String authSchoolName;

        public String getAuthIdCardImg() {
            return authIdCardImg;
        }

        public void setAuthIdCardImg(String authIdCardImg) {
            this.authIdCardImg = authIdCardImg;
        }

        public String getAuthName() {
            return authName;
        }

        public void setAuthName(String authName) {
            this.authName = authName;
        }

        public String getAuthSex() {
            return authSex;
        }

        public void setAuthSex(String authSex) {
            this.authSex = authSex;
        }

        public String getAuthStudentCardImg() {
            return authStudentCardImg;
        }

        public void setAuthStudentCardImg(String authStudentCardImg) {
            this.authStudentCardImg = authStudentCardImg;
        }

        public String getAuthStudentId() {
            return authStudentId;
        }

        public void setAuthStudentId(String authStudentId) {
            this.authStudentId = authStudentId;
        }

        public String getAuthSchoolId() {
            return authSchoolId;
        }

        public void setAuthSchoolId(String authSchoolId) {
            this.authSchoolId = authSchoolId;
        }

        public String getAuthPhone() {
            return authPhone;
        }

        public void setAuthPhone(String authPhone) {
            this.authPhone = authPhone;
        }

        public String getAuthSchoolName() {
            return authSchoolName;
        }

        public void setAuthSchoolName(String authSchoolName) {
            this.authSchoolName = authSchoolName;
        }
    }
}
