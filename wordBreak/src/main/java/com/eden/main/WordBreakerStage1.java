package com.eden.main;

import com.eden.interval.Intervalable;
import com.eden.trie.Emit;
import com.eden.trie.EmitComparatorByDictPriorityAndIntervalPostion;
import com.eden.trie.Trie;
import com.eden.trie.TrieConfig;
import com.eden.trie.keyword.DictType;
import com.eden.trie.keyword.Dictionary;
import com.eden.trie.keyword.Keyword;
import com.eden.trie.keyword.NewWord;
import com.eden.util.CollectionUtils;
import com.eden.util.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType.APPEND;
import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType.IGNORE_NEW_ITEM;
import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType.REWRITE;
import static com.eden.trie.keyword.Dictionary.DEFUALT_PRIORITY;
import static com.eden.trie.keyword.Dictionary.DICT_BASE_ID;
import static com.eden.trie.keyword.Dictionary.createDictionary;
import static com.eden.util.StringUtils.toLowerCaseList;

/**
 * Break words with given dictionary
 *          { i, like, sam, sung, samsung, mobile, ice, cream, man go}
 * Input:   "ilikesamsungmobile"
 * Output:  i like sam sung mobile
 *          i like samsung mobile
 *
 * @author Shuxiong Zeng, 2020-2-22
 *
 * */
public class WordBreakerStage1 {
    protected Trie trie;
    protected TrieConfig trieConfig;

    private BreakStrategy breakStrategy;

    public WordBreakerStage1() {
        this.trieConfig = new TrieConfig();
        trieConfig.setAllowOverlaps(true);
        this.trie = new Trie(trieConfig);
    }

    /**
     * @param useAsciiMode : true : ascii(char), false : unicode(char).
     * */
    public WordBreakerStage1(boolean useAsciiMode) {
        this.trieConfig = new TrieConfig();
        trieConfig.setAllowOverlaps(true);
        this.trie = new Trie(trieConfig, useAsciiMode);
    }

    /**
     * setAllowOverlaps
     *
     * @param allowOverlaps
     */
    public void setAllowOverlaps(boolean allowOverlaps)
    {
        this.trieConfig.setAllowOverlaps(allowOverlaps);
    }

    public void addToTrie(List<String> wordList, Dictionary dictAll) {
        //words : set{dictAll} = A + B.
        //     A = set{dictPartANoSpace}
        //     B = set{dictPartBHasSpace}

        // A: words contains no space (normal words),such as : like, cream
        Dictionary dictPartANoSpace = Dictionary.createDictionary(dictAll.getId() + 100, dictAll.getDictType(),"dictA_no_space", dictAll);

        // B: words contains space (special words),such as : "man go", "united states"
        Dictionary dictPartBHasSpace = Dictionary.createDictionary(dictAll.getId() + 200, dictAll.getDictType(),"dictB_has_space", dictAll);

        // ProxyB: Proxy of B ,
        //         ProxyB : set{dictProxyBSpaceRemoved} = remove spaces from words in set B,
        // such as : "man go" --> "mango", "united states" --> "unitedstates"
        Dictionary dictProxyBSpaceRemoved = Dictionary.createDictionary(dictAll.getId() + 300, dictAll.getDictType(),"dictB_has_space", dictAll);

        Set<Keyword> setA = new HashSet<>();
        Set<Keyword> setB = new HashSet<>();
        splitWords(wordList,
                setA, dictPartANoSpace,
                setB, dictPartBHasSpace); // split from wordList into A and B
        Set<Keyword> setProxyB = removeSpaceAndBuildProxy(setB, dictProxyBSpaceRemoved);

        Set<Keyword> mergedKeywords = merge(setA, setProxyB);

        //log
        //System.out.println("merged keywords : ");
        //mergedKeywords.forEach(x -> System.out.println(x));

        // and pattens and build goto/success table
        trie.addKeywords(mergedKeywords);
    }

    private Set<Keyword> merge(Set<Keyword> set1, Set<Keyword> set2) {
        Set<Keyword> keywords = new HashSet<Keyword>();
        if(!CollectionUtils.isEmpty(set1)){
            keywords.addAll(set1);
        }
        if(!CollectionUtils.isEmpty(set2)){
            keywords.addAll(set2);
        }

        return keywords;
    }

