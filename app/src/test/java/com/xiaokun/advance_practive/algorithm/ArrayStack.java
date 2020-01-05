package com.xiaokun.advance_practive.algorithm;

/**
 * Created by xiaokun on 2019/12/26.
 * 数组实现stack
 *
 * @author xiaokun
 * @date 2019/12/26
 */
public class ArrayStack {

    int size;
    int arr[];
    int top;

    ArrayStack(int size) {
        this.size = size;
        this.arr = new int[size];
        this.top = -1;
    }

    public void push(int pushedElement) {
        if (!isFull()) {
            top++;
            arr[top] = pushedElement;
            System.out.println("Pushed element:" + pushedElement);
        } else {
            System.out.println("Stack is full !");
        }
    }

    public int pop() {
        if (!isEmpty()) {
            int returnedTop = top;
            top--;
            System.out.println("Popped element :" + arr[returnedTop]);
            return arr[returnedTop];

        } else {
            System.out.println("Stack is empty !");
            return -1;
        }
    }

    public int peek() {
        if (!this.isEmpty())
            return arr[top];
        else {
            System.out.println("Stack is Empty");
            return -1;
        }
    }

    public boolean isEmpty() {
        return (top == -1);
    }

    public boolean isFull() {
        return (size - 1 == top);
    }

    public static void main(String[] args) {
        ArrayStack arrayStack = new ArrayStack(10);
        arrayStack.pop();
        System.out.println("=================");
        arrayStack.push(10);
        arrayStack.push(30);
        arrayStack.push(50);
        arrayStack.push(40);
        System.out.println("=================");
        arrayStack.pop();
        arrayStack.pop();
        arrayStack.pop();
        System.out.println("=================");
    }
}
