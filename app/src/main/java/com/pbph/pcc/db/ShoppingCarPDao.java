package com.pbph.pcc.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.pbph.pcc.db.ShoppingCarP;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SHOPPING_CAR_P.
*/
public class ShoppingCarPDao extends AbstractDao<ShoppingCarP, Long> {

    public static final String TABLENAME = "SHOPPING_CAR_P";

    /**
     * Properties of entity ShoppingCarP.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ShopName = new Property(1, String.class, "shopName", false, "SHOP_NAME");
        public final static Property ShopId = new Property(2, String.class, "shopId", false, "SHOP_ID");
        public final static Property SchoolId = new Property(3, String.class, "schoolId", false, "SCHOOL_ID");
        public final static Property UserId = new Property(4, String.class, "userId", false, "USER_ID");
        public final static Property ShopImgUrl = new Property(5, String.class, "shopImgUrl", false, "SHOP_IMG_URL");
        public final static Property ShopIsChecked = new Property(6, Boolean.class, "shopIsChecked", false, "SHOP_IS_CHECKED");
        public final static Property TotlePrice = new Property(7, Double.class, "totlePrice", false, "TOTLE_PRICE");
        public final static Property ShippingAmount = new Property(8, Double.class, "shippingAmount", false, "SHIPPING_AMOUNT");
        public final static Property TakeStoreNum = new Property(9, Integer.class, "takeStoreNum", false, "TAKE_STORE_NUM");
        public final static Property TakeSoodsPrice = new Property(10, Double.class, "takeSoodsPrice", false, "TAKE_SOODS_PRICE");
        public final static Property TakeSoodsPrices = new Property(11, Double.class, "takeSoodsPrices", false, "TAKE_SOODS_PRICES");
    };


    public ShoppingCarPDao(DaoConfig config) {
        super(config);
    }
    
    public ShoppingCarPDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SHOPPING_CAR_P' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SHOP_NAME' TEXT," + // 1: shopName
                "'SHOP_ID' TEXT," + // 2: shopId
                "'SCHOOL_ID' TEXT," + // 3: schoolId
                "'USER_ID' TEXT," + // 4: userId
                "'SHOP_IMG_URL' TEXT," + // 5: shopImgUrl
                "'SHOP_IS_CHECKED' INTEGER," + // 6: shopIsChecked
                "'TOTLE_PRICE' REAL," + // 7: totlePrice
                "'SHIPPING_AMOUNT' REAL," + // 8: shippingAmount
                "'TAKE_STORE_NUM' INTEGER," + // 9: takeStoreNum
                "'TAKE_SOODS_PRICE' REAL," + // 10: takeSoodsPrice
                "'TAKE_SOODS_PRICES' REAL);"); // 11: takeSoodsPrices
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SHOPPING_CAR_P'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ShoppingCarP entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String shopName = entity.getShopName();
        if (shopName != null) {
            stmt.bindString(2, shopName);
        }
 
        String shopId = entity.getShopId();
        if (shopId != null) {
            stmt.bindString(3, shopId);
        }
 
        String schoolId = entity.getSchoolId();
        if (schoolId != null) {
            stmt.bindString(4, schoolId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(5, userId);
        }
 
        String shopImgUrl = entity.getShopImgUrl();
        if (shopImgUrl != null) {
            stmt.bindString(6, shopImgUrl);
        }
 
        Boolean shopIsChecked = entity.getShopIsChecked();
        if (shopIsChecked != null) {
            stmt.bindLong(7, shopIsChecked ? 1l: 0l);
        }
 
        Double totlePrice = entity.getTotlePrice();
        if (totlePrice != null) {
            stmt.bindDouble(8, totlePrice);
        }
 
        Double shippingAmount = entity.getShippingAmount();
        if (shippingAmount != null) {
            stmt.bindDouble(9, shippingAmount);
        }
 
        Integer takeStoreNum = entity.getTakeStoreNum();
        if (takeStoreNum != null) {
            stmt.bindLong(10, takeStoreNum);
        }
 
        Double takeSoodsPrice = entity.getTakeSoodsPrice();
        if (takeSoodsPrice != null) {
            stmt.bindDouble(11, takeSoodsPrice);
        }
 
        Double takeSoodsPrices = entity.getTakeSoodsPrices();
        if (takeSoodsPrices != null) {
            stmt.bindDouble(12, takeSoodsPrices);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ShoppingCarP readEntity(Cursor cursor, int offset) {
        ShoppingCarP entity = new ShoppingCarP( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // shopName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // shopId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // schoolId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // userId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // shopImgUrl
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // shopIsChecked
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // totlePrice
            cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8), // shippingAmount
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // takeStoreNum
            cursor.isNull(offset + 10) ? null : cursor.getDouble(offset + 10), // takeSoodsPrice
            cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11) // takeSoodsPrices
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ShoppingCarP entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setShopName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setShopId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSchoolId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUserId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setShopImgUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setShopIsChecked(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setTotlePrice(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setShippingAmount(cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8));
        entity.setTakeStoreNum(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setTakeSoodsPrice(cursor.isNull(offset + 10) ? null : cursor.getDouble(offset + 10));
        entity.setTakeSoodsPrices(cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ShoppingCarP entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ShoppingCarP entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}