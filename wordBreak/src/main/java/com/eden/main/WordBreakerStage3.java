package com.eden.main;

import com.eden.trie.Emit;
import com.eden.trie.EmitComparatorByDictPriorityAndIntervalPostion;
import com.eden.trie.keyword.Dictionary;
import com.eden.trie.keyword.Keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType.APPEND;
import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType.REWRITE;
import static com.eden.trie.keyword.KeywordUtils.belongToUserDict;
/**
 * Break words with given dictionary(System dict & User customized dict)
 *          { i, like, sam, sung, samsung, mobile, ice, cream, man go},
 *          {...}
 * If user provide a customized dictionary of valid English words as additional input, and the
 * program will find all the valid words in the both dictionaries
 *
 * @author Shuxiong Zeng, 2020-2-23
 *
 * */
public class WordBreakerStage3 extends WordBreakerStage1 {
    private List<Dictionary> dicts = new ArrayList<>();
    private BreakStrategy breakStrategy;

    @Override
    public void addToTrie(List<String> wordList, Dictionary dictAll) {
        dicts.add(dictAll);

        super.addToTrie(wordList, dictAll);
    }

    /**
     * remove some emits
     * */
    @Override
    protected Collection<Emit> doFilter(Collection<Emit> emits) {
        return emits;
    }

    @Override
    protected List<String> doLastFilterForOutput(List<String> list) {
        return super.doLastFilterForOutput(list);
    }

}
