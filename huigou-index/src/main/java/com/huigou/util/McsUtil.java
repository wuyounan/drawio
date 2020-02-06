package com.huigou.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.Charsets;

import com.google.common.hash.Hashing;



public class McsUtil {

    private static final String CACHE_FORMATTER = "redis_hana:%s:%s";

    public static final String DASHBOARD_CACHE_KIND = "dashboard";
    
    public static final String CUBES_VIEWER_CACHE_KIND = "cubesViewer";

    public static final String BIZ_ORG_CACHE_KIND = "bizOrg";

    public static byte[] getCacheKey(String kind, String value) {
        String preKey = String.format(CACHE_FORMATTER, kind, value);
        return preKey.getBytes();
    }

    public static String getCacheHashCode(String value) {
        return Hashing.md5().newHasher().putString(value, Charsets.UTF_8).hash().toString();
    }
    
    public static String formatDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }
    
}
