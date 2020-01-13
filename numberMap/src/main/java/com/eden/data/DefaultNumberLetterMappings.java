package com.eden.data;

/**
 *  @author Shuxiong Zeng, 2020-1-13
 * */
public class DefaultNumberLetterMappings implements NumberLetterMappings {

    private static final int MAX_VALID_INDEX = 99;

    /**
     * Data : Number Letter Mappings.
     *    A mapping of digit to letters (just like on the telephone buttons)
     * {
     *     0 -> null,
     *     1 -> null,
     *     2 -> ABC, 3 -> DEF, 4 -> GHI, 5-> JKL, 6 -> MNO,
     *     7 -> PQRS,
     *     8 -> TUV,
     *     9 -> WXYZ
     * }
     * */
    private static char[][] mappings = {
                {}, // 0 -> null
                {}, // 1 -> null
                {'a', 'b', 'c'}, // 2 -> ABC
                {'d', 'e', 'f'}, // 3 -> DEF
                {'g', 'h', 'i'}, // 4 -> GHI
                {'j', 'k', 'l'}, // 5-> JKL
                {'m', 'n', 'o'}, // 6 -> MNO
                {'p', 'q', 'r', 's'},   // 7 -> PQRS
                {'t', 'u', 'v'},        // 8 -> TUV
                {'w', 'x', 'y', 'z'},   // 9 -> WXYZ
        };

    // 2 -> {'a', 'b', 'c'}
    @Override
    public char[] getLetters(int i) {

        if(i < 0) {
            throw new IndexOutOfBoundsException("Index should be at range [0, " + (mappings.length - 1) + "].");
        }else if (i > mappings.length - 1 && i > MAX_VALID_INDEX){
            throw new IndexOutOfBoundsException("Index exceeds max index.");
        }else if (i > mappings.length - 1){
            // Support converting the digits from 0 to 99 into letters
            // Empty Array:
            return new char[]{ };
        }

        return mappings[i];
    }
}
