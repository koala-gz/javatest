package com.eden.trie;

import com.eden.trie.keyword.Keyword;
import com.eden.util.StringUtils;

import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType;
import static com.eden.trie.TrieConfig.DuplicatedKeywordInsertType.APPEND;
import static com.eden.trie.keyword.KeywordUtils.belongToUserDict;

import java.util.*;

/**
 * states :
 * success
 * failure
 * emits :  output
 * RootNode hasn't failure state/tables
 */
public abstract class State {

    /**
     * BFS depth of the tree
     */
    protected final int depth;

    /**
     * used for root node only（Does match any state）
     */
    protected final State rootState;

    /**
     * fail tables/func
     */
    private State failure = null;

    /**
     * output if success.
     */
    //private Set<Keyword> emits = null;
    private Collection<Keyword> emits = null;

    /**
     * create node with depth 0.
     */
    public State() {
        this(0);
    }

    public State(int depth) {
        this.depth = depth;
        this.rootState = depth == 0 ? this : null;
    }

    public int getDepth() {
        return this.depth;
    }

    public void addEmit(Keyword keyword) {
        addEmit(keyword, DuplicatedKeywordInsertType.IGNORE_NEW_ITEM);
    }

    /**
     * add output string
     *
     * @param keyword
     */
    public void addEmit(Keyword keyword, DuplicatedKeywordInsertType addType) {
        if (this.emits == null) {
            //this.emits = new TreeSet<Keyword>();
            this.emits = new LinkedList<>();
        }

        Keyword old = null;
        for (Keyword kw : emits) {
            if (kw != null && StringUtils.equals(kw.getValue(), keyword.getValue())) {
                old = kw;
            }
        }
        if (old == null) {
            this.emits.add(keyword);
        } else if (addType == DuplicatedKeywordInsertType.REWRITE) {
            if (!belongToUserDict(old)) {
                this.emits.remove(old);
            }
            this.emits.add(keyword);
        } else if (addType == DuplicatedKeywordInsertType.APPEND) {
            this.emits.add(keyword);
        } else {
            this.emits.remove(old);
            this.emits.add(keyword);
        }
    }

    /**
     * 添加一些匹配到的模式串
     *
     * @param emits
     */
    public void addEmit(Collection<Keyword> emits) {
        for (Keyword emit : emits) {
            addEmit(emit);
        }
    }

    /**
     * get all the emits of the state
     *
     * @return
     */
    public Collection<Keyword> emit() {
        return this.emits == null ? Collections.<Keyword>emptyList() : this.emits;
    }

    /**
     * get failure state
     *
     * @return
     */
    public State failure() {
        return this.failure;
    }


    public void setFailure(State failState) {
        this.failure = failState;
    }

    /**
     * success state
     *
     * @param character input char
     * @return changed result
     */
    public abstract State nextState(Character character);

    /**
     * success state, ignore rootnode
     *
     * @param character
     * @return
     */
    public abstract State nextStateIgnoreRootState(Character character);

    /**
     * add state to success func
     *
     * @param character
     */
    public abstract State addState(Character character);

    /**
     * success state
     */
    public abstract Collection<State> getStates();

    /**
     * get possible chars of next state
     */
    public abstract Collection<Character> getTransitions();

}
