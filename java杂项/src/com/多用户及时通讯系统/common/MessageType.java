package com.多用户及时通讯系统.common;

/**
 * 消息类型
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED="1";//登录成功
    String MESSAGE_LOGIN_FAIL="2";//登录失败
    String MESSAGE_COMM_MES="3";//普通信息包
    String MESSAGE_GET_ONLINE_FRIEND="4";//获取在线列表
    String MESSAGE_RET_ONLINE_FRUEND="5";//返回在线列表
    String MESSAGE_CLIENT_EXIT="6";//客户端请求退出

}
