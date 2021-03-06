package com.yxst.epic.yixin.db;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig messageDaoConfig;
    private final DaoConfig memberDaoConfig;

    private final MessageDao messageDao;
    private final MemberDao memberDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        memberDaoConfig = daoConfigMap.get(MemberDao.class).clone();
        memberDaoConfig.initIdentityScope(type);

        messageDao = new MessageDao(messageDaoConfig, this);
        memberDao = new MemberDao(memberDaoConfig, this);

        registerDao(Message.class, messageDao);
        registerDao(Member.class, memberDao);
    }
    
    public void clear() {
        messageDaoConfig.getIdentityScope().clear();
        memberDaoConfig.getIdentityScope().clear();
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public MemberDao getMemberDao() {
        return memberDao;
    }

}
