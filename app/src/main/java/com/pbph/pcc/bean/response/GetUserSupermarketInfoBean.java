package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserSupermarketInfoBean extends BaseResponseBean {

    /**
     * data : {"SupermarketEntity":{"supSoodsNum":"5","supSoodsJcprices":"5.00","supResortTime":"5","supStatus":"0","supSoodsJcprice":"5","supSoodsPrice":"5.00","supSoodsNums":"5","supScope":"5","supOrderLoseTime":"1","cityId":"1","supSoodsPrices":"5.00","supNoresponseTime":"5"}}
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
         * SupermarketEntity : {"supSoodsNum":"5","supSoodsJcprices":"5.00","supResortTime":"5","supStatus":"0","supSoodsJcprice":"5","supSoodsPrice":"5.00","supSoodsNums":"5","supScope":"5","supOrderLoseTime":"1","cityId":"1","supSoodsPrices":"5.00","supNoresponseTime":"5"}
         */

        private SupermarketEntityBean SupermarketEntity;

        public SupermarketEntityBean getSupermarketEntity() {
            return SupermarketEntity;
        }

        public void setSupermarketEntity(SupermarketEntityBean SupermarketEntity) {
            this.SupermarketEntity = SupermarketEntity;
        }

        public static class SupermarketEntityBean {
            /**
             * supSoodsNum : 5
             * supSoodsJcprices : 5.00
             * supResortTime : 5
             * supStatus : 0
             * supSoodsJcprice : 5
             * supSoodsPrice : 5.00
             * supSoodsNums : 5
             * supScope : 5
             * supOrderLoseTime : 1
             * cityId : 1
             * supSoodsPrices : 5.00
             * supNoresponseTime : 5
             */

            private String supSoodsNum;
            private String supSoodsJcprices;
            private String supResortTime;
            private String supStatus;
            private String supSoodsJcprice;
            private String supSoodsPrice;
            private String supSoodsNums;
            private String supScope;
            private String supOrderLoseTime;
            private String cityId;
            private String supSoodsPrices;
            private String supNoresponseTime;

            public String getSupSoodsNum() {
                return supSoodsNum;
            }

            public void setSupSoodsNum(String supSoodsNum) {
                this.supSoodsNum = supSoodsNum;
            }

            public String getSupSoodsJcprices() {
                return supSoodsJcprices;
            }

            public void setSupSoodsJcprices(String supSoodsJcprices) {
                this.supSoodsJcprices = supSoodsJcprices;
            }

            public String getSupResortTime() {
                return supResortTime;
            }

            public void setSupResortTime(String supResortTime) {
                this.supResortTime = supResortTime;
            }

            public String getSupStatus() {
                return supStatus;
            }

            public void setSupStatus(String supStatus) {
                this.supStatus = supStatus;
            }

            public String getSupSoodsJcprice() {
                return supSoodsJcprice;
            }

            public void setSupSoodsJcprice(String supSoodsJcprice) {
                this.supSoodsJcprice = supSoodsJcprice;
            }

            public String getSupSoodsPrice() {
                return supSoodsPrice;
            }

            public void setSupSoodsPrice(String supSoodsPrice) {
                this.supSoodsPrice = supSoodsPrice;
            }

            public String getSupSoodsNums() {
                return supSoodsNums;
            }

            public void setSupSoodsNums(String supSoodsNums) {
                this.supSoodsNums = supSoodsNums;
            }

            public String getSupScope() {
                return supScope;
            }

            public void setSupScope(String supScope) {
                this.supScope = supScope;
            }

            public String getSupOrderLoseTime() {
                return supOrderLoseTime;
            }

            public void setSupOrderLoseTime(String supOrderLoseTime) {
                this.supOrderLoseTime = supOrderLoseTime;
            }

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public String getSupSoodsPrices() {
                return supSoodsPrices;
            }

            public void setSupSoodsPrices(String supSoodsPrices) {
                this.supSoodsPrices = supSoodsPrices;
            }

            public String getSupNoresponseTime() {
                return supNoresponseTime;
            }

            public void setSupNoresponseTime(String supNoresponseTime) {
                this.supNoresponseTime = supNoresponseTime;
            }
        }
    }
}
