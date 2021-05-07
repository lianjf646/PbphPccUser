package com.pbph.pcc.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

import java.io.File;

public class GreenDaoGenerator {

    public static final int VERSION = 53;
    public static final String GREEN_DAO_CODE_PATH = "../PbphPccUser/app/src/main/java";

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(VERSION, "com.pbph.pcc.db");

        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("userName");
        user.addStringProperty("userId");
        user.addStringProperty("headImgUrl");

//商店列表
        Entity shoppingCarP = schema.addEntity("ShoppingCarP");
        shoppingCarP.addIdProperty();
        shoppingCarP.addStringProperty("shopName");
        shoppingCarP.addStringProperty("shopId");
        shoppingCarP.addStringProperty("schoolId");
        shoppingCarP.addStringProperty("userId");
        shoppingCarP.addStringProperty("shopImgUrl");//店铺img
        shoppingCarP.addBooleanProperty("shopIsChecked");//是否选中
        shoppingCarP.addDoubleProperty("totlePrice");//商品总价
        shoppingCarP.addDoubleProperty("shippingAmount");//配送费
        shoppingCarP.addIntProperty("takeStoreNum");//多少件以内
        shoppingCarP.addDoubleProperty("takeSoodsPrice");//多少钱
        shoppingCarP.addDoubleProperty("takeSoodsPrices");//超出部分每件多少钱

//        商品列表
        Entity shoppingCarC = schema.addEntity("ShoppingCarC");
        shoppingCarC.addIdProperty();
        shoppingCarC.addStringProperty("shopId");
        shoppingCarC.addStringProperty("goodName");//商品名称
        shoppingCarC.addStringProperty("goodId");//商品id
        shoppingCarC.addStringProperty("schoolId");//学校id
        shoppingCarC.addStringProperty("userId");//用户id
        shoppingCarC.addStringProperty("goodImgUrl");//商品img
        shoppingCarC.addDoubleProperty("goodPrice");//商品价格
        shoppingCarC.addBooleanProperty("goodIsChecked");//是否选中
        shoppingCarC.addIntProperty("goodNum");//商品数量

        //搜索记录
        Entity shopSearchRecord = schema.addEntity("ShopSearchRecord");
        shopSearchRecord.addIdProperty();
        shopSearchRecord.addStringProperty("searchName");//记录名称
        shopSearchRecord.addDateProperty("createTime");
        shopSearchRecord.addIntProperty("searchType");//搜索类型1外卖 2 超市


        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }
}
