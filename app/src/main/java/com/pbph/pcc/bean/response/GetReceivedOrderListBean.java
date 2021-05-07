package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetReceivedOrderListBean extends BaseResponseBean {

    /**
     * data : {"orderList":[{"orderStatusText":"已完成","orderType":"1","orderTime":"2017-10-10 10:51:53","orderId":"144","gettingAmount":"2.00","orderStatus":"4","orderCode":"1000000722518752","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号","expressName":"食堂2","taskAddressDetail":"pengbopuhua","taskStatus":"7"},{"orderStatusText":"已评价","orderType":"1","orderTime":"2017-09-18 09:24:12","orderId":"5","gettingAmount":"2.00","orderStatus":"5","orderCode":"1234567890123452","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"68","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000151077036","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"138","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000001104469127","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"137","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000001764273168","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"136","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000177695586","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"133","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000426579769","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"106","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000138904855","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"105","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000002011847692","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"104","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000074351947","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"103","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000648938060","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"102","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000001437556811","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"67","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000001344923356","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"75","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000389332707","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"74","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000001251093126","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"73","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000719389593","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"72","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000002103661914","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"71","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000001879406553","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"70","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000002145728104","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"69","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000205556833","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"1","orderTime":"","orderId":"101","gettingAmount":"2.00","orderStatus":"1","orderCode":"1000000315727004","getAddress":"启","orderTypeText":"外卖","sendAddress":"海河东路138号"},{"orderStatusText":"已接单","orderType":"2","orderTime":"2017-09-21 21:44:12","orderId":"14","gettingAmount":"3.00","orderStatus":"2","orderCode":"1234567890123443","orderTypeText":"超市","sendAddress":"海河东路138号"},{"orderStatusText":"已评价","orderType":"2","orderTime":"2017-09-08 21:41:46","orderId":"10","gettingAmount":"3.00","orderStatus":"5","orderCode":"1234567890123447","orderTypeText":"超市","sendAddress":"海河东路138号"},{"orderStatusText":"已完成","orderType":"2","orderTime":"2017-09-05 21:41:12","orderId":"6","gettingAmount":"1.00","orderStatus":"4","orderCode":"1234567890123451","orderTypeText":"超市","sendAddress":"海河东路138号"},{"orderStatusText":"已接单","orderType":"2","orderTime":"2017-09-02 21:45:12","orderId":"2","gettingAmount":"1.00","orderStatus":"2","orderCode":"1234567890123455","orderTypeText":"超市","sendAddress":"海河东路138号"},{"orderStatusText":"待接单","orderType":"3","expressName":"食堂2","orderTime":"2017-09-22 15:40:24","orderId":"19","gettingAmount":"0.00","orderStatus":"1","orderCode":"1000001287323233","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"已评价","orderType":"3","expressName":"顺丰","orderTime":"2017-09-20 21:41:12","orderId":"15","gettingAmount":"1.00","orderStatus":"5","orderCode":"1234567890123442","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"已取消","orderType":"3","expressName":"食堂2","orderTime":"2017-09-05 21:41:12","orderId":"7","gettingAmount":"1.00","orderStatus":"0","orderCode":"1234567890123450","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"已送达","orderType":"3","expressName":"顺丰","orderTime":"2017-09-02 21:41:12","orderId":"3","gettingAmount":"1.00","orderStatus":"3","orderCode":"1234567890123454","orderTypeText":"快递","sendAddress":"海河东路138号"},{"orderStatusText":"已取消","orderType":"4","orderTime":"2017-09-22 15:40:24","orderId":"25","gettingAmount":"0.00","taskAddressDetail":"pengbopuhua","orderStatus":"6","orderCode":"1000000853766042","orderTypeText":"任务","taskStatus":"7"},{"orderStatusText":"已送达","orderType":"4","orderTime":"2017-09-19 21:42:12","orderId":"16","gettingAmount":"1.00","taskAddressDetail":"广泛大概","orderStatus":"3","orderCode":"1234567890123441","orderTypeText":"任务","taskStatus":"9"},{"orderStatusText":"已接单","orderType":"4","orderTime":"2017-09-10 21:42:56","orderId":"12","gettingAmount":"23.00","taskAddressDetail":"而","orderStatus":"2","orderCode":"1234567890123445","orderTypeText":"任务","taskStatus":"8"},{"orderStatusText":"待接单","orderType":"4","orderTime":"2017-09-06 21:41:12","orderId":"8","gettingAmount":"1.00","taskAddressDetail":"二位","orderStatus":"1","orderCode":"1234567890123449","orderTypeText":"任务","taskStatus":"9"},{"orderStatusText":"已取消","orderType":"4","orderTime":"2017-09-02 21:41:12","orderId":"4","gettingAmount":"1.00","taskAddressDetail":"啊还是看大家哈","orderStatus":"6","orderCode":"1234567890123453","orderTypeText":"任务","taskStatus":"9"}]}
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
             * orderTime : 2017-10-10 10:51:53
             * orderId : 144
             * gettingAmount : 2.00
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
            private String gettingAmount;
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

            public String getGettingAmount() {
                return gettingAmount;
            }

            public void setGettingAmount(String gettingAmount) {
                this.gettingAmount = gettingAmount;
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
