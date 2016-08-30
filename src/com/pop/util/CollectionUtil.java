package com.pop.util;

import java.util.Collection;

/**
 * Created by xugang on 16/8/31.
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
