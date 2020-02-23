package com.eden.main;

public class BreakStrategy {
    public static final BreakStrategy USER_DICT_ONLY = new BreakStrategy(1);
    public static final BreakStrategy SYSTEM_USER_DICT_BOTH = new BreakStrategy(2);

    private int type = 0;
    private BreakStrategy(int type){
        this.type = type;
    }
}
