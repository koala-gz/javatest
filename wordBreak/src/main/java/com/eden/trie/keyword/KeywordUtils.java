package com.eden.trie.keyword;

public final class KeywordUtils {

    public static boolean belongToUserDict(Keyword keyword) {
        if(keyword == null || keyword.getDict() == null) {
            return false;
        }

        Dictionary dict = keyword.getDict();
        Dictionary orgnDict = dict.getOriginSource();

        if(dict.getDictType() == DictType.USER_CUSTOMIZED
                || (orgnDict != null && orgnDict.getDictType() == DictType.USER_CUSTOMIZED)){
            return true;
        }

        return false;
    }


}
