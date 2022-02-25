package com.设计模式.适配器模式;

import java.io.PipedWriter;

public class PowerAdapter implements DC5 {
    private AC220 ac220;
    public PowerAdapter(AC220 ac220){
        this.ac220=ac220;
    }

    @Override
    public int outputDC5V() {
        int i = ac220.outputACC220V();
        //变压器
        int i1 = i / 44;
        System.out.println("使用PowerAdapter输入AC"+i+"输出"+i1+"V");
        return i1;
    }

    public static void main(String[] args) {
        PowerAdapter powerAdapter = new PowerAdapter(new AC220());
        powerAdapter.outputDC5V();
    }
}
