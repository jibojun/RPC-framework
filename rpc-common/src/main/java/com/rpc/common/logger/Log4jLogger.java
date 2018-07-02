package com.rpc.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Bojun Ji
 * @Date: Created in 2018-07-02 10:40
 * @Description:
 */
public class Log4jLogger {
    private final Logger logger = LoggerFactory.getLogger(Log4jLogger.class);

    public void logDebug(String msg){
        logger.debug(msg);
    }

    public void logInfo(String msg){
        logger.info(msg);
    }

    public void logWarn(String msg){
        logger.warn(msg);
    }

    public void logError(String msg){
        logger.error(msg);
    }

}
