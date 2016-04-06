package com.yxst.epic.yixin.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.yxst.epic.yixin.db.Member;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MEMBER.
*/
public class MemberDao extends AbstractDao<Member, Long> {

    public static final String TABLENAME = "MEMBER";

    /**
     * Properties of entity Member.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uid = new Property(1, Long.class, "Uid", false, "UID");
        public final static Property UserName = new Property(2, String.class, "UserName", false, "USER_NAME");
        public final static Property NickName = new Property(3, String.class, "NickName", false, "NICK_NAME");
        public final static Property PYInitial = new Property(4, String.class, "PYInitial", false, "PYINITIAL");
        public final static Property PYQuanPin = new Property(5, String.class, "PYQuanPin", false, "PYQUAN_PIN");
        public final static Property HeadImgUrl = new Property(6, String.class, "HeadImgUrl", false, "HEAD_IMG_URL");
        public final static Property Sex = new Property(7, Integer.class, "Sex", false, "SEX");
        public final static Property Signature = new Property(8, String.class, "Signature", false, "SIGNATURE");
        public final static Property RemarkName = new Property(9, String.class, "RemarkName", false, "REMARK_NAME");
        public final static Property RemarkPYInitial = new Property(10, String.class, "RemarkPYInitial", false, "REMARK_PYINITIAL");
        public final static Property RemarkPYQuanPin = new Property(11, String.class, "RemarkPYQuanPin", false, "REMARK_PYQUAN_PIN");
        public final static Property Province = new Property(12, String.class, "Province", false, "PROVINCE");
        public final static Property City = new Property(13, String.class, "City", false, "CITY");
        public final static Property Alias = new Property(14, String.class, "Alias", false, "ALIAS");
        public final static Property StarFriend = new Property(15, String.class, "StarFriend", false, "STAR_FRIEND");
    };


    public MemberDao(DaoConfig config) {
        super(config);
    }
    
    public MemberDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MEMBER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'UID' INTEGER," + // 1: Uid
                "'USER_NAME' TEXT," + // 2: UserName
                "'NICK_NAME' TEXT," + // 3: NickName
                "'PYINITIAL' TEXT," + // 4: PYInitial
                "'PYQUAN_PIN' TEXT," + // 5: PYQuanPin
                "'HEAD_IMG_URL' TEXT," + // 6: HeadImgUrl
                "'SEX' INTEGER," + // 7: Sex
                "'SIGNATURE' TEXT," + // 8: Signature
                "'REMARK_NAME' TEXT," + // 9: RemarkName
                "'REMARK_PYINITIAL' TEXT," + // 10: RemarkPYInitial
                "'REMARK_PYQUAN_PIN' TEXT," + // 11: RemarkPYQuanPin
                "'PROVINCE' TEXT," + // 12: Province
                "'CITY' TEXT," + // 13: City
                "'ALIAS' TEXT," + // 14: Alias
                "'STAR_FRIEND' TEXT);"); // 15: StarFriend
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MEMBER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Member entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Uid = entity.getUid();
        if (Uid != null) {
            stmt.bindString(2, Uid);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(3, UserName);
        }
 
        String NickName = entity.getNickName();
        if (NickName != null) {
            stmt.bindString(4, NickName);
        }
 
        String PYInitial = entity.getPYInitial();
        if (PYInitial != null) {
            stmt.bindString(5, PYInitial);
        }
 
        String PYQuanPin = entity.getPYQuanPin();
        if (PYQuanPin != null) {
            stmt.bindString(6, PYQuanPin);
        }
 
        String HeadImgUrl = entity.getHeadImgUrl();
        if (HeadImgUrl != null) {
            stmt.bindString(7, HeadImgUrl);
        }
 
        Integer Sex = entity.getSex();
        if (Sex != null) {
            stmt.bindLong(8, Sex);
        }
 
        String Signature = entity.getSignature();
        if (Signature != null) {
            stmt.bindString(9, Signature);
        }
 
        String RemarkName = entity.getRemarkName();
        if (RemarkName != null) {
            stmt.bindString(10, RemarkName);
        }
 
        String RemarkPYInitial = entity.getRemarkPYInitial();
        if (RemarkPYInitial != null) {
            stmt.bindString(11, RemarkPYInitial);
        }
 
        String RemarkPYQuanPin = entity.getRemarkPYQuanPin();
        if (RemarkPYQuanPin != null) {
            stmt.bindString(12, RemarkPYQuanPin);
        }
 
        String Province = entity.getProvince();
        if (Province != null) {
            stmt.bindString(13, Province);
        }
 
        String City = entity.getCity();
        if (City != null) {
            stmt.bindString(14, City);
        }
 
        String Alias = entity.getAlias();
        if (Alias != null) {
            stmt.bindString(15, Alias);
        }
 
        String StarFriend = entity.getStarFriend();
        if (StarFriend != null) {
            stmt.bindString(16, StarFriend);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Member readEntity(Cursor cursor, int offset) {
        Member entity = new Member( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Uid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // UserName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // NickName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // PYInitial
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // PYQuanPin
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // HeadImgUrl
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // Sex
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Signature
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // RemarkName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // RemarkPYInitial
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // RemarkPYQuanPin
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Province
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // City
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // Alias
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // StarFriend
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Member entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNickName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPYInitial(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPYQuanPin(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHeadImgUrl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSex(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setSignature(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRemarkName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setRemarkPYInitial(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRemarkPYQuanPin(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setProvince(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCity(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAlias(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setStarFriend(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Member entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Member entity) {
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
