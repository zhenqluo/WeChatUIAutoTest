package com.wechatui.trytodo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author law
 * @create 2022-04-2022/4/27 9:04
 * 说明：测试日志的使用
 */
public class Log4j2Test {
    private final static Logger logger = LoggerFactory.getLogger(Log4j2Test.class);

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        for(int i = 0; i < 1; i++) {
            logger.trace("trace level");
            logger.debug("debug level");
            logger.info("info level");
            logger.warn("warn level");
            logger.error("error level");
            logger.info("测试测试{}测试{}","test1","test2");
        }

        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {}
        logger.info("请求处理结束，耗时：{}毫秒", (System.currentTimeMillis() - beginTime));    //第一种用法
        logger.info("请求处理结束，耗时：" + (System.currentTimeMillis() - beginTime)  + "毫秒");    //第二种用法
    }
}
