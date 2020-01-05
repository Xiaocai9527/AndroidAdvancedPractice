package com.xiaokun.advance_practive.algorithm;

/**
 * Created by xiaokun on 2019/12/19.
 * 寻找数组中最大值和最小值
 *
 * @author xiaokun
 * @date 2019/12/19
 */
public class FindLargestSmallestNumberMain {

    public static void main(String[] args) {

        //array of 10 numbers
        int arr[] = new int[]{12, 56, 76, 89, 100, 343, 21, 234};

        //assign first element of an array to largest and smallest
        int smallest = arr[0];
        int largest = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > largest)
                largest = arr[i];
            else if (arr[i] < smallest)
                smallest = arr[i];

        }
        System.out.println("Smallest Number is : " + smallest);
        System.out.println("Largest Number is : " + largest);
    }

}
