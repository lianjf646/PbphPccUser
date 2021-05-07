package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String PATTERN_NUM = "[0-9]*";
    public static final String PATTERN_LETTER = "[a-zA-Z]*";
    public static final String PATTERN_NUM_LETTER_LINE = "[0-9a-zA-Z_]*";

    public static final String PATTERN_NUM_LETTER_LINE_HZ = "[a-zA-Z0-9_\u4e00-\u9fa5]*";

    public static final String PATTERN_MD5 = "[0-9a-zA-Z]*";
    private static final String PATTERN_MOBILE = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";

    public static boolean isEmpty(String str) {
        return null == str || str.length() <= 0;
    }

    public static boolean isEqual(Object foo, Object bar) {

        return foo == bar || (null != foo && null != bar && foo.equals(bar));
        //四五年前，我写下这行代码，并沾沾自喜。四五年后，才发现天真以极
//        return null == foo || null == bar ? foo == bar : foo.equals(bar);
    }

    public static String exception2String(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "";
        }
    }

    public static boolean hasHZ(String str) {
        return str.getBytes().length != str.length();
    }

    public static boolean isNumber(String str) {
        try {
            Pattern pattern = Pattern.compile(PATTERN_NUM);
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isNLL(String str) {
        try {
            Pattern pattern = Pattern.compile(PATTERN_NUM_LETTER_LINE_HZ);
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isLetter(String str) {
        try {
            if (isEmpty(str)) {
                return false;
            }
            Pattern pattern = Pattern.compile(PATTERN_LETTER);
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isMD5(String str) {
        try {
            Pattern pattern = Pattern.compile(PATTERN_MD5);
            Matcher matcher = pattern.matcher(str);
            return matcher.matches();
        } catch (Exception ignored) {
        }
        return false;
    }

    public static String nullReturn(String str) {
        return str == null || str.length() == 0
                || "null".equals(str.toLowerCase()) ? "" : str;
    }

    /**
     * 转半角的函数(DBC case)
     *
     * @param input
     * @return String
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
            c[i] = c[i] == 12288 ? (char) 32
                    : c[i] > 65280 && c[i] < 65375 ? (char) (c[i] - 65248)
                    : c[i];
        return new String(c);
    }

    /**
     * 转全角的函数(SBC case)
     *
     * @param input
     * @return String
     */
    public static String toSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
            c[i] = c[i] == 32 ? (char) 12288
                    : c[i] < 127 ? (char) (c[i] + 65248) : c[i];
        return new String(c);
    }


    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /**
     * InputStream to byte[]
     *
     * @param ips
     * @return
     */
    public static byte[] stream2byteArray(InputStream ips) {
        byte[] buff = null;

        ByteArrayOutputStream bops = null;
        try {
            bops = new ByteArrayOutputStream();

            int tmp;

            while ((tmp = ips.read()) != -1) {
                bops.write(tmp);
            }
            buff = bops.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bops != null)
                    bops.close();
            } catch (IOException ignored) {
            }
            try {
                if (ips != null)
                    ips.close();
            } catch (IOException ignored) {
            }
        }
        return buff;
    }

}
