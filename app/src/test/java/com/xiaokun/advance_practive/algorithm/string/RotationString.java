package com.xiaokun.advance_practive.algorithm.string;

import java.util.Stack;

/**
 * Created by xiaokun on 2021/5/10.
 *
 * @author xiaokun
 * @date 2021/5/10
 */
public class RotationString {

    public static void main(String[] args) {
        System.out.println(rotation("xiaokun", "nukoaix"));
    }

    public static boolean rotation(String origin1, String origin2) {
        if (origin1.length() != origin2.length()) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < origin1.length(); i++) {
            stack.push(origin1.charAt(i));
        }
        System.out.println(stack);
        for (int i = 0; i < origin2.length(); i++) {
            Character pop = stack.pop();
            System.out.println(pop);
            System.out.println(origin2.charAt(i));
            if (!pop.equals(origin2.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
