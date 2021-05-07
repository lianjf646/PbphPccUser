package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/26.
 */

public class GetMessageWebResponseBean extends BaseResponseBean {


    /**
     * code : 200
     * data : [{"messageIsRead":"0","messageType":"2","msg_content":"代理申请成功，请进入代理端体验您的权利吧！","messageId":"445","registration_id":"170976fa8a84ad467e0","messageCreateTime":"2017-10-23 15:14:15","title":"申请成功"},{"orderType":"","messageIsRead":"0","messageType":"1","orderId":"","msg_content":"订单发布成功，配送员选择中。","messageId":"457","registration_id":"160a3797c8006b3d61f","orderStatus":"","messageCreateTime":"2017-10-23 16:47:18","orderCode":"1000001103322156","title":"订单已提交"},{"orderType":"","messageIsRead":"0","messageType":"1","orderId":"","msg_content":"欢迎您对小阿光的支持，欢迎再次光临。","messageId":"458","registration_id":"160a3797c8006b3d61f","orderStatus":"","messageCreateTime":"2017-10-23 16:48:58","orderCode":"1000001103322156","title":"订单已完成"},{"orderType":"","messageIsRead":"0","messageType":"1","orderId":"","msg_content":"你的服务得到了评价呦，快去看一下吧！","messageId":"461","registration_id":"160a3797c8006b3d61f","orderStatus":"","messageCreateTime":"2017-10-23 16:50:17","orderCode":"1000001103322156","title":"订单已评价"},{"orderType":"","messageIsRead":"0","messageType":"1","orderId":"","msg_content":"订单已送达，请尽快确认收货，如有问题请咨询客服。","messageId":"462","registration_id":"160a3797c8006b3d61f","orderStatus":"","messageCreateTime":"2017-10-23 16:51:49","orderCode":"1000001103322156","title":"订单已送达"},{"recordId":"77","messageIsRead":"0","messageType":"4","msg_content":"请到一食堂接取订单，赚取更多的收益吧！","messageId":"463","registration_id":"160a3797c8006b3d61f","messageCreateTime":"2017-10-23 16:54:11","title":"推荐派单"},{"orderType":"1","messageIsRead":"0","messageType":"1","orderId":"566","msg_content":"你的服务得到了评价呦，快去看一下吧！","messageId":"481","registration_id":"160a3797c8006b3d61f","orderStatus":"5","messageCreateTime":"2017-10-24 09:43:27","orderCode":"1000001280495763","title":"订单已评价"}]
     * msg : 成功
     */


    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * messageIsRead : 0
         * messageType : 2
         * msg_content : 代理申请成功，请进入代理端体验您的权利吧！
         * messageId : 445
         * registration_id : 170976fa8a84ad467e0
         * messageCreateTime : 2017-10-23 15:14:15
         * title : 申请成功
         * orderType :
         * orderId :
         * orderStatus :
         * orderCode : 1000001103322156
         * recordId : 77
         */

        private String messageIsRead;
        private String messageType;
        private String msg_content;
        private String messageId;
        private String registration_id;
        private String messageCreateTime;
        private String title;
        private String orderType;
        private String orderId = "";
        private String orderStatus;
        private String orderCode;
        private String recordId;

        public String getMessageIsRead() {
            return messageIsRead;
        }

        public void setMessageIsRead(String messageIsRead) {
            this.messageIsRead = messageIsRead;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMsg_content() {
            return msg_content;
        }

        public void setMsg_content(String msg_content) {
            this.msg_content = msg_content;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getRegistration_id() {
            return registration_id;
        }

        public void setRegistration_id(String registration_id) {
            this.registration_id = registration_id;
        }

        public String getMessageCreateTime() {
            return messageCreateTime;
        }

        public void setMessageCreateTime(String messageCreateTime) {
            this.messageCreateTime = messageCreateTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
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

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }
    }
}
