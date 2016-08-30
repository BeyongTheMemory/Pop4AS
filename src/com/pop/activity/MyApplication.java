package com.pop.activity;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

/**
 * Created by wyouflf on 15/10/28.
 */
public class MyApplication extends Application {

    private  DbManager.DaoConfig daoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
         daoConfig = new DbManager.DaoConfig()
                .setDbName("pop.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //TODO:ddl
//                        try {
//                            db.dropDb();
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

}
