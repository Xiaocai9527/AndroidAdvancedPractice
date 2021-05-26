package com.xiaokun.advance_practive.algorithm.string;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiaokun on 2021/5/10.
 *
 * @author xiaokun
 * @date 2021/5/10
 */
public class UniqueCharInString {

    public static void main(String[] args) {
        System.out.println(uniqueChar("xiaokun"));
        System.out.println(uniqueChar("xiaokunkun"));
        System.out.println(uniqueChar2("xiaokun"));
        System.out.println(uniqueChar2("xiaokunkun"));
    }

    public static boolean uniqueChar2(String origin) {
        for (int i = 0; i < origin.length() - 1; i++) {
            char ch = origin.charAt(i);
            int lastIndex = origin.lastIndexOf(ch);
            if (i != lastIndex) {
                return false;
            }
        }
        return true;
    }

    public static boolean uniqueChar(String origin) {
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < origin.length() - 1; i++) {
            char o = origin.charAt(i);
            if (set.contains(o)) {
                return false;
            }
            set.add(o);
        }
        return true;
    }


}
