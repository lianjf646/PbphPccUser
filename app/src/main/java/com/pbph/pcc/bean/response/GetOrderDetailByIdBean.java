package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetOrderDetailByIdBean extends BaseResponseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        //订单信息
        private String orderId;
        private String orderCode;

        private String orderType;
        private String orderTypeText;

        private String orderStatus;
        private String orderStatusText;

        private String orderTime;

        private String orderFinishTime;
        private String orderPayTime;
        private String orderPayWay;
        private String remarkInfo;//备注

        private String expectedDeliveryTime;

        //取餐送餐地址
        private String getAddressName;
        private String receiveAddressName;
        private String briefaddressName;

        //发单人信息
        private String sendUserImg;
        private String sendUserPhone;
        private String sendUserName;

        //接单人信息
        private String receiveUserImg;
        private String receiveUserName;
        private String receiveUserPhone;

        //通用星级评价
        private String appraiseStar;


        //外卖信息
        private String storeId;
        private String storeName;
        private List<OrderTakeoutSubBean> OrderTakeoutSub;

        //超市信息
        private List<OrderTakeoutSubBean> orderMarketSub;

        //快递信息
        private String expressName;
        private String expressInfo;
        private String receiveTimeRemark;
        private String isUpstairs;
        private String surplusAmount; //打赏 金额

        //        任物信息
        private String taskType;
        private String taskDescribe;
        private String taskAddressName;
        private String taskAddressDetail;
        private String taskTelephone;
        private String taskStatus;


        ////外卖 超市
        private String dinnerAmount;//餐品金额
        //
        private String shippingAmount;// 外卖 超市 配送金额 快递 金额
        // 任务
        private String taskAmount;//任务金额

        //接单人佣金
        private String gettingAmount;//佣金

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderTypeText() {
            return orderTypeText;
        }

        public void setOrderTypeText(String orderTypeText) {
            this.orderTypeText = orderTypeText;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderStatusText() {
            return orderStatusText;
        }

        public void setOrderStatusText(String orderStatusText) {
            this.orderStatusText = orderStatusText;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getOrderFinishTime() {
            return orderFinishTime;
        }

        public void setOrderFinishTime(String orderFinishTime) {
            this.orderFinishTime = orderFinishTime;
        }

        public String getSendUserImg() {
            return sendUserImg;
        }

        public void setSendUserImg(String sendUserImg) {
            this.sendUserImg = sendUserImg;
        }

        public String getOrderPayTime() {
            return orderPayTime;
        }

        public void setOrderPayTime(String orderPayTime) {
            this.orderPayTime = orderPayTime;
        }

        public String getOrderPayWay() {
            return orderPayWay;
        }

        public void setOrderPayWay(String orderPayWay) {
            this.orderPayWay = orderPayWay;
        }

        public String getRemarkInfo() {
            return remarkInfo;
        }

        public void setRemarkInfo(String remarkInfo) {
            this.remarkInfo = remarkInfo;
        }

        public String getGetAddressName() {
            return getAddressName;
        }

        public void setGetAddressName(String getAddressName) {
            this.getAddressName = getAddressName;
        }

        public String getReceiveAddressName() {
            return receiveAddressName;
        }

        public void setReceiveAddressName(String receiveAddressName) {
            this.receiveAddressName = receiveAddressName;
        }

        public String getSendUserPhone() {
            return sendUserPhone;
        }

        public void setSendUserPhone(String sendUserPhone) {
            this.sendUserPhone = sendUserPhone;
        }

        public String getSendUserName() {
            return sendUserName;
        }

        public void setSendUserName(String sendUserName) {
            this.sendUserName = sendUserName;
        }

        public String getReceiveUserImg() {
            return receiveUserImg;
        }

        public void setReceiveUserImg(String receiveUserImg) {
            this.receiveUserImg = receiveUserImg;
        }

        public String getBriefaddressName() {
            return briefaddressName;
        }

        public void setBriefaddressName(String briefaddressName) {
            this.briefaddressName = briefaddressName;
        }

        public String getReceiveUserName() {
            return receiveUserName;
        }

        public void setReceiveUserName(String receiveUserName) {
            this.receiveUserName = receiveUserName;
        }

        public String getReceiveUserPhone() {
            return receiveUserPhone;
        }

        public void setReceiveUserPhone(String receiveUserPhone) {
            this.receiveUserPhone = receiveUserPhone;
        }

        public String getExpectedDeliveryTime() {
            return expectedDeliveryTime;
        }

        public void setExpectedDeliveryTime(String expectedDeliveryTime) {
            this.expectedDeliveryTime = expectedDeliveryTime;
        }

        public String getAppraiseStar() {
            return appraiseStar;
        }

        public void setAppraiseStar(String appraiseStar) {
            this.appraiseStar = appraiseStar;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public List<OrderTakeoutSubBean> getOrderTakeoutSub() {
            return OrderTakeoutSub;
        }

        public void setOrderTakeoutSub(List<OrderTakeoutSubBean> orderTakeoutSub) {
            OrderTakeoutSub = orderTakeoutSub;
        }

        public List<OrderTakeoutSubBean> getOrderMarketSub() {
            return orderMarketSub;
        }

        public void setOrderMarketSub(List<OrderTakeoutSubBean> orderMarketSub) {
            this.orderMarketSub = orderMarketSub;
        }

        public String getExpressName() {
            return expressName;
        }

        public void setExpressName(String expressName) {
            this.expressName = expressName;
        }

        public String getExpressInfo() {
            return expressInfo;
        }

        public void setExpressInfo(String expressInfo) {
            this.expressInfo = expressInfo;
        }

        public String getReceiveTimeRemark() {
            return receiveTimeRemark;
        }

        public void setReceiveTimeRemark(String receiveTimeRemark) {
            this.receiveTimeRemark = receiveTimeRemark;
        }

        public String getIsUpstairs() {
            return isUpstairs;
        }

        public void setIsUpstairs(String isUpstairs) {
            this.isUpstairs = isUpstairs;
        }

        public String getSurplusAmount() {
            return surplusAmount;
        }

        public void setSurplusAmount(String surplusAmount) {
            this.surplusAmount = surplusAmount;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getTaskDescribe() {
            return taskDescribe;
        }

        public void setTaskDescribe(String taskDescribe) {
            this.taskDescribe = taskDescribe;
        }

        public String getTaskAddressName() {
            return taskAddressName;
        }

        public void setTaskAddressName(String taskAddressName) {
            this.taskAddressName = taskAddressName;
        }

        public String getTaskAddressDetail() {
            return taskAddressDetail;
        }

        public void setTaskAddressDetail(String taskAddressDetail) {
            this.taskAddressDetail = taskAddressDetail;
        }

        public String getTaskTelephone() {
            return taskTelephone;
        }

        public void setTaskTelephone(String taskTelephone) {
            this.taskTelephone = taskTelephone;
        }

        public String getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getDinnerAmount() {
            return dinnerAmount;
        }

        public void setDinnerAmount(String dinnerAmount) {
            this.dinnerAmount = dinnerAmount;
        }

        public String getShippingAmount() {
            return shippingAmount;
        }

        public void setShippingAmount(String shippingAmount) {
            this.shippingAmount = shippingAmount;
        }

        public String getTaskAmount() {
            return taskAmount;
        }

        public void setTaskAmount(String taskAmount) {
            this.taskAmount = taskAmount;
        }

        public String getGettingAmount() {
            return gettingAmount;
        }

        public void setGettingAmount(String gettingAmount) {
            this.gettingAmount = gettingAmount;
        }

        public static class OrderTakeoutSubBean {

            private String totalAmount;
            private String fnId;
            private String goodsId;
            private String goodsPrice;
            private String id;
            private String goodsName;
            private String goodsNum;

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getFnId() {
                return fnId;
            }

            public void setFnId(String fnId) {
                this.fnId = fnId;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(String goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(String goodsNum) {
                this.goodsNum = goodsNum;
            }
        }
    }

}