    private Set<Keyword> removeSpaceAndBuildProxy(Set<Keyword> setB, Dictionary dictProxyB) {
        Set<Keyword> keywords = new HashSet<Keyword>((int)(setB.size() / 0.75) + 1);

        for(Keyword principal : setB){
            String valueNoSpace = principal.getValue().replaceAll("\\s+", "");
            keywords.add(new Keyword(valueNoSpace, dictProxyB, true, principal));
        }

        return keywords;
    }

    /**
     * Split from wordList into A and B
     * @param wordList : input
     * @param setA : output (words with no space)
     * @param dictA : input dict
     *
     * @param setB : output (words with space)
     * @param dictB : input dict
     *
     * */
    private void splitWords(final List<String> wordList,
                            Set<Keyword> setA, final Dictionary dictA,
                            Set<Keyword> setB, final Dictionary dictB) {
        for(String word : wordList){
            String newWord = StringUtils.trim(word);
            if(newWord.length() <= 0){
                continue;
            }

            if(!newWord.contains(" ")){
                // no spaces
                setA.add(new Keyword(newWord, dictA));
            }else {
                setB.add(new Keyword(newWord, dictB));
            }
        }
    }

    public List<String> breakText(String inputText) {
        List<String> list = new ArrayList<>();

        trie.buildFailuerStates();
        //Set<Keyword> keywords = (Set<Keyword>)context.get(MERGED_KEYWORDS);
        Collection<Emit> emits = trie.parseText(inputText);

        //System.out.println("breakText result emits: ");
        //emits.stream().forEach(x -> System.out.println(x));

        emits = doFilter(emits);

        List<NewWord> newWords = new ArrayList<>();
        findAllPaths(inputText, emits, list, newWords);
        list = doLastFilterForOutput(list);
        return list;
    }

    protected List<String> doLastFilterForOutput(List<String> list) {
        Set<String> set = new HashSet<>();
        set.addAll(list);
        return set.stream().collect(Collectors.toList());
    }

    protected Collection<Emit> doFilter(Collection<Emit> emits) {
        return emits;
    }

    /**
     * @param emits : input
     * @param paths : output : path[i] = interval[0] + interval[1] + ... + interval[n-1].
     *              and interval[k] = [emits[k].getStart(), emits[k].getEnd()].
     * @param newWords : output : the words which are not in dictionary.
     * */
    protected void findAllPaths(final String theWholeInputText, final Collection<Emit> emits, List<String> paths, List<NewWord> newWords) {
        if (CollectionUtils.isEmpty(emits)){
            if(!StringUtils.isEmpty(theWholeInputText)){
                // treat it as the only one new word and print
                newWords.add(createNewWord(theWholeInputText, 0, theWholeInputText.length()-1));
                paths.add(theWholeInputText);
                return ;
            }
            return ;
        }

        //SortedSet<Emit> sortedEmits = sortSet(emits, new EmitComparatorByDictPriorityAndIntervalPostion());
        List<Emit> sortedEmits = sortList(emits, new EmitComparatorByDictPriorityAndIntervalPostion());

        //Set<NewWord> allNewWords = new HashSet<>();
        //TODO new words
        //Set<NewWord> newWordsAtFront = findNewWordsAtFront(theWholeInputText, sortedEmits);
        //allNewWords.addAll(newWordsAtFront);

        //Set<Emit> mergedEmits = mergeCollections(sortedEmits, newWordsAtFront);
        //sortedEmits = sortSet(mergedEmits, new EmitComparatorByDictPriorityAndIntervalPostion());

        // result
        Deque<String> pathStack = new ArrayDeque<String>();

        // min & max could change later
        int minpos = getFirstItem(sortedEmits).getStart();
        int maxEndPos = findMaxEndPosition(sortedEmits);
        //Map<Emit, Integer> indexMap = createIndexMapping(sortedEmits);

        Deque<Node> stack = new ArrayDeque<Node>();
        for(Emit e : sortedEmits){
            // find 1st interval
            int startPos = e.getStart();
            if(startPos > minpos){
                break;
            }

            // push 1st interval
            List<Emit> outputEmits = new ArrayList<>();
            outputEmits.add(e);
            stack.push(new Node(e, outputEmits));

            while(!stack.isEmpty()){
                Node node = stack.pop();
                Emit currentInterval = node.interval; // could be NewWord

                //debug
                //System.out.println("debug: " + getPathForOutput(node.outputEmits));

                if(currentInterval.getEnd() == maxEndPos){
                    //found path
                    pathStack.push(getPathForOutput(node.outputEmits));
                    //paths.add(getPathForOutput(node.outputEmits));
                    continue;
                }

                //int nextIndex = indexMap.get(currentInterval) + 1;
                //for(int j = nextIndex; j < sortedEmits.size(); ++j){
                Iterator<Emit> iterator = sortedEmits.iterator();
                boolean isFoundCurrentEmit = false;
                while(iterator.hasNext()){
                    Emit e2 = iterator.next();
                    // skip the ones before currentInterval
                    if(!isFoundCurrentEmit){
                        if(e2 == currentInterval){
                            isFoundCurrentEmit = true;
                        }
                        continue;
                    }

                    //Emit e2 = sortedEmits.get(j);
                    if(e2.getStart() == currentInterval.getEnd() + 1){ // consecutive interval

                        // copy parent emits
                        List<Emit> branchEmits = new ArrayList<>();
                        branchEmits.addAll(node.outputEmits);
                        branchEmits.add(e2);

                        stack.push(new Node(e2, branchEmits));
                    }
                    else if(e2.getStart() > currentInterval.getEnd() + 1){
                        break;
// TODO:
//                        // found new word
//                        // copy parent emits
//                        List<Emit> branchEmits = new ArrayList<>();
//                        branchEmits.addAll(node.outputEmits);
//
//                        int startNewWord = currentInterval.getEnd() + 1;
//                        int endNewWord = e2.getStart() -1;
//                        NewWord newWord = createNewWord(theWholeInputText.substring(startNewWord, endNewWord + 1),
//                                                    startNewWord, endNewWord);
//                        branchEmits.add(newWord);
//
//                        //sortedEmits.add(newWord); // include all words
//                        allNewWords.add(newWord);
//                        minpos = startNewWord < minpos ? startNewWord : minpos;
//                        maxEndPos = endNewWord > maxEndPos ? endNewWord : maxEndPos;
//
//                        stack.push(new Node(newWord, branchEmits));
//                        break;
                    }

                } //end for
            } //end while

        }// end for sorted emits

        // change the list order:
        while(!pathStack.isEmpty()){
            String path = pathStack.pop();
            if(!StringUtils.isEmpty(path)){
                paths.add(path);
            }
        }

        //newWords.addAll(allNewWords);
        return;
    } //end func

