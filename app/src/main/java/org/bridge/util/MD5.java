package org.bridge.util;

import java.security.MessageDigest;

/**
 * 用于进行MD5加密的工具类
 */
public final class MD5 {
    static final String HEXES = "0123456789abcdef";

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param raw
     * @return 返回16进制形式的字符串
     */
    public static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
                    HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    /**
     * 对字符串进行加密
     *
     * @param toMd5
     * @return 返回加密后的结果
     */
    public static String digest(String toMd5) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(toMd5.getBytes());
            return getHex(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
