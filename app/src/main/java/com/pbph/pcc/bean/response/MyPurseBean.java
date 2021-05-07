package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class MyPurseBean extends BaseResponseBean {

    /**
     * data : {"getTotleSum":"200","myBalance":"100.00","deals":[{"dealCreatetime":"2017-09-07 09:44:26","dealId":1,"dealPrice":"100.00","dealStatus":"0","orderType":1,"userId":20},{"dealCreatetime":"2017-09-07 10:17:17","dealId":2,"dealPrice":"100.00","dealStatus":"0","orderType":2,"userId":20}]}
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
         * getTotleSum : 200
         * myBalance : 100.00
         * deals : [{"dealCreatetime":"2017-09-07 09:44:26","dealId":1,"dealPrice":"100.00","dealStatus":"0","orderType":1,"userId":20},{"dealCreatetime":"2017-09-07 10:17:17","dealId":2,"dealPrice":"100.00","dealStatus":"0","orderType":2,"userId":20}]
         */

        private String getTotleSum;
        private String myBalance;
        private List<DealsBean> deals;

        public String getGetTotleSum() {
            return getTotleSum;
        }

        public void setGetTotleSum(String getTotleSum) {
            this.getTotleSum = getTotleSum;
        }

        public String getMyBalance() {
            return myBalance;
        }

        public void setMyBalance(String myBalance) {
            this.myBalance = myBalance;
        }

        public List<DealsBean> getDeals() {
            return deals;
        }

        public void setDeals(List<DealsBean> deals) {
            this.deals = deals;
        }

        public static class DealsBean {
            /**
             * dealCreatetime : 2017-09-07 09:44:26
             * dealId : 1
             * dealPrice : 100.00
             * dealStatus : 0
             * orderType : 1
             * userId : 20
             */

            private String dealCreatetime;
            private int dealId;
            private String dealPrice;
            private String dealStatus; //0收入 +，1支出 -，2退款 +
            private int orderType;
            private int userId;

            public String getDealCreatetime() {
                return dealCreatetime;
            }

            public void setDealCreatetime(String dealCreatetime) {
                this.dealCreatetime = dealCreatetime;
            }

            public int getDealId() {
                return dealId;
            }

            public void setDealId(int dealId) {
                this.dealId = dealId;
            }

            public String getDealPrice() {
                return dealPrice;
            }

            public void setDealPrice(String dealPrice) {
                this.dealPrice = dealPrice;
            }

            public String getDealStatus() {
                return dealStatus;
            }

            public void setDealStatus(String dealStatus) {
                this.dealStatus = dealStatus;
            }

            public int getOrderType() {
                return orderType;
            }

            public void setOrderType(int orderType) {
                this.orderType = orderType;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
