package org.server.tools;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class MathUtil extends jodd.util.MathUtil {
    
    /**
     * 随机数发生器
     */
    public static Random Random;
    
    static {
        try {
            Random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            Random = new SecureRandom();
        }
    }
    
    /**
     * 精确加法
     * 
     * @param a
     *            数 a
     * @param b
     *            数 b
     * @return 计算结果
     */
    public static double add(double a, double b) {
        return BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).doubleValue();
    }
    
    /**
     * 精确减法
     * 
     * @param a
     *            数 a
     * @param b
     *            数 b
     * @return 计算结果
     */
    public static double subtract(double a, double b) {
        return BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).doubleValue();
    }
    
    /**
     * 精确乘法
     * 
     * @param a
     *            数 a
     * @param b
     *            数 b
     * @return 计算结果
     */
    public static double multiply(double a, double b) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).doubleValue();
    }
    
    /**
     * 精确除法
     * 
     * @param a
     *            数 a
     * @param b
     *            数 b
     * @return 计算结果
     */
    public static double divide(double a, double b) {
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b)).doubleValue();
    }
    
}
