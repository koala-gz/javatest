package com.eden.main;

import com.eden.trie.Emit;
import com.eden.trie.EmitComparatorByDictPriorityAndIntervalPostion;
import com.eden.trie.keyword.Dictionary;
import com.eden.trie.keyword.Keyword;
import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType.REWRITE;
import static com.eden.trie.keyword.KeywordUtils.belongToUserDict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Break words with given dictionary(System dict & User customized dict)
 *          { i, like, sam, sung, samsung, mobile, ice, cream, man go},
 *          {...}
 * If user provide a customized dictionary of valid English words as additional input, and the
 * program will only find in the user customized dictionary
 *
 * @author Shuxiong Zeng, 2020-2-23
 *
 * */
public class WordBreakerStage2 extends WordBreakerStage1 {
    private List<Dictionary> dicts = new ArrayList<>();

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
        SortedSet<Emit> sortedEmits = sortSet(emits, new EmitComparatorByDictPriorityAndIntervalPostion());

        Set<Emit> deletedEmits = new HashSet<>();

        for(Emit e : sortedEmits){
            Keyword keyword = e.getKeyword();
            if(!belongToUserDict(keyword)){
                continue;
            }

            //USER_CUSTOMIZED
            // find intervals which overlaps with the current emit.
            int curstart = e.getStart();
            int curend = e.getEnd();

            for(Emit target : sortedEmits){
                if(e == target){
                    continue;
                }

                int tgestart = target.getStart();
                int tgeend = target.getEnd();

                if(curstart >=  tgestart && curstart <= tgeend
                    ||  curend >=  tgestart && curend <= tgeend ){
                    if (!belongToUserDict(target.getKeyword())){
                        deletedEmits.add(target);
                    }
                }
                if(tgestart > curend){
                    break;
                }
            }
        }

        System.out.println("deletedEmits: \n" + deletedEmits);
        emits.removeAll(deletedEmits);
        return emits;
    }

    @Override
    protected List<String> doLastFilterForOutput(List<String> list) {
        return super.doLastFilterForOutput(list);
    }
}
