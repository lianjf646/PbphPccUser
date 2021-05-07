package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserTakeoutInfoBean extends BaseResponseBean {

    /**
     * data : {"TakeoutEntity":{"takeStoreNum":"1","takeOrderLoseTime":"2","takeStatus":"0","takeSoodsPrices":"1.00","takeScope":"1","takeResortTime":"2","schoolId":"2","takeSoodsPrice":"1.00","takeNoresponseTime":"1","takeSoodsJcprice":"1.00"}}
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
         * TakeoutEntity : {"takeStoreNum":"1","takeOrderLoseTime":"2","takeStatus":"0","takeSoodsPrices":"1.00","takeScope":"1","takeResortTime":"2","schoolId":"2","takeSoodsPrice":"1.00","takeNoresponseTime":"1","takeSoodsJcprice":"1.00"}
         */

        private TakeoutEntityBean TakeoutEntity;

        public TakeoutEntityBean getTakeoutEntity() {
            return TakeoutEntity;
        }

        public void setTakeoutEntity(TakeoutEntityBean TakeoutEntity) {
            this.TakeoutEntity = TakeoutEntity;
        }

        public static class TakeoutEntityBean {
            /**
             * takeStoreNum : 1
             * takeOrderLoseTime : 2
             * takeStatus : 0
             * takeSoodsPrices : 1.00
             * takeScope : 1
             * takeResortTime : 2
             * schoolId : 2
             * takeSoodsPrice : 1.00
             * takeNoresponseTime : 1
             * takeSoodsJcprice : 1.00
             */

            private String takeStoreNum;
            private String takeOrderLoseTime;
            private String takeStatus;
            private String takeSoodsPrices;
            private String takeScope;
            private String takeResortTime;
            private String schoolId;
            private String takeSoodsPrice;
            private String takeNoresponseTime;
            private String takeSoodsJcprice;

            public String getTakeStoreNum() {
                return takeStoreNum;
            }

            public void setTakeStoreNum(String takeStoreNum) {
                this.takeStoreNum = takeStoreNum;
            }

            public String getTakeOrderLoseTime() {
                return takeOrderLoseTime;
            }

            public void setTakeOrderLoseTime(String takeOrderLoseTime) {
                this.takeOrderLoseTime = takeOrderLoseTime;
            }

            public String getTakeStatus() {
                return takeStatus;
            }

            public void setTakeStatus(String takeStatus) {
                this.takeStatus = takeStatus;
            }

            public String getTakeSoodsPrices() {
                return takeSoodsPrices;
            }

            public void setTakeSoodsPrices(String takeSoodsPrices) {
                this.takeSoodsPrices = takeSoodsPrices;
            }

            public String getTakeScope() {
                return takeScope;
            }

            public void setTakeScope(String takeScope) {
                this.takeScope = takeScope;
            }

            public String getTakeResortTime() {
                return takeResortTime;
            }

            public void setTakeResortTime(String takeResortTime) {
                this.takeResortTime = takeResortTime;
            }

            public String getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(String schoolId) {
                this.schoolId = schoolId;
            }

            public String getTakeSoodsPrice() {
                return takeSoodsPrice;
            }

            public void setTakeSoodsPrice(String takeSoodsPrice) {
                this.takeSoodsPrice = takeSoodsPrice;
            }

            public String getTakeNoresponseTime() {
                return takeNoresponseTime;
            }

            public void setTakeNoresponseTime(String takeNoresponseTime) {
                this.takeNoresponseTime = takeNoresponseTime;
            }

            public String getTakeSoodsJcprice() {
                return takeSoodsJcprice;
            }

            public void setTakeSoodsJcprice(String takeSoodsJcprice) {
                this.takeSoodsJcprice = takeSoodsJcprice;
            }
        }
    }
}
