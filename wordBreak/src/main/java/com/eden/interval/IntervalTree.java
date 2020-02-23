package com.eden.interval;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * store intervals into a tree for searching.
 */
public class IntervalTree {

    private IntervalNode rootNode = null;
    public IntervalTree(List<Intervalable> intervals) {
        this.rootNode = new IntervalNode(intervals);
    }

    public List<Intervalable> removeOverlaps(List<Intervalable> intervals) {

        Collections.sort(intervals, new IntervalableComparatorBySize());
        Set<Intervalable> removeIntervals = new TreeSet<Intervalable>();
        for (Intervalable interval : intervals) {
            if (removeIntervals.contains(interval)) {
                continue;
            }
            // will remove
            removeIntervals.addAll(findOverlaps(interval));
        }

        // remove
        for (Intervalable removeInterval : removeIntervals) {
            intervals.remove(removeInterval);
        }

        Collections.sort(intervals, new IntervalableComparatorByPosition());

        return intervals;
    }

    public List<Intervalable> findOverlaps(Intervalable interval) {
        return rootNode.findOverlaps(interval);
    }

}
