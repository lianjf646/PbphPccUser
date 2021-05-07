package com.pbph.pcc.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.pbph.pcc.db.ShopSearchRecord;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SHOP_SEARCH_RECORD.
*/
public class ShopSearchRecordDao extends AbstractDao<ShopSearchRecord, Long> {

    public static final String TABLENAME = "SHOP_SEARCH_RECORD";

    /**
     * Properties of entity ShopSearchRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SearchName = new Property(1, String.class, "searchName", false, "SEARCH_NAME");
        public final static Property CreateTime = new Property(2, java.util.Date.class, "createTime", false, "CREATE_TIME");
        public final static Property SearchType = new Property(3, Integer.class, "searchType", false, "SEARCH_TYPE");
    };


    public ShopSearchRecordDao(DaoConfig config) {
        super(config);
    }
    
    public ShopSearchRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SHOP_SEARCH_RECORD' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SEARCH_NAME' TEXT," + // 1: searchName
                "'CREATE_TIME' INTEGER," + // 2: createTime
                "'SEARCH_TYPE' INTEGER);"); // 3: searchType
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SHOP_SEARCH_RECORD'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ShopSearchRecord entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String searchName = entity.getSearchName();
        if (searchName != null) {
            stmt.bindString(2, searchName);
        }
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(3, createTime.getTime());
        }
 
        Integer searchType = entity.getSearchType();
        if (searchType != null) {
            stmt.bindLong(4, searchType);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ShopSearchRecord readEntity(Cursor cursor, int offset) {
        ShopSearchRecord entity = new ShopSearchRecord( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // searchName
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // createTime
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // searchType
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ShopSearchRecord entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSearchName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreateTime(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setSearchType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ShopSearchRecord entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ShopSearchRecord entity) {
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