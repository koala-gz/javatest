package com.eden.trie;

import com.eden.interval.Intervalable;
import com.eden.trie.Emit;
import com.eden.trie.keyword.Dictionary;

import java.util.Comparator;

/**
 *  Priority : Emit.StartPositon > Emit.EndPosition > Dictionary.Priority.
 *  Move emit ahead, if Priority is more higher.
 */
public class EmitComparatorByDictPriorityAndIntervalPostion implements Comparator<Emit>
{
    @Override
    public int compare(Emit emit1, Emit emit2)
    {
        int comparison = emit1.getStart() - emit2.getStart();

        if (comparison == 0){
            comparison = emit1.getEnd() - emit2.getEnd();
        }
        if (comparison == 0)
        {
            Dictionary dict1 = emit1.getKeyword().getDict();
            Dictionary dict2 = emit2.getKeyword().getDict();
            if(dict1 == null && dict2 == null){ // NewWord
                comparison = 0;
            }
            else if(dict1 == null){
                comparison = 1; // 1 - 0 == (dict2.getPriority() - dict1.getPriority())
            }
            else{
                comparison = dict2.getPriority() - dict1.getPriority();
            }
        }

        return comparison;
    }
}
