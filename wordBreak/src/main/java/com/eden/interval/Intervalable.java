package com.eden.interval;

public interface Intervalable extends Comparable
{
    public int getStart();

    public int getEnd();

    /** end-start+1 */
    public int size();
}
