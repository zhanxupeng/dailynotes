package com.mr.study.queue;

/**
 * @author zhanxp
 * @version 1.0 2019/8/9
 */
public class SimpleArrayQueue<T> {
    private Object[] array;
    //当前取的位置
    private int first;
    //下一个放入的位置
    private int last;
    private final static int DEFAULT_SIZE = 4;
    private int currentSize;

    public SimpleArrayQueue() {
        array = new Object[DEFAULT_SIZE];
        first = 0;
        last = 0;
        currentSize = 0;
    }

    public void put(T t) {
        if (currentSize >= array.length) {
            throw new RuntimeException("超过长度");
        }
        currentSize++;
        array[(last = getNextIndex(last))] = t;

    }

    private int getNextIndex(int index) {
        return (index + 1) == array.length ? 0 : (index + 1);
    }

    public T get() {
        if (currentSize == 0) {
            throw new RuntimeException("不存在值");
        }
        currentSize--;
        return (T) array[(first = getNextIndex(first))];

    }
}
