package com.eden;

import com.eden.data.DefaultNumberLetterMappings;
import com.eden.data.NumberLetterMappings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.eden.utils.MyUtil.isEmpty;

/**
 * Recursive Algorithm
 * Convert the digits from 0 to 9 into letters
 * Input: arr[] = {2, 3}
 * Output: ad ae af bd be bf cd ce cf
 *
 * @author Shuxiong Zeng, 2020-1-13
 * */
public class RecursiveNumberLetterConverter implements NumberLetterConverter {
    //private static final Log LOG = LogFactory.getLog(RecursiveNumberLetterConverter.class);

    private static NumberLetterMappings mappings = new DefaultNumberLetterMappings();

    public static void main(String[] args) {
        int[] arr = {2, 3, 4};
        NumberLetterConverter c = new RecursiveNumberLetterConverter();
        Collection<String> rs = c.convert(arr);
        System.out.println(rs.size());
        StringBuilder sb = new StringBuilder();
        for(String val : rs){
            sb.append(val).append(" ");
        }
        System.out.println(sb.toString());
    }
    /**
     * Convert and combinate
     *  Input: arr[] = {2, 3}
     *  Output: ad ae af bd be bf cd ce cf
     *
     * */
    @Override
    public List<String> convert(int[] arr) {
        if (isEmpty(arr)){
            return Collections.emptyList();
        }

        // Map array{2, 3} to List{abc, def},
        // Or map array{2, 3, 9} to List{abc, def, wxyz}, ...
        List<char[]> lettersList = getLettersList(arr);

        // combinate List{abc, def} to Result{ad ae af bd be bf cd ce cf}
        // Result.size = list[0].length * list[1].length * ... (factor := 1 for length = 0)
        final List<String> combinations = new LinkedList<String>();
        combination(lettersList, 0, "", combinations);

        return combinations;
    }

    /**
     * combinate List{abc, def} to Result{ad ae af bd be bf cd ce cf}
     * @param list  input immutable List{abc, def} ==> char[0] ={'a','b','c'}, char[1]={'d','e','f'}}.
     * @param indexOfSetToPrint input : The letters of the digit to be processed
     */
    public static void combination(final List<char[]> list, int indexOfSetToPrint, String leftTotalContent, List<String> results){
        //print current set(letters)
        char[] letters = list.get(indexOfSetToPrint);
        if (isEmpty(letters)){
            String combContext = leftTotalContent;
            if (indexOfSetToPrint == list.size() -1 ){
                // the last set, print result
                // System.out.println(combContext + " ");
                results.add(combContext);
            } else {
                combination(list, indexOfSetToPrint + 1, combContext, results);
            }
            return;
        }
        //not empty
        for (char ch : letters){
            String combContext = leftTotalContent + ch;
            if (indexOfSetToPrint == list.size() -1 ){
                // the last set, print result
                // System.out.println(combContext + " ");
                results.add(combContext);
            } else {
                combination(list, indexOfSetToPrint + 1, combContext, results);
            }
        }
    }

    /**
     * Map array{2, 3} to List{ABC, DEF}
     * */
    private List<char[]> getLettersList(int[] arr) {
        List<char[]> lettersList = new ArrayList<char[]>(arr.length);

        for(int i = 0; i < arr.length; ++i){
            char[] letters =  mappings.getLetters(arr[i]);
            lettersList.add(letters);
        }

        return lettersList;
    }

}
