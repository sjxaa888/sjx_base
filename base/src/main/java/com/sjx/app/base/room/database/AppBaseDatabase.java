package com.sjx.app.base.room.database;

import com.sjx.app.base.room.Entity.NetworkCache;
import com.sjx.app.base.room.dao.NetworkCacheDao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NetworkCache.class}, version = 1)

public abstract class AppBaseDatabase extends RoomDatabase {

    public abstract NetworkCacheDao networkCacheDao();

}
