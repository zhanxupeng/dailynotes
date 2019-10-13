package com.mr.study.threadlocal;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhanxp
 * @version 1.0 2019/7/31
 */
public class Main {
    public static void main(String[] args) {
        String amount="12.32";
        System.out.println(new BigDecimal(amount).multiply(new BigDecimal("100")).longValue());
    }
}
