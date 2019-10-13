package com.mr.study.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanxp
 * @version 1.0 2019/5/16
 */
public class Main {
    private final static ch.qos.logback.classic.Logger parentLogger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.mr");
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        parentLogger.setLevel(Level.ERROR);

        logger.info("hello");
        logger.info("hello {}", "占旭鹏");
        logger.debug("world");
        logger.error("error");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }
}
