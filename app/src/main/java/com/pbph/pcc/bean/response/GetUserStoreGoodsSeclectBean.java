package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserStoreGoodsSeclectBean extends BaseResponseBean {

    /**
     * data : {"StoreListEntity":[{"goodsList":[{"storeGoodsId":9,"storeGoodsName":"麻辣烫烤a","storeGoodsPrice":15.01,"storeGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"},{"storeGoodsId":10,"storeGoodsName":"烤肉拌饭a","storeGoodsPrice":15.07,"storeGoodsImg":"beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"}],"sales":"3","storeEndtime":"11:07:44","storeId":1,"storeImg":"","storeName":"富贵人家","storeStarttime":"11:07:39"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<StoreListEntityBean> StoreListEntity;

        public List<StoreListEntityBean> getStoreListEntity() {
            return StoreListEntity;
        }

        public void setStoreListEntity(List<StoreListEntityBean> StoreListEntity) {
            this.StoreListEntity = StoreListEntity;
        }

        public static class StoreListEntityBean {
            /**
             * goodsList : [{"storeGoodsId":9,"storeGoodsName":"麻辣烫烤a","storeGoodsPrice":15.01,"storeGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"},{"storeGoodsId":10,"storeGoodsName":"烤肉拌饭a","storeGoodsPrice":15.07,"storeGoodsImg":"beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"}]
             * sales : 3
             * storeEndtime : 11:07:44
             * storeId : 1
             * storeImg :
             * storeName : 富贵人家
             * storeStarttime : 11:07:39
             */

            private String sales;
            private String storeEndtime;
            private int storeId;
            private String storeImg;
            private String storeName;
            private String storeStarttime;
            private List<GoodsListBean> goodsList;

            public String getSales() {
                return sales;
            }

            public void setSales(String sales) {
                this.sales = sales;
            }

            public String getStoreEndtime() {
                return storeEndtime;
            }

            public void setStoreEndtime(String storeEndtime) {
                this.storeEndtime = storeEndtime;
            }

            public int getStoreId() {
                return storeId;
            }

            public void setStoreId(int storeId) {
                this.storeId = storeId;
            }

            public String getStoreImg() {
                return storeImg;
            }

            public void setStoreImg(String storeImg) {
                this.storeImg = storeImg;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getStoreStarttime() {
                return storeStarttime;
            }

            public void setStoreStarttime(String storeStarttime) {
                this.storeStarttime = storeStarttime;
            }

            public List<GoodsListBean> getGoodsList() {
                return goodsList;
            }

            public void setGoodsList(List<GoodsListBean> goodsList) {
                this.goodsList = goodsList;
            }

            public static class GoodsListBean {
                /**
                 * storeGoodsId : 9
                 * storeGoodsName : 麻辣烫烤a
                 * storeGoodsPrice : 15.01
                 * storeGoodsImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg
                 * sales : 0
                 */

                private int storeGoodsId;
                private String storeGoodsName;
                private double storeGoodsPrice;
                private String storeGoodsImg;
                private String sales;

                public int getStoreGoodsId() {
                    return storeGoodsId;
                }

                public void setStoreGoodsId(int storeGoodsId) {
                    this.storeGoodsId = storeGoodsId;
                }

                public String getStoreGoodsName() {
                    return storeGoodsName;
                }

                public void setStoreGoodsName(String storeGoodsName) {
                    this.storeGoodsName = storeGoodsName;
                }

                public double getStoreGoodsPrice() {
                    return storeGoodsPrice;
                }

                public void setStoreGoodsPrice(double storeGoodsPrice) {
                    this.storeGoodsPrice = storeGoodsPrice;
                }

                public String getStoreGoodsImg() {
                    return storeGoodsImg;
                }

                public void setStoreGoodsImg(String storeGoodsImg) {
                    this.storeGoodsImg = storeGoodsImg;
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
}
