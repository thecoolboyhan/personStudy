package com.网络编程.com.上传图片;

import java.io.*;

public class StreamUtil {
    /**
     * 把输入流转换为byte数组
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] streamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        while ((len=is.read(bytes))!=-1){
            byteArrayOutputStream.write(bytes,0,len);
        }
        byte[] bytes1 = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return bytes1;
    }

    public static String streamToString(InputStream is) throws IOException {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line+"\r\n");
        }
        return stringBuilder.toString();
    }
}
