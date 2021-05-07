package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetAcceptOrderListBean extends BaseResponseBean {

    /**
     * data : {"OrderListEntity":[{"orderType":"任务","getAddressName":"测试取货地址","orderTime":"1508239122","getAddressId":"","sendAddressName":"测试送货地址","gettingAmount":"10","schoolId":"2","orderCode":"4","sendAddressId":"14","dinnerAmount":""},{"orderType":"快递","getAddressName":"测试取货地址","orderTime":"1508239122","getAddressId":"14","sendAddressName":"测试送货地址","gettingAmount":"10","schoolId":"2","orderCode":"3","sendAddressId":"14","dinnerAmount":""},{"orderType":"超市","getAddressName":"测试取货地址","orderTime":"1508239122","getAddressId":"","sendAddressName":"测试送货地址","gettingAmount":"10","schoolId":"2","orderCode":"2","sendAddressId":"14","dinnerAmount":""},{"orderType":"外卖","getAddressName":"测试取货地址","orderTime":"1508239122","getAddressId":"14","sendAddressName":"测试送货地址","gettingAmount":"10","schoolId":"2","orderCode":"1","sendAddressId":"14","dinnerAmount":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<OrderListEntityBean> OrderListEntity;

        public List<OrderListEntityBean> getOrderListEntity() {
            return OrderListEntity;
        }

        public void setOrderListEntity(List<OrderListEntityBean> OrderListEntity) {
            this.OrderListEntity = OrderListEntity;
        }

        public static class OrderListEntityBean {
            /**
             * orderType : 任务
             * getAddressName : 测试取货地址
             * orderTime : 1508239122
             * getAddressId :
             * sendAddressName : 测试送货地址
             * gettingAmount : 10
             * schoolId : 2
             * orderCode : 4
             * sendAddressId : 14
             * dinnerAmount :
             */

            private String orderId;
            private String orderType;
            private String orderTypeText;
            private String getAddressName;
            private String orderTime;
            private String getAddressId;
            private String sendAddressName;
            private String gettingAmount;
            private String schoolId;
            private String orderCode;
            private String sendAddressId;
            private String dinnerAmount;
            private String resortStatus;
            private String expectedDeliveryTime;

            public String getExpectedDeliveryTime() {
                return expectedDeliveryTime;
            }

            public void setExpectedDeliveryTime(String expectedDeliveryTime) {
                this.expectedDeliveryTime = expectedDeliveryTime;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getOrderTypeText() {
                return orderTypeText;
            }

            public void setOrderTypeText(String orderTypeText) {
                this.orderTypeText = orderTypeText;
            }

            public String getOrderType() {
                return orderType;
            }

            public void setOrderType(String orderType) {
                this.orderType = orderType;
            }

            public String getGetAddressName() {
                return getAddressName;
            }

            public void setGetAddressName(String getAddressName) {
                this.getAddressName = getAddressName;
            }

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public String getGetAddressId() {
                return getAddressId;
            }

            public void setGetAddressId(String getAddressId) {
                this.getAddressId = getAddressId;
            }

            public String getSendAddressName() {
                return sendAddressName;
            }

            public void setSendAddressName(String sendAddressName) {
                this.sendAddressName = sendAddressName;
            }

            public String getGettingAmount() {
                return gettingAmount;
            }

            public void setGettingAmount(String gettingAmount) {
                this.gettingAmount = gettingAmount;
            }

            public String getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(String schoolId) {
                this.schoolId = schoolId;
            }

            public String getOrderCode() {
                return orderCode;
            }

            public void setOrderCode(String orderCode) {
                this.orderCode = orderCode;
            }

            public String getSendAddressId() {
                return sendAddressId;
            }

            public void setSendAddressId(String sendAddressId) {
                this.sendAddressId = sendAddressId;
            }

            public String getDinnerAmount() {
                return dinnerAmount;
            }

            public void setDinnerAmount(String dinnerAmount) {
                this.dinnerAmount = dinnerAmount;
            }

            public String getResortStatus() {
                return resortStatus;
            }

            public void setResortStatus(String resortStatus) {
                this.resortStatus = resortStatus;
            }
        }
    }
}
