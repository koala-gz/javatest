package com.eden.trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * ascii AC automation node
 */
public class AsciiState extends State {
    static final int SIZE = 256;
    /**
     * goto table
     */
    private State[] success = new State[SIZE];

    /**
     * change state
     *
     * @param character       input char
     * @param ignoreRootState if called by rootNode,set true. else set false
     * @return result state
     */
    private State nextState(Character character, boolean ignoreRootState) {
        State nextState = this.success[character & 0xff];
        if (!ignoreRootState && nextState == null && this.rootState != null) {
            nextState = this.rootState;
        }
        return nextState;
    }

    public AsciiState() {
    }

    public AsciiState(int depth) {
        super(depth);
    }

    public State nextState(Character character) {
        return nextState(character, false);
    }

    public State nextStateIgnoreRootState(Character character) {
        return nextState(character, true);
    }

    /**
     * add state to success table
     *
     * @param character
     * @return
     */
    public State addState(Character character) {
        State nextState = nextStateIgnoreRootState(character);
        if (nextState == null) {
            nextState = new AsciiState(this.depth + 1);
            this.success[character] = nextState;
        }
        return nextState;
    }

    /**
     * get success state
     *
     * @return
     */
    public Collection<State> getStates() {
        List<State> stateList = new ArrayList<State>(SIZE);
        for (State state : success) {
            if (state != null) stateList.add(state);
        }
        return stateList;
    }

    /**
     * get possible chars of next state
     *
     * @return
     */
    public Collection<Character> getTransitions() {
        List<Character> stateList = new ArrayList<Character>(SIZE);
        int i = 0;
        for (State state : success) {
            if (state != null) stateList.add((char) i);
            ++i;
        }
        return stateList;
    }

}
