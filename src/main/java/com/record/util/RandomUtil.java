package com.record.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomUtil {

    private static final SecureRandom sr;

    static {
        SecureRandom temp;
        try {
            // 获取高强度安全随机数生成器, 需查看主机的熵 cat /proc/sys/kernel/random/entropy_avail , 如果太少，会等待/dev/random有足够的熵，导致阻塞
//            temp = SecureRandom.getInstanceStrong();
            // 获取安全随机数, 最好添加启动参数 -Djava.security.egd=file:/dev/urandom
            temp = SecureRandom.getInstance("SHA1PRNG");
            // 或者使用这个, 不会阻塞
//            temp =  SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (NoSuchAlgorithmException e) {
            // 获取普通的安全随机数生成器
            temp = new SecureRandom();
        }
        sr = temp;
    }

    public static double strongSecureDoubleRandomAbs() {
        byte[] buffer = new byte[8];
        // 用安全随机数填充buffer
        sr.nextBytes(buffer);
        long randomFactor = 0;
        for (byte b : buffer) {
            randomFactor = (randomFactor << 8) | (b & 0xFF);
        }
        double randomValue = sr.nextDouble() + (randomFactor / (double) Long.MAX_VALUE);
        // 设置上限为max
        return Math.abs(randomValue % 1.0);
    }
}
