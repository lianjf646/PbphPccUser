package com.pbph.pcc.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GetUserMarketGoodsBean extends BaseResponseBean {


    /**
     * data : {"MarkeGoodsListEntity":[{"marketGoodsList":[{"cityId":0,"goodsId":0,"goodsIdName":"","marketGoodsCheckStatus":0,"marketGoodsDescribe":"百事可乐气真多","marketGoodsId":1,"marketGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505119450027.jpg","marketGoodsName":"百事可乐1","marketGoodsPrice":4.01,"sales":0}],"typeId":49,"typeName":""},{"marketGoodsList":[{"cityId":0,"goodsId":0,"goodsIdName":"","marketGoodsCheckStatus":0,"marketGoodsDescribe":"百事可乐气真多","marketGoodsId":1,"marketGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505119450027.jpg","marketGoodsName":"百事可乐1","marketGoodsPrice":4.01,"sales":0}],"typeId":42,"typeName":"饮料"},{"marketGoodsList":[{"cityId":0,"goodsId":0,"goodsIdName":"","marketGoodsCheckStatus":0,"marketGoodsDescribe":"1","marketGoodsId":6,"marketGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505120323808.png","marketGoodsName":"可口可乐","marketGoodsPrice":1.01,"sales":0}],"typeId":44,"typeName":"小食品"},{"marketGoodsList":[],"typeId":47,"typeName":"辣条"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<MarkeGoodsListEntityBean> MarkeGoodsListEntity;

        public List<MarkeGoodsListEntityBean> getMarkeGoodsListEntity() {
            return MarkeGoodsListEntity;
        }

        public void setMarkeGoodsListEntity(List<MarkeGoodsListEntityBean> MarkeGoodsListEntity) {
            this.MarkeGoodsListEntity = MarkeGoodsListEntity;
        }

        public static class MarkeGoodsListEntityBean {
            /**
             * marketGoodsList : [{"cityId":0,"goodsId":0,"goodsIdName":"","marketGoodsCheckStatus":0,"marketGoodsDescribe":"百事可乐气真多","marketGoodsId":1,"marketGoodsImg":"http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505119450027.jpg","marketGoodsName":"百事可乐1","marketGoodsPrice":4.01,"sales":0}]
             * typeId : 49
             * typeName :
             */

            private int typeId;
            private String typeName;
            private List<MarketGoodsListBean> marketGoodsList;

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

            public List<MarketGoodsListBean> getMarketGoodsList() {
                return marketGoodsList;
            }

            public void setMarketGoodsList(List<MarketGoodsListBean> marketGoodsList) {
                this.marketGoodsList = marketGoodsList;
            }

            public static class MarketGoodsListBean {
                /**
                 * cityId : 0
                 * goodsId : 0
                 * goodsIdName :
                 * marketGoodsCheckStatus : 0
                 * marketGoodsDescribe : 百事可乐气真多
                 * marketGoodsId : 1
                 * marketGoodsImg : http://pccbucket.oss-cn-beijing.aliyuncs.com/pcctest/photoShare/1505119450027.jpg
                 * marketGoodsName : 百事可乐1
                 * marketGoodsPrice : 4.01
                 * sales : 0
                 */

                public Long id;

                private int cityId;

                private int marketGoodsCheckStatus;
                private String marketGoodsDescribe;
                private int marketGoodsId;
                private String marketGoodsImg;
                private String marketGoodsName;
                private double marketGoodsPrice;
                private int sales;

                private int number = 0;

                public int getCityId() {
                    return cityId;
                }

                public void setCityId(int cityId) {
                    this.cityId = cityId;
                }


                public int getMarketGoodsCheckStatus() {
                    return marketGoodsCheckStatus;
                }

                public void setMarketGoodsCheckStatus(int marketGoodsCheckStatus) {
                    this.marketGoodsCheckStatus = marketGoodsCheckStatus;
                }

                public String getMarketGoodsDescribe() {
                    return marketGoodsDescribe;
                }

                public void setMarketGoodsDescribe(String marketGoodsDescribe) {
                    this.marketGoodsDescribe = marketGoodsDescribe;
                }

                public int getMarketGoodsId() {
                    return marketGoodsId;
                }

                public void setMarketGoodsId(int marketGoodsId) {
                    this.marketGoodsId = marketGoodsId;
                }

                public String getMarketGoodsImg() {
                    return marketGoodsImg;
                }

                public void setMarketGoodsImg(String marketGoodsImg) {
                    this.marketGoodsImg = marketGoodsImg;
                }

                public String getMarketGoodsName() {
                    return marketGoodsName;
                }

                public void setMarketGoodsName(String marketGoodsName) {
                    this.marketGoodsName = marketGoodsName;
                }

                public double getMarketGoodsPrice() {
                    return marketGoodsPrice;
                }

                public void setMarketGoodsPrice(double marketGoodsPrice) {
                    this.marketGoodsPrice = marketGoodsPrice;
                }

                public int getSales() {
                    return sales;
                }

                public void setSales(int sales) {
                    this.sales = sales;
                }

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }
            }
        }
    }
}
