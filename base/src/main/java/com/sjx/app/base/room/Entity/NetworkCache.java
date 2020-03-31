package com.sjx.app.base.room.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NetworkCache {
    @PrimaryKey
    @NonNull
    public String key;

    @ColumnInfo(name = "data")
    public String data;
}
