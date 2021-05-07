package com.pbph.pcc.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.pbph.pcc.db.ShoppingCarC;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SHOPPING_CAR_C.
*/
public class ShoppingCarCDao extends AbstractDao<ShoppingCarC, Long> {

    public static final String TABLENAME = "SHOPPING_CAR_C";

    /**
     * Properties of entity ShoppingCarC.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ShopId = new Property(1, String.class, "shopId", false, "SHOP_ID");
        public final static Property GoodName = new Property(2, String.class, "goodName", false, "GOOD_NAME");
        public final static Property GoodId = new Property(3, String.class, "goodId", false, "GOOD_ID");
        public final static Property SchoolId = new Property(4, String.class, "schoolId", false, "SCHOOL_ID");
        public final static Property UserId = new Property(5, String.class, "userId", false, "USER_ID");
        public final static Property GoodImgUrl = new Property(6, String.class, "goodImgUrl", false, "GOOD_IMG_URL");
        public final static Property GoodPrice = new Property(7, Double.class, "goodPrice", false, "GOOD_PRICE");
        public final static Property GoodIsChecked = new Property(8, Boolean.class, "goodIsChecked", false, "GOOD_IS_CHECKED");
        public final static Property GoodNum = new Property(9, Integer.class, "goodNum", false, "GOOD_NUM");
    };


    public ShoppingCarCDao(DaoConfig config) {
        super(config);
    }
    
    public ShoppingCarCDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SHOPPING_CAR_C' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SHOP_ID' TEXT," + // 1: shopId
                "'GOOD_NAME' TEXT," + // 2: goodName
                "'GOOD_ID' TEXT," + // 3: goodId
                "'SCHOOL_ID' TEXT," + // 4: schoolId
                "'USER_ID' TEXT," + // 5: userId
                "'GOOD_IMG_URL' TEXT," + // 6: goodImgUrl
                "'GOOD_PRICE' REAL," + // 7: goodPrice
                "'GOOD_IS_CHECKED' INTEGER," + // 8: goodIsChecked
                "'GOOD_NUM' INTEGER);"); // 9: goodNum
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SHOPPING_CAR_C'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ShoppingCarC entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String shopId = entity.getShopId();
        if (shopId != null) {
            stmt.bindString(2, shopId);
        }
 
        String goodName = entity.getGoodName();
        if (goodName != null) {
            stmt.bindString(3, goodName);
        }
 
        String goodId = entity.getGoodId();
        if (goodId != null) {
            stmt.bindString(4, goodId);
        }
 
        String schoolId = entity.getSchoolId();
        if (schoolId != null) {
            stmt.bindString(5, schoolId);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(6, userId);
        }
 
        String goodImgUrl = entity.getGoodImgUrl();
        if (goodImgUrl != null) {
            stmt.bindString(7, goodImgUrl);
        }
 
        Double goodPrice = entity.getGoodPrice();
        if (goodPrice != null) {
            stmt.bindDouble(8, goodPrice);
        }
 
        Boolean goodIsChecked = entity.getGoodIsChecked();
        if (goodIsChecked != null) {
            stmt.bindLong(9, goodIsChecked ? 1l: 0l);
        }
 
        Integer goodNum = entity.getGoodNum();
        if (goodNum != null) {
            stmt.bindLong(10, goodNum);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ShoppingCarC readEntity(Cursor cursor, int offset) {
        ShoppingCarC entity = new ShoppingCarC( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // shopId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // goodName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // goodId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // schoolId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // userId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // goodImgUrl
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // goodPrice
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0, // goodIsChecked
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9) // goodNum
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ShoppingCarC entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setShopId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setGoodName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGoodId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSchoolId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGoodImgUrl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setGoodPrice(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setGoodIsChecked(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
        entity.setGoodNum(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ShoppingCarC entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ShoppingCarC entity) {
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
