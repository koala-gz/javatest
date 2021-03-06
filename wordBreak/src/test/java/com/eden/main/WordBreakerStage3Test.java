package com.eden.main;

import com.eden.trie.keyword.DictType;
import com.eden.trie.keyword.Dictionary;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.eden.main.TestUtils.newSystemDictionary;
import static com.eden.main.TestUtils.newUserDictionary;
import static com.eden.trie.keyword.Dictionary.DEFUALT_PRIORITY;
import static com.eden.trie.keyword.Dictionary.DICT_BASE_ID;
import static com.eden.trie.keyword.Dictionary.createDictionary;
import static com.eden.util.StringUtils.toLowerCaseList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

//import static junit.framework.Assert.assertEquals;

public class WordBreakerStage3Test {

    @Before
    public void setup(){
        // init global dict
    }

    @Test
    public void wordBreakTestCase1() {

        String systemWords = "i, like, sam, sung, samsung, mobile, ice, cream, and, man go";
        List<String> systemWordList = toLowerCaseList(systemWords);
        Dictionary dictA = newSystemDictionary( 1, "dictAll");

        String userWords = "i, like, sam, sung, mobile, icecream, man go, mango";
        List<String> userWordList = toLowerCaseList(userWords);
        Dictionary dictU = newUserDictionary( 29, "dictUserWords", DEFUALT_PRIORITY);

        String inputText = "ilikesamsungmobile"; //should printed : samsung , sam sung


        WordBreakerStage3 wordBreaker = new WordBreakerStage3();
        wordBreaker.setBreakStrategy(BreakStrategy.SYSTEM_USER_DICT_BOTH);

        wordBreaker.addToTrie(systemWordList, dictA);
        wordBreaker.addToTrie(userWordList, dictU);

        List<String> outputSentences = wordBreaker.breakText(inputText);

        System.out.println(outputSentences); // real : [i like sam sung mobile, i like samsung mobile]

        assertThat(outputSentences.size(), is(2));
        assertThat(outputSentences.contains("i like sam sung mobile"), is(true));
        assertThat(outputSentences.contains("i like samsung mobile"), is(true));
    }

    @Test
    public void wordBreakTestCase2() {

        String systemWords = "i, like, sam, sung, samsung, mobile, ice, cream, and, man go";
        List<String> systemWordList = toLowerCaseList(systemWords);
        Dictionary dictA = newSystemDictionary( 1, "dictAll");

        String userWords = "i, like, sam, sung, mobile, icecream, man go, mango";
        List<String> userWordList = toLowerCaseList(userWords);
        Dictionary dictU = newUserDictionary( 29, "dictUserWords", DEFUALT_PRIORITY);

        String inputText = "ilikeicecreamandmango"; //should printe : ice, cream

        WordBreakerStage3 wordBreaker = new WordBreakerStage3();
        wordBreaker.setBreakStrategy(BreakStrategy.SYSTEM_USER_DICT_BOTH);

        wordBreaker.addToTrie(systemWordList, dictA);
        wordBreaker.addToTrie(userWordList, dictU);

        List<String> outputSentences = wordBreaker.breakText(inputText);

        System.out.println("outputSentences:\n" + outputSentences); // [i like ice cream and man go, i like icecream and man go]

        assertThat(outputSentences.size(), is(4));
        assertThat(outputSentences.contains("i like icecream and man go"), is(true));
        assertThat(outputSentences.contains("i like icecream and mango"), is(true));
        assertThat(outputSentences.contains("i like ice cream and man go"), is(true));
        assertThat(outputSentences.contains("i like ice cream and mango"), is(true));
    }

}