package com.eden.trie;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Support Unicode Char
 */
public class UnicodeState extends State {
    /**
     * goto table
     */
    private Map<Character, State> success = new TreeMap<Character, State>();

    public UnicodeState() {
        super();
    }

    public UnicodeState(int depth) {
        super(depth);
    }

    /**
     * change state
     *
     * @param character       input char
     * @param ignoreRootState if called by rootNode,set true. else set false
     * @return result state
     */
    private State nextState(Character character, boolean ignoreRootState) {
        State nextState = this.success.get(character);
        if (!ignoreRootState && nextState == null && this.rootState != null) {
            nextState = this.rootState;
        }
        return nextState;
    }

    @Override
    public State nextState(Character character) {
        return nextState(character, false);
    }

    @Override
    public State nextStateIgnoreRootState(Character character) {
        return nextState(character, true);
    }

    @Override
    public State addState(Character character) {
        State nextState = nextStateIgnoreRootState(character);
        if (nextState == null) {
            nextState = new UnicodeState(this.depth + 1);
            this.success.put(character, nextState);
        }
        return nextState;
    }

    @Override
    public Collection<State> getStates() {
        return this.success.values();
    }

    @Override
    public Collection<Character> getTransitions() {
        return this.success.keySet();
    }
}
