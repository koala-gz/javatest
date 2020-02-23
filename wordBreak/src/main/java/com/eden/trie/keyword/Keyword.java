package com.eden.trie.keyword;

import com.eden.util.ObjectUtils;

import java.lang.Comparable;

public class Keyword implements Comparable{
    /** Keyword value */
    private String value;

    /** Which the keyword belong to .
     *  For new word the dict is null.
     * */
    private Dictionary dict;

    /** The keyword(value, dict) if is a proxy one */
    private boolean isProxy = false;

    /**
     *  The principal keyword.
     *  It should not be null when isProxy is true,
     *    and would be ignored when isProxy is false
     *  */
    private Keyword principal = null;


    public Keyword(String value, Dictionary dict) {
        this.value = value;
        this.dict = dict;
    }

    public Keyword(String value, Dictionary dict, boolean isProxy, Keyword principal) {
        this.value = value;
        this.dict = dict;
        this.isProxy = isProxy;
        this.principal = principal;

        if (isProxy){
            if (this.principal == null){
                throw new IllegalArgumentException("If the parameter of isProxy is true, the principal should not be null.");
            }
            if(this.principal == this){
                throw new IllegalArgumentException("If the parameter of isProxy is true, the principal should be assigned with another keyword.");
            }
            if(ObjectUtils.equals(this.principal.getDict(), this.dict)){
                throw new IllegalArgumentException("If the parameter of isProxy is true, the principal's dictionary should be assigned with another one.");
            }
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Dictionary getDict() {
        return dict;
    }

    public void setDict(Dictionary dict) {
        this.dict = dict;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public void setProxy(boolean proxy) {
        isProxy = proxy;
    }

    public Keyword getPrincipal() {
        return principal;
    }

    public void setPrincipal(Keyword principal) {
        this.principal = principal;
    }

    @Override
    public String toString() {
        return "{" +
                "value='" + value + '\'' +
                ", dict=" + dict +
                ", isProxy=" + isProxy +
                ", principal=" + principal +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if (o == null || !(o instanceof Keyword))
        {
            return false;
        }

        Keyword other = (Keyword) o;

        if(this.value == null){
             if(other.value != null){
                return false;
             }
             return ObjectUtils.equals(this.dict, other.dict);
        }
        // not null
        if(!this.value.equals(other.value)){
            return false;
        }
        return ObjectUtils.equals(this.dict, other.dict);
    }

    @Override
    public int hashCode()
    {
        int code = this.value == null ? 0 : this.value.hashCode();
        return code * 31 + (this.dict == null ?  0 : this.dict.hashCode());
    }

    /**
     * Just simply compare the string value.
     * */
    @Override
    public int compareTo(Object o)
    {
        if (o == null || !(o instanceof Keyword)){
            return 1;
        }

        Keyword other = (Keyword) o;
        if(this.value == null) {
            return other.value == null ? 0 : -1;
        }

        return this.value.compareTo(other.value);
    }

}
