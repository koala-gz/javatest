package com.eden.util;

import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

    public static boolean isEmpty(String value) {
        return value == null ? true : trim(value).length() <= 0;
    }

    public static String trim(String val) {
        return val == null ? "" : val.trim();
    }

    public static List<String> toLowerCaseList(String words) {
        List<String> wordlist = new ArrayList<>();

        String[] arr = words.split(",");
        for(String word : arr) {
            String item = StringUtils.trim(word);
            if(item.length() > 0) {
                wordlist.add(item.toLowerCase());
            }
        }
        return wordlist;
    }

    public static boolean equals(String value1, String value2) {
        if(value1 == null && value2 == null){
            return true;
        }
        return (value1 != null) ? value1.equals(value2) : value2.equals(value1);
    }
}
