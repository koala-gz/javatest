package com.eden.trie;

import com.eden.interval.IntervalTree;
import com.eden.interval.Intervalable;
import com.eden.trie.keyword.Dictionary;
import com.eden.trie.keyword.Keyword;
import com.eden.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Trie Tree.
 * Based on Aho-Corasick Automation
 */
public class Trie {

    private TrieConfig trieConfig;

    private State rootState;

    /**
     * if the failure table built.
     */
    private boolean failureStatesConstructed = false;

    /**
     * @param trieConfig : input config
     */
    public Trie(TrieConfig trieConfig) {
        this(trieConfig, true);
    }

    public Trie(TrieConfig trieConfig, boolean ascii) {
        this.trieConfig = trieConfig;
        if (ascii) {
            this.rootState = new AsciiState();
        } else {
            this.rootState = new UnicodeState();
        }
    }

    public Trie() {
        this(new TrieConfig());
    }

    public Trie(boolean ascii) {
        this(new TrieConfig(), ascii);
    }

    public Trie caseInsensitive() {
        this.trieConfig.setCaseInsensitive(true);
        return this;
    }

    /**
     * not allow overlaps on the same position
     *
     * @return
     */
    public Trie removeOverlaps() {
        this.trieConfig.setAllowOverlaps(false);
        return this;
    }

    public Trie onlyWholeWords() {
        this.trieConfig.setOnlyWholeWords(true);
        return this;
    }

    public void addKeyword(String value) {
        addKeyword(new Keyword(value, Dictionary.SYSTEM_DEFAULT_DICT));
    }

    public void addKeyword(String value, Dictionary dict) {
        addKeyword(new Keyword(value, dict));
    }

    public void addKeyword(Collection<String> values, Dictionary dict) {
        if (CollectionUtils.isEmpty(values)) {
            return;
        }
        values.forEach(x -> addKeyword(new Keyword(x, dict)));
    }

    public void addKeywords(Collection<Keyword> keywords) {
        if (CollectionUtils.isEmpty(keywords)) {
            return;
        }
        keywords.forEach(x -> addKeyword(x));
    }

    /**
     * Add a patten
     *
     * @param keyword : input patten string value
     */
    public void addKeyword(Keyword keyword) {
        String keywordValue = keyword.getValue();

        if (keywordValue == null || keywordValue.length() == 0) {
            return;
        }
        State currentState = this.rootState;
        for (Character character : keywordValue.toCharArray()) {
            currentState = currentState.addState(character);
        }
        currentState.addEmit(keyword, trieConfig.getDupKeywordInsType());
    }

    /**
     * tokenize
     *
     * @param text : input
     * @return
     */
    public Collection<Token> tokenize(String text) {

        Collection<Token> tokens = new ArrayList<Token>();

        Collection<Emit> collectedEmits = parseText(text);
        int lastCollectedPosition = -1;
        for (Emit emit : collectedEmits) {
            if (emit.getStart() - lastCollectedPosition > 1) {
                tokens.add(createFragment(emit, text, lastCollectedPosition));
            }
            tokens.add(createMatch(emit, text));
            lastCollectedPosition = emit.getEnd();
        }
        if (text.length() - lastCollectedPosition > 1) {
            tokens.add(createFragment(null, text, lastCollectedPosition));
        }

        return tokens;
    }

    private Token createFragment(Emit emit, String text, int lastCollectedPosition) {
        return new FragmentToken(text.substring(lastCollectedPosition + 1, emit == null ? text.length() : emit.getStart()));
    }

    private Token createMatch(Emit emit, String text) {
        return new MatchToken(text.substring(emit.getStart(), emit.getEnd() + 1), emit);
    }

    /**
     * To match the input text.
     *
     * @param text input
     * @return result output emits
     */
    @SuppressWarnings("unchecked")
    public Collection<Emit> parseText(String text) {
        checkForConstructedFailureStates();

        int position = 0;
        State currentState = this.rootState;
        List<Emit> collectedEmits = new ArrayList<Emit>();
        for (Character character : text.toCharArray()) {
            if (trieConfig.isCaseInsensitive()) {
                character = Character.toLowerCase(character);
            }
            currentState = getState(currentState, character);
            storeEmits(position, currentState, collectedEmits);
            ++position;
        }

        if (trieConfig.isOnlyWholeWords()) {
            removePartialMatches(text, collectedEmits);
        }

        if (!trieConfig.isAllowOverlaps()) {
            IntervalTree intervalTree = new IntervalTree((List<Intervalable>) (List<?>) collectedEmits);
            intervalTree.removeOverlaps((List<Intervalable>) (List<?>) collectedEmits);
        }

        return collectedEmits;
    }

    /**
     * remove partial words
     *
     * @param searchText
     * @param collectedEmits
     */
    private void removePartialMatches(String searchText, List<Emit> collectedEmits) {
        long size = searchText.length();
        List<Emit> removeEmits = new ArrayList<Emit>();
        for (Emit emit : collectedEmits) {
            if ((emit.getStart() == 0 ||
                    !Character.isAlphabetic(searchText.charAt(emit.getStart() - 1))) &&
                    (emit.getEnd() + 1 == size ||
                            !Character.isAlphabetic(searchText.charAt(emit.getEnd() + 1)))) {
                continue;
            }
            removeEmits.add(emit);
        }

        for (Emit removeEmit : removeEmits) {
            collectedEmits.remove(removeEmit);
        }
    }

    /**
     * goto next state
     *
     * @param currentState
     * @param character    input char
     * @return state to goto
     */
    private static State getState(State currentState, Character character) {
        State newCurrentState = currentState.nextState(character);  // goto table
        while (newCurrentState == null) // failure table
        {
            currentState = currentState.failure();
            newCurrentState = currentState.nextState(character);
        }
        return newCurrentState;
    }

    /**
     * build failure table
     */
    public void buildFailuerStates() {
        if (!this.failureStatesConstructed) {
            constructFailureStates();
        }
    }

    private void checkForConstructedFailureStates() {
        if (!this.failureStatesConstructed) {
            constructFailureStates();
        }
    }

    private void constructFailureStates() {
        Queue<State> queue = new LinkedBlockingDeque<State>();

        // set fail state with rootnode for the nodes at 1th depth.
        for (State depthOneState : this.rootState.getStates()) {
            depthOneState.setFailure(this.rootState);
            queue.add(depthOneState);
        }
        this.failureStatesConstructed = true;

        // BFS , create failure table for nodes at Depth > 1.
        while (!queue.isEmpty()) {
            State currentState = queue.remove();

            for (Character transition : currentState.getTransitions()) {
                State targetState = currentState.nextState(transition);
                queue.add(targetState);

                State traceFailureState = currentState.failure();
                while (traceFailureState.nextState(transition) == null) {
                    traceFailureState = traceFailureState.failure();
                }
                State newFailureState = traceFailureState.nextState(transition);
                targetState.setFailure(newFailureState);
                targetState.addEmit(newFailureState.emit());
            }
        }
    }

    /**
     * store the results.
     *
     * @param position       current pos，keyword.end + 1
     * @param currentState
     * @param collectedEmits output results
     */
    private static void storeEmits(int position, State currentState, List<Emit> collectedEmits) {
        Collection<Keyword> emits = currentState.emit();
        if (emits != null && !emits.isEmpty()) {
            for (Keyword emit : emits) {
                collectedEmits.add(new Emit(position - emit.getValue().length() + 1, position, emit));
            }
        }
    }

}
