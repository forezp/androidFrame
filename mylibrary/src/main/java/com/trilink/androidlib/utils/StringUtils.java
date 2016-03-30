package com.trilink.androidlib.utils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.MessageDigest;

/**
 * 字符串操作类
 * Created by forezp on 2015/12/28.
 */
public class StringUtils {




    /**
     * 将字符串保存到文件中
     *
     * @param desc
     *            : 要保存的字符 Path:保存文件的绝对路
     *
     */
    public static void saveStringToFile(String desc, String absPath) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File file = null;
        if (desc == null || "".equals(desc.trim())) {
            return;
        }
        if (absPath == null || "".equals(absPath.trim())) {
            return;
        }
        try {
            file = new File(absPath);
            if (file != null && file.exists()) {
                return;
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(desc.getBytes());
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * MD5加密
     * @param str
     * @return
     */
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    private static final int BUFF_SIZE = 1024;

    /**
     * 把文件转换成字符串
     * @param file
     * @param encoding
     * @return
     */
    public static String file2String(File file, String encoding) {
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            if (encoding == null || "".equals(encoding.trim())) {
                reader = new InputStreamReader(new FileInputStream(file));
            } else {
                reader = new InputStreamReader(new FileInputStream(file),
                        encoding);
            }
            // 将输入流写入输出流
            char[] buffer = new char[BUFF_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return writer.toString();
    }

    // 把inputstream转换为字符串（方法一）
    public static String getStr1FromInputstream(InputStream input) {
        String result = null;
        int i = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((i = input.read()) != -1) {
                baos.write(i);
            }
            result = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 把inputstream转换为字符串（方法二）
    public static String getStr2FromInputstream(InputStream input) {
        int i = -1;
        String result = null;
        byte[] b = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try {
            while ((i = input.read(b)) != -1) {
                sb.append(new String(b, 0, i));
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取有颜色的文字，这里默认为橙色
     *
     * @param str
     *            文字内容
     * @return
     */
    public static String getHtmlColorString(String color, String str) {
        StringBuffer sb = new StringBuffer();
        sb.append("<font color='" + color + "'>");
        sb.append(str);
        sb.append("</font>");
        return sb.toString();
    }



}

