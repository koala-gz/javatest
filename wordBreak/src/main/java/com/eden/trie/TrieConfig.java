package com.eden.trie;

public class TrieConfig {

    private boolean allowOverlaps = true;

    /**
     * match whole words
     */
    private boolean onlyWholeWords = false;

    /**
     * case
     */
    private boolean caseInsensitive = false;

    private DuplicatedKeywordInsertType dupKeywordInsType;

    public static enum DuplicatedKeywordInsertType {
        IGNORE_NEW_ITEM,
        REWRITE,
        APPEND,
    }

    public DuplicatedKeywordInsertType getDupKeywordInsType() {
        return dupKeywordInsType;
    }

    public void setDupKeywordInsType(DuplicatedKeywordInsertType dupKeywordInsType) {
        this.dupKeywordInsType = dupKeywordInsType;
    }

    public boolean isAllowOverlaps() {
        return allowOverlaps;
    }

    public void setAllowOverlaps(boolean allowOverlaps) {
        this.allowOverlaps = allowOverlaps;
    }

    public boolean isOnlyWholeWords() {
        return onlyWholeWords;
    }

    public void setOnlyWholeWords(boolean onlyWholeWords) {
        this.onlyWholeWords = onlyWholeWords;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }
}
