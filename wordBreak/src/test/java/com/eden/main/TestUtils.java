package com.eden.main;

import com.eden.trie.keyword.DictType;
import com.eden.trie.keyword.Dictionary;

import static com.eden.trie.keyword.Dictionary.DEFUALT_PRIORITY;
import static com.eden.trie.keyword.Dictionary.DICT_BASE_ID;
import static com.eden.trie.keyword.Dictionary.createDictionary;

public class TestUtils {

    public static Dictionary newSystemDictionary(int idx, String dictName){
        return createDictionary(DICT_BASE_ID + idx,
                DictType.SYSTEM,
                dictName,
                DEFUALT_PRIORITY);
    }

    public static Dictionary newUserDictionary(int idx, String dictName, int priority){
        return createDictionary(DICT_BASE_ID + idx,
                DictType.USER_CUSTOMIZED,
                dictName,
                priority);
    }
}
