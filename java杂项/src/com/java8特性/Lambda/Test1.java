package com.java8特性.Lambda;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.lang.reflect.Array;
import java.util.*;

public class Test1 {

    //匿名内部类
    public void test01(){
        Comparator<Integer> comparable = new Comparator<Integer>() {

            @Override
            public int compare(Integer o1,Integer o2) {
                return Integer.compare(o1,o2);
            }
        };
        TreeSet treeSet = new TreeSet<>(comparable);
    }

    //lambda表达式
    public void test02(){
        Comparator<Integer> com = (x, y) -> Integer.compare(x, y);
        //用法
        TreeSet<Integer> integers = new TreeSet<>(com);

    }

    //伪装数据
    static List<Employee> employees= Arrays.asList(
            new Employee("员工1",20,1111.11),
            new Employee("员工2",22,2020),
            new Employee("员工3",31,1132.11),
            new Employee("员工4",23,45111.11),
            new Employee("员工5",44,5111.11),
            new Employee("员工6",55,56111.11),
            new Employee("员工7",2,1111.11)
    );
    //获取公司中所有年龄大于30的员工对象
    public List<Employee> fiterEmployee(List<Employee> list){
        ArrayList<Employee> employees = new ArrayList<>();
        for (Employee employee : list) {
            if (employee.getAge()>30){
                employees.add(employee);
            }
        }
        return employees;
    }

    static public void FilterEmployee(List<Employee> list,FilterEm<Employee> filterByAge){
        ArrayList<Employee> employees = new ArrayList<>();
        for (Employee employee : list) {
            if (filterByAge.Test(employee))
                employees.add(employee);
        }
        System.out.println(employees);
    }


    public static void main(String[] args) {
        //策略模式
        Test1 test1 = new Test1();
        test1.FilterEmployee(employees,new FilterByAge());
        test1.FilterEmployee(employees,new FilterBySallary());
        System.out.println("----------------------------------");
        //用匿名内部类来完成
        FilterEmployee(employees, new FilterEm<Employee>() {
            @Override
            public boolean Test(Employee o) {
                return o.getSalary()<5000;
            }
        });
        System.out.println("----------------------------------");
        //方式3
        FilterEmployee(employees,(e)->e.getSalary()<=2000);
        //方式4
        employees.stream()
                .filter(e->e.getSalary()>2000)
                .limit(2)
                .map(Employee::getName)
                .forEach(System.out::println);
    }

}

class FilterByAge implements FilterEm<Employee>{
    @Override
    public boolean Test(Employee employee) {
        return employee.getAge()>=30;
    }
}

class FilterBySallary implements FilterEm<Employee>{
    @Override
    public boolean Test(Employee employee) {
        return employee.getSalary()>5000;
    }
}

//员工类
class Employee{
    private String name;
    private int age;
    private double salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public Employee() {
    }

    @Override
    public String  toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", salary='" + salary + '\'' +
                '}';
    }
}