    private Emit getFirstItem(Collection<Emit> sortedEmits) {
        if(sortedEmits instanceof SortedSet){
            return ((SortedSet<Emit>) sortedEmits).first();
        }
        else if(sortedEmits instanceof List){
            return ((List<Emit>) sortedEmits).get(0);
        }

        throw new IllegalArgumentException("Unsupport class type.");
    }

    private static <T> Set<T> mergeCollections(Collection<? extends T> set1, Collection<? extends T> set2) {
        Set<T> merged = new HashSet<>();
        if(!CollectionUtils.isEmpty(set1)) {
            merged.addAll(set1);
        }
        if(!CollectionUtils.isEmpty(set2)) {
            merged.addAll(set2);
        }
        return merged;
    }

    private Set<NewWord> findNewWordsAtFront(String theWholeInputText, SortedSet<Emit> sortedEmits) {

        //if(sortedEmits.get(0).getStart() == 0){
        //    return Collections.EMPTY_SET;
        //}
        final Emit firstEmit =  sortedEmits.first();
        if(firstEmit.getStart() == 0){
            return Collections.EMPTY_SET;
        }

        Set<Emit> targets = new HashSet<>();
        targets.add(firstEmit);
        int maxEndPosInTargets = firstEmit.getEnd();
        int minStartPosInTargets = 0;

        //int i = 0;
        for(Emit e : sortedEmits){
            if(firstEmit == e){ // already has been added
                continue;
            }

            int start = e.getStart();
            int end = e.getEnd();
            if(start <= maxEndPosInTargets && end > minStartPosInTargets){
                targets.add(e);
                if(start < minStartPosInTargets) minStartPosInTargets = start;
                if(end > maxEndPosInTargets) maxEndPosInTargets = end;
            }
            //++i;
        }

        Set<NewWord> newWords = new HashSet<>();
        for(Emit e : targets){
            int newWordEnd =  e.getStart()-1;
            newWords.add(
                    new NewWord(0, newWordEnd,
                                new Keyword(theWholeInputText.substring(0, newWordEnd + 1), null)));
        }

        return newWords;
    }


