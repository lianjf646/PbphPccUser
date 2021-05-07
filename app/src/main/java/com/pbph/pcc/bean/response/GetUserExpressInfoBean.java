package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserExpressInfoBean extends BaseResponseBean {

    /**
     * data : {"ExpressEntity":{"expressScope":"4","expressNoresponseTime":"3","expressUpstairsPrice":"3.00","expressOrderLoseTime":"3","schoolId":"2","expressResortTime":"3","expressPrice":"3.50","expressStatus":"1"}}
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
         * ExpressEntity : {"expressScope":"4","expressNoresponseTime":"3","expressUpstairsPrice":"3.00","expressOrderLoseTime":"3","schoolId":"2","expressResortTime":"3","expressPrice":"3.50","expressStatus":"1"}
         */

        private ExpressEntityBean ExpressEntity;

        public ExpressEntityBean getExpressEntity() {
            return ExpressEntity;
        }

        public void setExpressEntity(ExpressEntityBean ExpressEntity) {
            this.ExpressEntity = ExpressEntity;
        }

        public static class ExpressEntityBean {
            /**
             * expressScope : 4
             * expressNoresponseTime : 3
             * expressUpstairsPrice : 3.00
             * expressOrderLoseTime : 3
             * schoolId : 2
             * expressResortTime : 3
             * expressPrice : 3.50
             * expressStatus : 1
             */

            private String expressScope;
            private String expressNoresponseTime;
            private String expressUpstairsPrice;
            private String expressOrderLoseTime;
            private String schoolId;
            private String expressResortTime;
            private String expressPrice;
            private String expressStatus;

            public String getExpressScope() {
                return expressScope;
            }

            public void setExpressScope(String expressScope) {
                this.expressScope = expressScope;
            }

            public String getExpressNoresponseTime() {
                return expressNoresponseTime;
            }

            public void setExpressNoresponseTime(String expressNoresponseTime) {
                this.expressNoresponseTime = expressNoresponseTime;
            }

            public String getExpressUpstairsPrice() {
                return expressUpstairsPrice;
            }

            public void setExpressUpstairsPrice(String expressUpstairsPrice) {
                this.expressUpstairsPrice = expressUpstairsPrice;
            }

            public String getExpressOrderLoseTime() {
                return expressOrderLoseTime;
            }

            public void setExpressOrderLoseTime(String expressOrderLoseTime) {
                this.expressOrderLoseTime = expressOrderLoseTime;
            }

            public String getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(String schoolId) {
                this.schoolId = schoolId;
            }

            public String getExpressResortTime() {
                return expressResortTime;
            }

            public void setExpressResortTime(String expressResortTime) {
                this.expressResortTime = expressResortTime;
            }

            public String getExpressPrice() {
                return expressPrice;
            }

            public void setExpressPrice(String expressPrice) {
                this.expressPrice = expressPrice;
            }

            public String getExpressStatus() {
                return expressStatus;
            }

            public void setExpressStatus(String expressStatus) {
                this.expressStatus = expressStatus;
            }
        }
    }
}
