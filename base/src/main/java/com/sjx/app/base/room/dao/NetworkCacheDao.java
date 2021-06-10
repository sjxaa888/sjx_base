package com.sjx.app.base.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sjx.app.base.room.Entity.NetworkCache;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface NetworkCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(NetworkCache... networkCaches);

    //删除一个
    @Delete
    void delete(NetworkCache networkCache);

    //删除全部
    @Query("DELETE FROM networkcache")
    void deleteAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateAll(NetworkCache... networkCaches);

//    @Query("SELECT * FROM networkcache WHERE `key` == :cacheKey LIMIT 1")
//    NetworkCache findByCacheKey(String cacheKey);

    //配合rxjava
    @Query("SELECT * FROM networkcache WHERE `key` == :cacheKey LIMIT 1")
    Single<NetworkCache> findByCacheKey(String cacheKey);

    @Query("SELECT * FROM networkcache")
    Single<List<NetworkCache>> getAll();

    /*
     * onConflict：默认值是OnConflictStrategy.ABORT，表示当插入有冲突的时候的处理策略。
     *      1. OnConflictStrategy.REPLACE：冲突策略是取代旧数据同时继续事务。
     *
     *      2. OnConflictStrategy.ROLLBACK：冲突策略是回滚事务。
     *
     *      3. OnConflictStrategy.ABORT：冲突策略是终止事务。
     *
     *      4. OnConflictStrategy.FAIL：冲突策略是事务失败。
     *
     *      5. OnConflictStrategy.IGNORE：冲突策略是忽略冲突。
     */

    /*
     * Maybe
     *      1. 当数据库中没有用户，查询将不返回行，Maybe会完成。
     *      2. 当数据库中有一个用户，Maybe会触发onSuccess，然后将完成。
     *      3. Maybe完成后，如果这个用户有更新，什么都不会发生。
     *
     * Single
     *      1. 数据库没有User，Single会触发onError(EmptyResultSetException.class)
     *      2. 数据库中有User，Single会触发onSuccess
     *      3. Single完成后，User的数据更新了，不会发生任何事
     *
     * Flowable/Observable
     *      1. 数据库没有User，查询不会返回数据，不会调用onNext也不会调用onError
     *      2. 数据库中有User，会调用onNext
     *      3. 每次User的数据更新后，Flowable会自动发送出去
     */
}