    //private Map<Emit, Integer> createIndexMapping(List<Emit> sortedEmits) {
    //    Map<Emit, Integer> indexMap = new TreeMap<>();
    //    for(int i = 0 ;i< sortedEmits.size(); ++i){
    //        indexMap.put(sortedEmits.get(i), i);
    //    }
    //    return indexMap;
    //}

    protected List<Emit> sortList(Collection<Emit> emits, Comparator<Emit> comparator) {
        List<Emit> sortedEmits = emits.stream().collect(Collectors.toList());
        sortedEmits.sort(comparator);
        emits.forEach(x -> sortedEmits.add(x));
        return sortedEmits;
    }

    protected SortedSet<Emit> sortSet(Collection<Emit> emits, Comparator<Emit> comparator) {
        SortedSet<Emit> sortedEmits = new TreeSet(comparator);
        emits.forEach(x -> sortedEmits.add(x));
        return sortedEmits;
    }

    private int findMaxEndPosition(Collection<Emit> sortedEmits) {
        if(CollectionUtils.isEmpty(sortedEmits)){
            throw new IllegalArgumentException("Input collection shouldn't be empty.");
        }

        int maxEndPos = getLastItem(sortedEmits).getEnd();
        // for(int i = sortedEmits.size()-1; i >= 0; --i){
        //     Emit e = sortedEmits.get(i);
        //     int endPos = e.getEnd();
        //     if(endPos > maxEndPos) {maxEndPos = endPos;}
        // }
        for(Emit e : sortedEmits){
            int endPos = e.getEnd();
            if(endPos > maxEndPos) {maxEndPos = endPos;}
        }
        return maxEndPos;
    }

    private Intervalable getLastItem(Collection<Emit> sortedEmits) {
        if(sortedEmits instanceof SortedSet){
            return ((SortedSet<Emit>) sortedEmits).last();
        }
        else if(sortedEmits instanceof List){
            return ((List<Emit>) sortedEmits).get(sortedEmits.size() - 1);
        }

        throw new IllegalArgumentException("Unsupport class type.");
    }

    private NewWord createNewWord(String value, int start, int end) {
        return new NewWord(start, end,
                new Keyword(value, null));
    }

    static class Node{
        Emit interval;
        List<Emit> outputEmits;
        public Node(Emit interval, List<Emit> outputEmits) {
            this.interval = interval;
            this.outputEmits = outputEmits;
        }
    }

    protected String getPathForOutput(List<Emit> outputEmits) {
        //System.out.println("find path:");

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Emit emit : outputEmits){
            Keyword keyword = emit.getKeyword();

            //System.out.println(emit);
            sb.append(keyword.isProxy()?
                        keyword.getPrincipal().getValue()
                        : keyword.getValue());
            if(++count != outputEmits.size()){ // not last char
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    private String getPrincipalKeywordValue(Keyword keyword) {
        if (!keyword.isProxy()){
            return keyword.getValue();
        }
        return null;
    }

    public void setBreakStrategy(BreakStrategy breakStrategy) {
        this.breakStrategy = breakStrategy;

        if(this.breakStrategy == BreakStrategy.USER_DICT_ONLY) {
            trieConfig.setDupKeywordInsType(REWRITE);
        }
        else if(this.breakStrategy == BreakStrategy.SYSTEM_USER_DICT_BOTH) {
            trieConfig.setDupKeywordInsType(APPEND);
        }
        else {
            trieConfig.setDupKeywordInsType(IGNORE_NEW_ITEM);
        }
    }

    /** for debug only */
    public static void main(String[] args) {
        String words = "i, like, sam, sung, samsung, mobile, ice, cream, man go";
        String inputText = "ilikesamsungmobile";

        List<String> wordList = toLowerCaseList(words);
        Dictionary dictA = newDictionary( 1, "dictAll");

        WordBreakerStage1 wordBreaker = new WordBreakerStage1();
        wordBreaker.addToTrie(wordList, dictA);
        List<String> outputSentences = wordBreaker.breakText(inputText);

        for (int i = 0 ; i < outputSentences.size(); ++i){
            System.out.println(outputSentences.get(i));
        }
    }

    /** for debug only */
    private static Dictionary newDictionary(int idx, String dictName){
        return createDictionary(DICT_BASE_ID + idx,
                DictType.SYSTEM,
                dictName,
                DEFUALT_PRIORITY);
    }
}










