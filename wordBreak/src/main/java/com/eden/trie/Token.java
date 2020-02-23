package com.eden.trie;

/**
 * token or fragment
 */
public abstract class Token {
    private String fragment;

    public Token(String fragment) {
        this.fragment = fragment;
    }

    public String getFragment() {
        return this.fragment;
    }

    public abstract boolean isMatch();

    public abstract Emit getEmit();

}
