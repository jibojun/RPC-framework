package com.rpc.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 10:40
 * @Description:
 */
public class LogUtil {
    private final static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void logDebug(String msg){
        logger.debug(msg);
    }

    public static void logInfo(String msg){
        logger.info(msg);
    }

    public static void logWarn(String msg){
        logger.warn(msg);
    }

    public static void logError(String msg){
        logger.error(msg);
    }

}
