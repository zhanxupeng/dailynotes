package com.mr.xue.queue;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author zhanxp
 * @version 1.0  2019/9/8
 * @Description todo
 */
public class Main {
    private static void stack() {
        Stack<String> stack = new Stack<>();
        stack.push("aaa");
        stack.push("bbb");
        stack.push("ccc");

        System.out.println("bbb在栈中的位置:" + stack.search("bbb"));

        System.out.println("栈是否为空：" + stack.empty());

        System.out.println("获取栈顶的元素，不删除:" + stack.peek());

        System.out.println("获取栈顶的元素，并删除：" + stack.pop());

        //遍历栈方式1
        for (String s : stack) {
            System.out.print(s + " ");
        }
        System.out.println();

        //遍历栈方式2
        Iterator<String> iterator = stack.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }

    private static void queue() {
        Queue<String> queue = new LinkedList<>();
        //添加元素
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.offer("e");
        for (String q : queue) {
            System.out.print(q + " ");
        }
        System.out.println();

        System.out.println("poll=" + queue.poll()); //返回第一个元素，并在队列中删除
        for (String q : queue) {
            System.out.print(q + " ");
        }
        System.out.println();

        System.out.println("element=" + queue.element()); //返回第一个元素,不删除,如果当前没有值会抛出异常
        for (String q : queue) {
            System.out.print(q + " ");
        }
        System.out.println();

        System.out.println("peek=" + queue.peek()); //返回第一个元素，不删除，如果当前没有值返回null
        for (String q : queue) {
            System.out.print(q + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        stack();
    }
}
