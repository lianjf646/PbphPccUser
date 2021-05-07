package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserStoreGoodsBean extends BaseResponseBean {

    /**
     * code : 200
     * data : {"GoodsListEntity":[{"storeGoodsList":[{"storeGoodsIshot":0,"storeGoodsId":9,"storeGoodsDescribe":"贼几把好吃","storeGoodsName":"麻辣烫烤a","storeGoodsPrice":15,"storeGoodsDis":0,"storeGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"},{"storeGoodsIshot":0,"storeGoodsId":10,"storeGoodsName":"烤肉拌饭a","storeGoodsPrice":15,"storeGoodsDis":0,"storeGoodsImg":"beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"}],"typeId":33,"typeName":"小吃"},{"storeGoodsList":[],"typeId":34,"typeName":"面条"},{"storeGoodsList":[],"typeId":36,"typeName":"凉菜"}]}
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
        private List<GoodsListEntityBean> GoodsListEntity;

        public List<GoodsListEntityBean> getGoodsListEntity() {
            return GoodsListEntity;
        }

        public void setGoodsListEntity(List<GoodsListEntityBean> GoodsListEntity) {
            this.GoodsListEntity = GoodsListEntity;
        }

        public static class GoodsListEntityBean {
            /**
             * storeGoodsList : [{"storeGoodsIshot":0,"storeGoodsId":9,"storeGoodsDescribe":"贼几把好吃","storeGoodsName":"麻辣烫烤a","storeGoodsPrice":15,"storeGoodsDis":0,"storeGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"},{"storeGoodsIshot":0,"storeGoodsId":10,"storeGoodsName":"烤肉拌饭a","storeGoodsPrice":15,"storeGoodsDis":0,"storeGoodsImg":"beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg","sales":"0"}]
             * typeId : 33
             * typeName : 小吃
             */

            private int typeId;
            private String typeName;
            private List<StoreGoodsListBean> storeGoodsList;

            public int getTypeId() {
                return typeId;
            }

            public void setTypeId(int typeId) {
                this.typeId = typeId;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public List<StoreGoodsListBean> getStoreGoodsList() {
                return storeGoodsList;
            }

            public void setStoreGoodsList(List<StoreGoodsListBean> storeGoodsList) {
                this.storeGoodsList = storeGoodsList;
            }

            public static class StoreGoodsListBean {
                /**
                 * storeGoodsIshot : 0
                 * storeGoodsId : 9
                 * storeGoodsDescribe : 贼几把好吃
                 * storeGoodsName : 麻辣烫烤a
                 * storeGoodsPrice : 15
                 * storeGoodsDis : 0
                 * storeGoodsImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505120422528.jpg
                 * sales : 0
                 */

                public Long id;

                private int storeGoodsIshot;
                private int storeGoodsId;
                private String storeGoodsDescribe;
                private String storeGoodsName;
                private double storeGoodsPrice;
                private int storeGoodsDis;
                private String storeGoodsImg;
                private String sales;


                private int number = 0;

                public int getStoreGoodsIshot() {
                    return storeGoodsIshot;
                }

                public void setStoreGoodsIshot(int storeGoodsIshot) {
                    this.storeGoodsIshot = storeGoodsIshot;
                }

                public int getStoreGoodsId() {
                    return storeGoodsId;
                }

                public void setStoreGoodsId(int storeGoodsId) {
                    this.storeGoodsId = storeGoodsId;
                }

                public String getStoreGoodsDescribe() {
                    return storeGoodsDescribe;
                }

                public void setStoreGoodsDescribe(String storeGoodsDescribe) {
                    this.storeGoodsDescribe = storeGoodsDescribe;
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

                public int getStoreGoodsDis() {
                    return storeGoodsDis;
                }

                public void setStoreGoodsDis(int storeGoodsDis) {
                    this.storeGoodsDis = storeGoodsDis;
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

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    if (number < 0) number = 0;
                    this.number = number;
                }
            }
        }
    }
}
