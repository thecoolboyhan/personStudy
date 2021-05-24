package com.多用户及时通讯系统.客户端.servive;

import com.多用户及时通讯系统.common.Message;
import com.多用户及时通讯系统.common.MessageType;
import com.多用户及时通讯系统.客户端.servive.ManageClientServerThread;

import java.io.*;

public class FileClientService {

    /**
     *
     * @param src 源文件
     * @param dest 传输的目录
     * @param sendId 发送者
     * @param getterId 接收者
     */
    public void sendFileToOne(String src,String dest,String sendId,String getterId){
        Message message = new Message();
        message.setMsgType(MessageType.MESSAGE_FILE_MES);
        message.setSender(sendId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        //读取src文件
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[(int) new File(src).length()];

        try {
            fileInputStream=new FileInputStream(src);
            fileInputStream.read(bytes);
            //把文件对应的字节数组设置到message里
            message.setFileBytes(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(message.getSender()+"给"+getterId+"发送了文件"+src+"到对方的"+dest+"目录");
        //发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.GetThreadByUserId(sendId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
