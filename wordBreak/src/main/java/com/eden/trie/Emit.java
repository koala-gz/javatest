package com.eden.trie;

import com.eden.interval.Interval;
import com.eden.interval.Intervalable;
import com.eden.trie.keyword.Keyword;
import com.eden.util.ObjectUtils;

/**
 * A matched result
 */
public class Emit extends Interval implements Intervalable {
    /**
     * pattern string / word
     */
    private final Keyword keyword;

    /**
     * @param start   start position ( for interval)
     * @param end     end position
     * @param keyword pattern
     */
    public Emit(final int start, final int end, final Keyword keyword) {
        super(start, end);
        this.keyword = keyword;
    }

    public Keyword getKeyword() {
        return this.keyword;
    }

    public String getKeywordValue() {
        return this.keyword == null ? null : this.keyword.getValue();
    }

    @Override
    public int hashCode() {
        int code = super.hashCode();
        return code * 31 + (this.getKeyword() == null ? 0 : this.getKeyword().hashCode());
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Emit)) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        if (!ObjectUtils.equals(this.getKeyword(), ((Emit) o).getKeyword())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Emit{" + super.toString() + ", " + this.keyword + "}";
    }
}
