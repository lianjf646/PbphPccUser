package com.pbph.pcc.bean.response;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserStoreInfoBean extends BaseResponseBean {

    /**
     * data : {"StoreInfoEntity":{"storeStarttime":"08:00","storeAddress":"2楼","storeEndtime":"20:05","storeName":"韩家粥铺","storeImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1511590124002.jpg","storeId":"56","addrName":"A区食堂","storeDescribe":""}}
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
         * StoreInfoEntity : {"storeStarttime":"08:00","storeAddress":"2楼","storeEndtime":"20:05","storeName":"韩家粥铺","storeImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1511590124002.jpg","storeId":"56","addrName":"A区食堂","storeDescribe":""}
         */

        private StoreInfoEntityBean StoreInfoEntity;

        public StoreInfoEntityBean getStoreInfoEntity() {
            return StoreInfoEntity;
        }

        public void setStoreInfoEntity(StoreInfoEntityBean StoreInfoEntity) {
            this.StoreInfoEntity = StoreInfoEntity;
        }

        public static class StoreInfoEntityBean {
            /**
             * storeStarttime : 08:00
             * storeAddress : 2楼
             * storeEndtime : 20:05
             * storeName : 韩家粥铺
             * storeImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1511590124002.jpg
             * storeId : 56
             * addrName : A区食堂
             * storeDescribe :
             */

            private String storeStarttime;
            private String storeAddress;
            private String storeEndtime;
            private String storeName;
            private String storeImg;
            private String storeId;
            private String addrName;
            private String storeDescribe;

            public String getStoreStarttime() {
                return storeStarttime;
            }

            public void setStoreStarttime(String storeStarttime) {
                this.storeStarttime = storeStarttime;
            }

            public String getStoreAddress() {
                return storeAddress;
            }

            public void setStoreAddress(String storeAddress) {
                this.storeAddress = storeAddress;
            }

            public String getStoreEndtime() {
                return storeEndtime;
            }

            public void setStoreEndtime(String storeEndtime) {
                this.storeEndtime = storeEndtime;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getStoreImg() {
                return storeImg;
            }

            public void setStoreImg(String storeImg) {
                this.storeImg = storeImg;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getAddrName() {
                return addrName;
            }

            public void setAddrName(String addrName) {
                this.addrName = addrName;
            }

            public String getStoreDescribe() {
                return storeDescribe;
            }

            public void setStoreDescribe(String storeDescribe) {
                this.storeDescribe = storeDescribe;
            }
        }
    }
}
