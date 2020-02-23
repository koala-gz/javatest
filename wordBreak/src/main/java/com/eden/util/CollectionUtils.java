package com.eden.util;

import java.util.Collection;
import java.util.Set;

public final class CollectionUtils {
    public static boolean isEmpty(Collection col){
        return col == null ? true : col.isEmpty();
    }

    public static boolean isEmpty(Set set){
        return set == null ? true : set.isEmpty();
    }

}
