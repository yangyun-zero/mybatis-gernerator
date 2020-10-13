package com.yangyun.generator.utils;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/13 14:25
 */
public class StringUtils {

    public static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    public static String captureName(String name) {
        char[] chars=name.toCharArray();
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            chars[0] = (char)(chars[0] + 32);
        }
        return String.valueOf(chars);
    }
}
