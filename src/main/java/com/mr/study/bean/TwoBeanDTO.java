package com.mr.study.bean;

/**
 * @author zhanxp
 * @version 1.0 2019/8/6
 */
public class TwoBeanDTO<V, T> {
    private V one;
    private T two;

    public V getOne() {
        return one;
    }

    public void setOne(V one) {
        this.one = one;
    }

    public T getTwo() {
        return two;
    }

    public void setTwo(T two) {
        this.two = two;
    }
}
