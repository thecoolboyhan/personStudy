package com.test.bean;

public class Food {
    private String no;
    private String abb;

    public Food() {
    }

    public Food(String no, String abb) {
        this.no = no;
        this.abb = abb;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAbb() {
        return abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    @Override
    public String toString() {
        return "Food{" +
                "no='" + no + '\'' +
                ", abb='" + abb + '\'' +
                '}';
    }
}
