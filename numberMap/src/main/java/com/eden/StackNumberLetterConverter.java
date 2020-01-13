package com.eden;

import com.eden.data.DefaultNumberLetterMappings;
import com.eden.data.NumberLetterMappings;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import static com.eden.utils.MyUtil.isEmpty;

/**
 *  Stack Algorithm instead of Recursive one.
 *
 * @since 1.6+ Deque
 * Convert the digits from 0 to 9 into letters
 * Input: arr[] = {2, 3}
 * Output: ad ae af bd be bf cd ce cf
 *
 * @author Shuxiong Zeng, 2020-1-13
 * */
public class StackNumberLetterConverter implements NumberLetterConverter {
    //private static final Log LOG = LogFactory.getLog(DefaultNumberLetterConverter.class);

    private static NumberLetterMappings mappings = new DefaultNumberLetterMappings();

    public static void main(String[] args) {
        int[] arr = {2, 3};
        StackNumberLetterConverter c = new StackNumberLetterConverter();
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
        combination(lettersList, combinations);

        return combinations;
    }

    /**
     * combinate List{abc, def} to Result{ad ae af bd be bf cd ce cf}
     * @param list  input immutable List{abc, def} ==> char[0] ={'a','b','c'}, char[1]={'d','e','f'}}.
     *
     */
    public static void combination(final List<char[]> list,  List<String> results){

        // Let n := list.size(), then:
        // push index(int i) into stack ,instead of Set.
        // i = 0 stands for: list == Set{list.get(0) ,list.get(1), ... , list.get(n-1)};
        // i = 1 stands for : Set{list.get(1) , ... , list.get(n-1)}
        // i = n-1 stands for : Set{list.get(n-1)}
        // i = n stands for : Set{ } //empty set.

        final int n = list.size();
        //Stack<Node> startIndexStack = new Stack<Node>();
        Deque<Node> startIndexStack = new ArrayDeque<Node>();
        startIndexStack.push(new Node("", 0));
        while(!startIndexStack.isEmpty()){
            Node node = startIndexStack.pop();
            int i = node.startIndex;

            if(i == n){
                // finished in scanning the list,print result:
                //System.out.println(node.combText);
                results.add(node.combText);
                continue;
            }

            // else :  i <= n-1
            char[] headLetterSet = list.get(i);
            if (isEmpty(headLetterSet)){ // digit 0 -> { }
                startIndexStack.push(new Node(node.combText, i+1));
                continue;
            }

            for(int k = headLetterSet.length - 1; k >= 0 ; --k){
                String newResult = node.combText + headLetterSet[k];
                startIndexStack.push(new Node(newResult, i+1));
            }
        } // END WHILE
    }

    static class Node{
        String combText;
        int startIndex;
        public Node(String combText, int startIndex){
            this.combText = combText;
            this.startIndex = startIndex;
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
