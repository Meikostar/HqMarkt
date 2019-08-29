package com.hqmy.market.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.db.greendao.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;


/**
 * 数据库管理类
 * Created by dahai on 2018/9/22.
 */

public class OpenHelper extends DaoMaster.DevOpenHelper {
    private static final String TAG = OpenHelper.class.getSimpleName();
    public OpenHelper(Context context, String name) {
        super(context, name);
    }

    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
        LogUtil.d(TAG,"--OpenHelper: " + " oldVersion: " + oldVersion + ", newVersion: " + newVersion);

//        switch (oldVersion) {
//            case 1:
//                String upgradeLastLoginTime = "ALTER TABLE "
//                        + UserInfoDao.TABLENAME + " ADD COLUMN "
//                        + UserInfoDao.Properties.LastLoginTime.columnName + " INTEGER;";
//                db.execSQL(upgradeLastLoginTime);
//            case 2:
//                String upgradeAccountType = "ALTER TABLE "
//                        + UserInfoDao.TABLENAME + " ADD COLUMN "
//                        + UserInfoDao.Properties.AccountType.columnName + " INTEGER;";
//                db.execSQL(upgradeAccountType);
//                break;
//            case 3:
//                String upgradeProductCount = "ALTER TABLE "
//                        + UserInfoDao.TABLENAME + " ADD COLUMN "
//                        + UserInfoDao.Properties.ProductCount.columnName + " INTEGER;";
//                db.execSQL(upgradeProductCount);
//
//                upgradeProductCount = "ALTER TABLE "
//                    + UserInfoDao.TABLENAME + " ADD COLUMN "
//                    + UserInfoDao.Properties.AudioCount.columnName + " INTEGER;";
//                db.execSQL(upgradeProductCount);
//                break;
//            case 4:
//                SongListDao.createTable(db, false);
//                RankingMVDataDao.createTable(db,false);
//                AdvertisingInfoDao.createTable(db, false);
//                break;
//            default:
//                DaoMaster.dropAllTables(db, true);
//                onCreate(db);
//                break;
//        }
    }
}
