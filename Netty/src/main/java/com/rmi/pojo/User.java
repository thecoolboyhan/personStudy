package com.rmi.pojo;

import java.io.Serializable;

/**
 * 韩永发
 *
 * @Date 10:41 2022/4/26
 */
public class User implements Serializable {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
