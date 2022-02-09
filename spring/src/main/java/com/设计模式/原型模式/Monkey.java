package com.设计模式.原型模式;

import java.io.*;
import java.util.Date;

/**
 * 深克隆
 * @author rose
 */
public class Monkey {
    public int height;
    public int weight;
    public Date birthday;
}
class JinGuBang implements Serializable{
    public float h= 100;
    public float d= 10;
    public void big(){
        this.d=2;
        this.h=2;
    }
    public void small(){
        this.d/=2;
        this.h/=2;
    }
}
class QiTianDaSheng extends Monkey implements Cloneable,Serializable{
    public JinGuBang jinGuBang;
    public QiTianDaSheng(){
        this.birthday=new Date();
        this.jinGuBang=new JinGuBang();
    }
    @Override
    protected Object clone() {
        return this.deepClone();
    }
    public Object deepClone(){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            QiTianDaSheng copy = (QiTianDaSheng) ois.readObject();
            copy.birthday=new Date();
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public QiTianDaSheng shallowClone(QiTianDaSheng target){
        QiTianDaSheng qiTianDaSheng = new QiTianDaSheng();
        qiTianDaSheng.height=target.height;
        qiTianDaSheng.weight=target.weight;
        qiTianDaSheng.jinGuBang=target.jinGuBang;
        qiTianDaSheng.birthday=new Date();
        return qiTianDaSheng;
    }
}

class DeepCloneTest{
    public static void main(String[] args) {
        QiTianDaSheng qiTianDaSheng = new QiTianDaSheng();
        QiTianDaSheng clone = (QiTianDaSheng) qiTianDaSheng.clone();
        System.out.println("深克隆"+(qiTianDaSheng.jinGuBang==clone.jinGuBang));

        QiTianDaSheng q = new QiTianDaSheng();
        QiTianDaSheng n = q.shallowClone(q);
        System.out.println("浅克隆"+(q.jinGuBang==n.jinGuBang));
    }
}