package com.xiaokun.advance_practive.algorithm.string;

/**
 * 反转字符串
 *
 * @author xiaokun
 * @date 2020/9/10
 */
public class ReverseString {

    public static void main(String[] args) {
        System.out.println(reverseStringByLoop("xiaokun1993"));

        System.out.println(recursiveReverse("xiaokun19940107"));
    }

    private static String reverseStringByLoop(String origin) {
        StringBuilder reverse = new StringBuilder();
        for (int i = origin.length() - 1; i >= 0; i--) {
            reverse.append(origin.charAt(i));
        }
        return reverse.toString();
    }

    private static String recursiveReverse(String origin) {
        if (origin.length() == 1)
            return origin;
        else
            return origin.charAt(origin.length() - 1) +
                    recursiveReverse(origin.substring(0, origin.length() - 1));
    }

}
