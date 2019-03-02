package com.hb.myapplication.data.cache;

import java.lang.reflect.Type;

/**
 * Created by haibt3 on 2/24/2017.
 */

public interface ICache {

    void setObject(String key, Object obj);

    <T> T getObject(String key, Class<T> clazz);

    <T> T getObject(String key, Type type);

    boolean contains(String key);

    void delete(String key);

    void clearAll();

}
