package com.hb.myapplication.data.cache;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.vincentbrison.openlibraries.android.dualcache.Builder;
import com.vincentbrison.openlibraries.android.dualcache.CacheSerializer;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;

import java.lang.reflect.Type;


/**
 * Created by haibt3 on 2/27/2017.
 */

public class AppCache implements ICache {

    private static final String CACHE_NAME = "";
    private static final int VERSION = 1;

    private static final int KB = 1024;
    private static final int MB = 1024 * KB;
    private static final int RAM_MAX_SIZE = 5 * MB;
    private static final int DISK_MAX_SIZE = 30 * MB;

    private DualCache<String> mCache;
    private Gson gson = new Gson();

    public AppCache(Context context) {

        mCache = new Builder<>(CACHE_NAME, VERSION, String.class)
                .enableLog()
                .useReferenceInRam(RAM_MAX_SIZE, String::length)
                .useSerializerInDisk(DISK_MAX_SIZE, false, new CacheSerializer<String>() {
                    @Override
                    public String fromString(String data) {
                        return data;
                    }

                    @Override
                    public String toString(String object) {
                        return object;
                    }
                }, context)
                .build();

    }

    @Override
    public void setObject(String key, Object obj) {
        String keyHash = String.valueOf(key.hashCode());
        String value = gson.toJson(obj);
        mCache.put(keyHash, value);
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        String keyHash = String.valueOf(key.hashCode());
        String valueStr = mCache.get(keyHash);
        if (TextUtils.isEmpty(valueStr))
            return null;
        try {
            return gson.fromJson(valueStr, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public <T> T getObject(String key, Type type) {
        String keyHash = String.valueOf(key.hashCode());
        String valueStr = mCache.get(keyHash);
        if (TextUtils.isEmpty(valueStr))
            return null;
        try {
            return gson.fromJson(valueStr, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean contains(String key) {
        String keyHash = String.valueOf(key.hashCode());
        String valueStr = mCache.get(keyHash);
        return !TextUtils.isEmpty(valueStr);
    }

    @Override
    public void delete(String key) {
        String keyHash = String.valueOf(key.hashCode());
        mCache.delete(keyHash);
    }

    @Override
    public void clearAll() {
        mCache.invalidate();
    }
}
