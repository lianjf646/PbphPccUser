package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class GetUserStoreBean extends BaseResponseBean {

    /**
     * code : 200
     * data : {"TakeoutListEntity":[{"storeStarttime":"11:07:39","storeEndtime":"11:07:44","storeName":"富贵人家","storeImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1504686787345.jpg","storeId":1,"sales":"3"},{"storeStarttime":"09:25","storeEndtime":"10:35","storeName":"aaaaaaaaa","storeImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1504686787345.jpg","storeId":4,"sales":"0"}]}
     * msg : 成功
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TakeoutListEntityBean> TakeoutListEntity;

        public List<TakeoutListEntityBean> getTakeoutListEntity() {
            return TakeoutListEntity;
        }

        public void setTakeoutListEntity(List<TakeoutListEntityBean> TakeoutListEntity) {
            this.TakeoutListEntity = TakeoutListEntity;
        }

        public static class TakeoutListEntityBean {
            /**
             * storeStarttime : 11:07:39
             * storeEndtime : 11:07:44
             * storeName : 富贵人家
             * storeImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1504686787345.jpg
             * storeId : 1
             * sales : 3
             */

            private String storeStarttime;
            private String storeEndtime;
            private String storeName;
            private String storeImg;
            private int storeId;
            private String sales;

            public String getStoreStarttime() {
                return storeStarttime;
            }

            public void setStoreStarttime(String storeStarttime) {
                this.storeStarttime = storeStarttime;
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

            public int getStoreId() {
                return storeId;
            }

            public void setStoreId(int storeId) {
                this.storeId = storeId;
            }

            public String getSales() {
                return sales;
            }

            public void setSales(String sales) {
                this.sales = sales;
            }
        }
    }
}
