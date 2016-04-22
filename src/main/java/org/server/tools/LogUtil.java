package org.server.tools;

import static org.server.tools.SystemUtil.LINE_SEPARATOR;

import org.slf4j.Logger;

import jodd.util.StringUtil;

public class LogUtil {
    
    /**
     * 漂亮输出
     * 
     * @param logger
     *            日志输出
     * 
     * @param output
     *            输出语句
     * @param args
     *            参数集合
     */
    public static void prettyOutput(Logger logger, String output, Object... args) {
        StringBuffer finalOutput = new StringBuffer(LINE_SEPARATOR);
        finalOutput.append("\t");
        
        for (int i = 0; i < args.length; i++)
            output = StringUtil.replaceFirst(output, "{}", args[i].toString());
            
        output = output.replace("\n", "\n\t");
        output = output.replace(LINE_SEPARATOR, LINE_SEPARATOR + "\t");
        output = output.replace("{nl}", LINE_SEPARATOR + "\t");
        
        finalOutput.append(output);
        finalOutput.append(LINE_SEPARATOR);
        
        logger.info(finalOutput.toString());
    }
}
