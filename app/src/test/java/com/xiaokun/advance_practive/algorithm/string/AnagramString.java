package com.xiaokun.advance_practive.algorithm.string;

import java.util.Arrays;

/**
 * Created by xiaokun on 2021/5/10.
 *
 * @author xiaokun
 * @date 2021/5/10
 */
public class AnagramString {

    public static void main(String[] args) {
        String word2 = "xiaokun";
        int index = 3;
        word2 = word2.substring(0, index) + word2.substring(index + 1);
        System.out.println(word2);

        System.out.println(isAnagram("xiaokun", "kunxiao"));

        System.out.println(isAnagramBySort("Xiaokun坤", "kun坤Xiao"));

        System.out.println(isAnagramByIntArray("Xiaokun", "kunXIao"));
        System.out.println(isAnagramByIntArray("Xiaokun", "kunXiao"));
    }

    //不能包含中文字符
    public static boolean isAnagramByIntArray(String word1, String word2) {
        if (word1.length() != word2.length()) {
            return false;
        }
        int[] count = new int[256];
        for (int i = 0; i < word1.length(); i++) {
            count[word1.charAt(i)]++;
            count[word2.charAt(i)]--;
        }
        System.out.println(Arrays.toString(count));
        for (int i = 0; i < 256; i++) {
            if (count[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnagram(String word1, String word2) {
        if (word1.length() != word2.length()) {
            return false;
        }
        for (int i = 0; i < word1.length(); i++) {
            char c = word1.charAt(i);
            int index = word2.indexOf(c);
            if (index == -1) {
                return false;
            } else {
                word2 = word2.substring(0, index) + word2.substring(index + 1);
            }
        }
        return word2.isEmpty();
    }

    public static boolean isAnagramBySort(String word1, String word2) {
        if (word1.length() != word2.length()) {
            return false;
        }
        return sortString(word1).equals(sortString(word2));
    }

    private static String sortString(String value) {
        char[] chars = value.toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }

}

