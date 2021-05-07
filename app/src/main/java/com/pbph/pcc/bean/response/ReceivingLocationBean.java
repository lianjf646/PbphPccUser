package com.pbph.pcc.bean.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */

public class ReceivingLocationBean extends BaseResponseBean {


    /**
     * code : 200
     * data : [{"addrId":"18","addrName":"小区","addrTypeId":"4","defaultAddress":"0","raddressName":"我在找找","receiveId":"55","receiveName":"看看咯","receivePhone":"111","receiveSex":"1","userId":"1"},{"addrId":"18","addrName":"小区","addrTypeId":"4","defaultAddress":"0","raddressName":"我在找找哦摸摸","receiveId":"56","receiveName":"看看咯陌陌","receivePhone":"1112222","receiveSex":"0","userId":"1"},{"addrId":"13","addrName":"圆通","addrTypeId":"5","defaultAddress":"0","raddressName":"火影村","receiveId":"61","receiveName":"卡卡西","receivePhone":"15636899875","receiveSex":"1","userId":"1"},{"addrId":"18","addrName":"小区","addrTypeId":"4","defaultAddress":"0","raddressName":"哦哦哦摸摸","receiveId":"63","receiveName":"额的","receivePhone":"15636899875","receiveSex":"1","userId":"1"},{"addrId":"18","addrName":"小区","addrTypeId":"4","defaultAddress":"0","raddressName":"小游戏的","receiveId":"64","receiveName":"悟空","receivePhone":"15632122554","receiveSex":"1","userId":"1"},{"addrId":"18","addrName":"小区","addrTypeId":"4","defaultAddress":"0","raddressName":"中央音乐学院","receiveId":"65","receiveName":"短笛","receivePhone":"15636899875","receiveSex":"1","userId":"1"},{"addrId":"18","addrName":"小区","addrTypeId":"4","defaultAddress":"1","raddressName":"我在找找","receiveId":"66","receiveName":"我在找找","receivePhone":"15632141478","receiveSex":"1","userId":"1"}]
     * msg : 成功
     */


    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * addrId : 18raddressName
         * addrName : 小区
         * addrTypeId : 4
         * defaultAddress : 0
         * raddressName : 我在找找
         * receiveId : 55
         * receiveName : 看看咯
         * receivePhone : 111
         * receiveSex : 1
         * userId : 1
         */

        private String addrId;
        private String addrName;
        private String addrTypeId;
        private String defaultAddress;
        private String raddressName;
        private String receiveId;
        private String receiveName;
        private String receivePhone;
        private String receiveSex;
        private String userId;

        public String getAddrId() {
            return addrId;
        }

        public void setAddrId(String addrId) {
            this.addrId = addrId;
        }

        public String getAddrName() {
            return addrName;
        }

        public void setAddrName(String addrName) {
            this.addrName = addrName;
        }

        public String getAddrTypeId() {
            return addrTypeId;
        }

        public void setAddrTypeId(String addrTypeId) {
            this.addrTypeId = addrTypeId;
        }

        public String getDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(String defaultAddress) {
            this.defaultAddress = defaultAddress;
        }

        public String getRaddressName() {
            return raddressName;
        }

        public void setRaddressName(String raddressName) {
            this.raddressName = raddressName;
        }

        public String getReceiveId() {
            return receiveId;
        }

        public void setReceiveId(String receiveId) {
            this.receiveId = receiveId;
        }

        public String getReceiveName() {
            return receiveName;
        }

        public void setReceiveName(String receiveName) {
            this.receiveName = receiveName;
        }

        public String getReceivePhone() {
            return receivePhone;
        }

        public void setReceivePhone(String receivePhone) {
            this.receivePhone = receivePhone;
        }

        public String getReceiveSex() {
            return receiveSex;
        }

        public void setReceiveSex(String receiveSex) {
            this.receiveSex = receiveSex;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
