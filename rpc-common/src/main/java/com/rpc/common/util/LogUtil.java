package com.rpc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 10:40
 * @Description:
 */
public class LogUtil {

    public static void logDebug(Class cls, String msg) {
        Logger logger = LoggerFactory.getLogger(cls);
        logger.debug(msg);
    }

    public static void logInfo(Class cls, String msg) {
        Logger logger = LoggerFactory.getLogger(cls);
        logger.info(msg);
    }

    public static void logWarn(Class cls, String msg) {
        Logger logger = LoggerFactory.getLogger(cls);
        logger.warn(msg);
    }

    public static void logError(Class cls, String msg) {
        Logger logger = LoggerFactory.getLogger(cls);
        logger.error(msg);
    }

}
