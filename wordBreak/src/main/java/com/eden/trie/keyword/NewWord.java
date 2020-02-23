package com.eden.trie.keyword;

import com.eden.trie.Emit;
import com.eden.util.ObjectUtils;

public class NewWord extends Emit {

    //private boolean isNewWord = true;

    public NewWord(int start, int end, final Keyword keyword) {
        super(start, end, keyword);
    }
    public NewWord(final Emit emit) {
        super(emit.getStart(), emit.getEnd(), emit.getKeyword());
    }


    public String getWordValue() {
        return super.getKeywordValue();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof NewWord)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NewWord{" + super.toString() + "}";
    }
}
