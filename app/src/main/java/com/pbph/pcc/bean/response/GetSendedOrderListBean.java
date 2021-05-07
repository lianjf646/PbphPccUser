package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetSendedOrderListBean extends BaseResponseBean {

    /**
     * data : {"orderList":[{"orderStatusText":"已完成","orderType":"1","orderTime":"2017-10-10 10:52:01","orderId":"144","orderStatus":"4","orderCode":"1000000722518752","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号","expressName":"食堂2","taskAddressDetail":"pengbopuhua","taskStatus":"7"},{"orderStatusText":"已评价","orderType":"1","orderTime":"2017-09-18 09:22:12","orderId":"5","orderStatus":"5","orderCode":"1234567890123452","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"已完成","orderType":"1","orderTime":"2017-09-11 21:39:12","orderId":"13","orderStatus":"4","orderCode":"1234567890123444","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"3","expressName":"食堂2","orderTime":"2017-09-22 15:12:52","orderId":"20","orderStatus":"1","orderCode":"1000001494471676","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"3","expressName":"食堂2","orderTime":"2017-09-22 15:11:30","orderId":"19","orderStatus":"1","orderCode":"1000001287323233","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"3","expressName":"食堂2","orderTime":"2017-09-22 15:09:40","orderId":"18","orderStatus":"1","orderCode":"1000001286619643","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"3","expressName":"食堂2","orderTime":"2017-09-22 15:03:24","orderId":"17","orderStatus":"1","orderCode":"1000001010101486","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"已完成","orderType":"3","expressName":"圆通","orderTime":"2017-09-09 21:39:12","orderId":"11","orderStatus":"4","orderCode":"1234567890123446","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"4","orderTime":"2017-10-09 20:40:11","orderId":"143","taskAddressDetail":"pengbopuhua","orderStatus":"1","orderCode":"1000000722518751","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"待接单","orderType":"4","orderTime":"2017-10-09 17:53:58","orderId":"142","taskAddressDetail":"pengbopuhua","orderStatus":"1","orderCode":"1000000251484181","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"已取消","orderType":"4","orderTime":"2017-10-09 17:52:06","orderId":"141","taskAddressDetail":"pengbopuhua","orderStatus":"6","orderCode":"1000000544404580","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"已评价","orderType":"4","orderTime":"2017-09-27 09:01:46","orderId":"29","taskAddressDetail":"pengbopuhua","orderStatus":"5","orderCode":"1000002115346914","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"已评价","orderType":"4","orderTime":"2017-09-27 08:59:37","orderId":"28","taskAddressDetail":"pengbopuhua","orderStatus":"5","orderCode":"1000001316312788","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"已评价","orderType":"4","orderTime":"2017-09-26 11:28:28","orderId":"27","taskAddressDetail":"pengbopuhua","orderStatus":"5","orderCode":"1000000145163564","orderTypeText":"任务","taskStatus":"8"},{"orderStatusText":"已评价","orderType":"4","orderTime":"2017-09-26 11:27:42","orderId":"26","taskAddressDetail":"pengbopuhua","orderStatus":"5","orderCode":"1000001064992583","orderTypeText":"任务","taskStatus":"8"},{"orderStatusText":"已取消","orderType":"4","orderTime":"2017-09-22 15:24:14","orderId":"25","taskAddressDetail":"pengbopuhua","orderStatus":"6","orderCode":"1000000853766042","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"已评价","orderType":"4","orderTime":"2017-09-22 15:22:48","orderId":"24","taskAddressDetail":"pengbopuhua","orderStatus":"5","orderCode":"1000000837918788","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"已完成","orderType":"4","orderTime":"2017-09-22 15:21:51","orderId":"23","taskAddressDetail":"pengbopuhua","orderStatus":"4","orderCode":"1000000398403963","orderTypeText":"任务","taskStatus":"9"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<OrderListBean> orderList;

        public List<OrderListBean> getOrderList() {
            return orderList;
        }

        public void setOrderList(List<OrderListBean> orderList) {
            this.orderList = orderList;
        }

        public static class OrderListBean {
            /**
             * orderStatusText : 已完成
             * orderType : 1
             * orderTime : 2017-10-10 10:52:01
             * orderId : 144
             * orderStatus : 4
             * orderCode : 1000000722518752
             * getAddress : 启
             * orderTypeText : 外卖
             * sendAddress : 海河东路138号
             * expressName : 食堂2
             * taskAddressDetail : pengbopuhua
             * taskStatus : 7
             */

            private String orderStatusText;
            private String orderType;
            private String orderTime;
            private String orderId;
            private String orderStatus;
            private String orderCode;
            private String getAddress;
            private String orderTypeText;
            private String sendAddress;
            private String expressName;
            private String taskAddressDetail;
            private String taskStatus;
            private String expectedDeliveryTime;

            public String getExpectedDeliveryTime() {
                return expectedDeliveryTime;
            }

            public void setExpectedDeliveryTime(String expectedDeliveryTime) {
                this.expectedDeliveryTime = expectedDeliveryTime;
            }

            public String getOrderStatusText() {
                return orderStatusText;
            }

            public void setOrderStatusText(String orderStatusText) {
                this.orderStatusText = orderStatusText;
            }

            public String getOrderType() {
                return orderType;
            }

            public void setOrderType(String orderType) {
                this.orderType = orderType;
            }

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public String getOrderCode() {
                return orderCode;
            }

            public void setOrderCode(String orderCode) {
                this.orderCode = orderCode;
            }

            public String getGetAddress() {
                return getAddress;
            }

            public void setGetAddress(String getAddress) {
                this.getAddress = getAddress;
            }

            public String getOrderTypeText() {
                return orderTypeText;
            }

            public void setOrderTypeText(String orderTypeText) {
                this.orderTypeText = orderTypeText;
            }

            public String getSendAddress() {
                return sendAddress;
            }

            public void setSendAddress(String sendAddress) {
                this.sendAddress = sendAddress;
            }

            public String getExpressName() {
                return expressName;
            }

            public void setExpressName(String expressName) {
                this.expressName = expressName;
            }

            public String getTaskAddressDetail() {
                return taskAddressDetail;
            }

            public void setTaskAddressDetail(String taskAddressDetail) {
                this.taskAddressDetail = taskAddressDetail;
            }

            public String getTaskStatus() {
                return taskStatus;
            }

            public void setTaskStatus(String taskStatus) {
                this.taskStatus = taskStatus;
            }
        }
    }
}
