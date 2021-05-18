package com.多用户及时通讯系统.common;

import java.io.File;
import java.io.Serializable;

/**
 * 用户
 * 如果想让一个对象可以被网络传输（IO处理）一定要序列化
 */
public class User implements Serializable {
    //保证序列化兼容性
    private static final long serialVersionUID = 1L;

    private String userId;
    private String passwd;

    public User() {
    }

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
