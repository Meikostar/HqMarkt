package com.hqmy.market.db;


import com.hqmy.market.db.bean.Groups;
import com.hqmy.market.db.greendao.gen.GroupsDao;

import java.util.List;

/**
 * desc:
 * last modified time:2018/8/21 17:20
 *
 * @author dahai.zhou
 * @since 2018/8/21
 */
public class GroupsDBUtil {
    private static final byte[] slnstanceLock = new byte[0];
    private static GroupsDBUtil mInstance;

    private GroupsDao groupsDao;

    private GroupsDBUtil() {
        groupsDao = DaoManager.getInstance().getDaoSession().getGroupsDao();
    }

    public static GroupsDBUtil getInstance() {
        if (mInstance == null) {
            synchronized (slnstanceLock) {
                mInstance = new GroupsDBUtil();
            }
        }
        return mInstance;
    }

    public List<Groups> loadAll() {
        return groupsDao.loadAll();
    }

    public void deleteAll() {
        groupsDao.deleteAll();
    }

    public void delete(Groups groups) {
        groupsDao.delete(groups);
    }

    public void save(Groups groups) {
        groupsDao.save(groups);
    }

    public Groups queryGroupsId(String groupsId) {
        return groupsDao.queryBuilder().where(GroupsDao.Properties.GroupsId.eq(groupsId)).build().unique();
    }

}
