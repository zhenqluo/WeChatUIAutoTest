package com.wechatui.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * 功能说明：日志记录服务类
 * 修改说明：
 * @author zhenglibing
 * @date 2017-4-25 下午4:01:24
 * @version V0.1
 */
public final class LogService {
    private Logger logger = null;
    private static Map<Class, LogService> loggerList = new HashMap<>(); //用于缓存logger对象
    /**
     * 定义私有构造方法实现单例
     */
    private LogService(Logger logger) {
        this.logger = logger;
    }
    public synchronized static LogService getInstance(Class clzz){
        LogService ls = loggerList.get(clzz);
        if (ls == null){
            ls = new LogService(LoggerFactory.getLogger(clzz));
            loggerList.put(clzz,ls);
        }
        return ls;
    }
    public synchronized static LogService getInstance(){
        LogService ls = loggerList.get(LogService.class);
        if (ls == null){
            ls = new LogService(LoggerFactory.getLogger(LogService.class));
            loggerList.put(LogService.class,ls);
        }
        return ls;
    }

    public Logger getLogger() {
        return logger;
    }


    //在日志中记录异常跟踪栈信息
    public synchronized void logException(Exception e){
        StringWriter trace=new StringWriter();
        e.printStackTrace(new PrintWriter(trace));
        logger.error(trace.toString());
    }


}

/*
slf4j、logback、log4j打印出的日志行号不正确，如何获取正确的行号（调用者类里的行号）:http://www.zyiz.net/tech/detail-231921.html

 */