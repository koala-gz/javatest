package com.eden.main;

import com.eden.trie.keyword.DictType;
import com.eden.trie.keyword.Dictionary;
import org.junit.Test;

import java.util.List;

import static com.eden.main.TestUtils.newSystemDictionary;
import static com.eden.trie.keyword.Dictionary.DEFUALT_PRIORITY;
import static com.eden.trie.keyword.Dictionary.DICT_BASE_ID;
import static com.eden.trie.keyword.Dictionary.createDictionary;

import static com.eden.util.StringUtils.toLowerCaseList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class WordBreakerStage1Test {

    @Test
    public void wordBreakTestCase1() {

        String words = "i, like, sam, sung, samsung, mobile, ice, cream, man go";
        String inputText = "ilikesamsungmobile";

        List<String> wordList = toLowerCaseList(words);
        Dictionary dictA = newSystemDictionary( 1, "dictAll");

        WordBreakerStage1 wordBreaker = new WordBreakerStage1();
        wordBreaker.addToTrie(wordList, dictA);
        List<String> outputSentences = wordBreaker.breakText(inputText);

        assertThat(outputSentences.size(), is(2));
        assertThat(outputSentences.contains("i like sam sung mobile"), is(true));
        assertThat(outputSentences.contains("i like samsung mobile"), is(true));
    }

    @Test
    public void wordBreakTestCase2() {

        String words = "i, like, sam, sung, samsung, mobile, ice, cream, and, man go";
        String inputText = "ilikeicecreamandmango";

        List<String> wordList = toLowerCaseList(words);
        Dictionary dictA = newSystemDictionary( 1, "dictAll");

        WordBreakerStage1 wordBreaker = new WordBreakerStage1();
        wordBreaker.addToTrie(wordList, dictA);
        List<String> outputSentences = wordBreaker.breakText(inputText);

        assertThat(outputSentences.size(), is(1));
        assertThat(outputSentences.contains("i like ice cream and man go"), is(true));
    }

    @Test
    public void wordBreakTestCase3() {

        String words = "i, like, sam, sung, samsung,手机";
        String inputText = "ilikesamsung手机";

        List<String> wordList = toLowerCaseList(words);
        Dictionary dictA = newSystemDictionary( 1, "dictAll");

        WordBreakerStage1 wordBreaker = new WordBreakerStage1(false);
        wordBreaker.addToTrie(wordList, dictA);
        List<String> outputSentences = wordBreaker.breakText(inputText);

        assertThat(outputSentences.size(), is(2));
        assertThat(outputSentences.contains("i like sam sung 手机"), is(true));
        assertThat(outputSentences.contains("i like samsung 手机"), is(true));
    }
}