> 所有交给spring管理的对象默认都是单例的，new对象是一个非常消耗性能的操作。

在这些spring管理的类中，最好不要有状态属性，如果有的话一定要非常小心的使用，由于他们都是单例的，所以线程不安全，



# 动态代理

- spring 的动态代理是由ASM来实现的

## JDK动态代理

> jdk动态代理使用的是java的反射机制来实现的，jdk的动态代理需要被代理的类实现一个接口，然后继承接口。

``` java
public static void main(String[] args) {

        final GirlDJ girlDJ = new GirlDJ();
        /**
         * InvocationHandler:拦截结果对于拦截的结果进行处理
         */
        ProGirlDJ proGirlDJ  = (ProGirlDJ) Proxy.newProxyInstance(GirlDJ.class.getClassLoader(), GirlDJ.class.getInterfaces(), new InvocationHandler() {
            /**
             *
             * @param proxy :代理对象
             * @param method: 执行的方法
             * @param args：参数
             * @return
             * @throws Throwable
             */
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName());
                if (method.getName().endsWith("bath")) {
                    System.out.println("偷看前");
                    Object invoke = method.invoke(girlDJ, args);
                    System.out.println("偷看后");
                    return invoke;
                } else {
                    System.out.println("饭前");
                    Object invoke = method.invoke(girlDJ, args);
                    System.out.println("饭后");
                    return invoke;
                }
            }
        });

        proGirlDJ.bath();
        proGirlDJ.eat();
    }
```



## cglib动态代理

> cglib动态代理是继承需要代理的类，通过增强器来实现动态代理，增强器会继承代理类，cglib的好处是要代理的类不用实现接口。



``` java
public class CGLibFactory implements MethodInterceptor {

    private Object object;

    public CGLibFactory() {
    }

    public CGLibFactory(Object object) {
        super();
        this.obj`ect = object;
    }

    public Object createProxy(){
        //增强器
        Enhancer enhancer = new Enhancer();
        //这是增强器的父类
        enhancer.setSuperclass(object.getClass());
        //回调
        enhancer.setCallback(this);
        return enhancer.create();
    }


    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("前");
        method.invoke(object,objects);
        System.out.println("后");
        return null;
    }
}

```





# Nginx



## 五种IO模型

- 阻塞IO：当没有获取到资源时，会一直等待，直到获取到资源为止。
- 非阻塞IO：任然只有一个线程在运行，但当没有获得到资源时，不会一直等待，继续执行，等到空闲时再回来判断是否获得的到资源。
- 异步IO: 会开辟一个新的线程，新的线程负责等待，而原来的继续执行
- 信号驱动IO：在获取资源时，会给资源发送一个信号值，当获取到资源时，就会收到一个信号值，然后获取。
- 多通道IO：需要获取资源时，会给多个地方同时发送获取的请求，只要有一个地方可以成功获取到资源，就可以继续执行。



## Tengine

> nginx是一个高性能的HTTP和反向代理服务器。其特点是占用内存少，并发能力强。



## nginx和apache的区别

### nginx相对于apache的优点

- 轻量级： 同样起web服务，比apache占用更少的内存和资源
- 抗并发：nginx处理请求为异步非阻塞的，而apache则是阻塞的，在高并发下nginx可以保持低资源，低消耗，高性能。
- 高度模块化的设计，编写模块相对简单。
- 社区活跃，各种高效能模块出品迅速。

### apache相对于nginx的优点：

> rewrite：重写url

- rewrite：比nginx的rewrite强大
- 模块超多，基本想到的都可以找到。
- 少bug



nginx配置简洁，apache复杂

> 最核心的区别在于apache是同步多进程模型，一个连接对应一个进程，nginx是异步的，多个连接（万级别）可以对应一个进程



- nginx性能高的原因

nginx 当请求数超过最大进程数时，它会在一个进程中开辟一个新线程，由于nginx是异步非阻塞的，此非阻塞IO操作是由操作系统来执行的，而apache是阻塞的



- sendfile off/on（异步网络IO）

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624260167667-1624260167658.png)

off时：在网络传输文件中，文件会先被IO操作读到APP，然后APP再通过一次IO操作推给内核去进行网络传输。

on时，会给内核发送一个指令，让内核直接去读文件，然后传输。（异步网络IO）





## 反向代理

> 用户发送请求给代理代理服务器，代理服务在把这些请求分发给后面的服务器

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624416591118-1624416591103.png)

### upsteam

给需要负载的服务器分组

如果只写server就是默认轮巡（一人一下）

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624426250073-1624426250060.png)

### 权重

给可以通过weight属性给负载的服务器设置权重，权重（1-10）高的服务器被负载的几率大。

![](https://cdn.jsdelivr.net/gh/weidadeyongshi2/th_blogs@main/image/1624426551982-1624426551978.png)

### session共享

tomcat可以通过Memcache来实现多台服务器共享同一个session





------------------------------

# <spring5核心原理与30个类手写实战>

# spring内功心法

### 软件架构设计原则

#### 开闭原则

一个软件实体，应该对扩展开放，对修改关闭。它强调用抽象构建框架，用实现扩展细节，可以提高软件系统的可复用性和可维护性。

#### 依赖倒置原则

设计代码结构时，高层代码不应该依赖底层模块，二者都应该依赖其抽象。抽象不应该依赖细节，细节应该依赖抽象。通过依赖倒置，可以减少类与类之间的耦合性，提高系统的稳定性，提高代码的可读性和可维护性，并可以降低修改程序的风险。

#### 单一职责原则

不要存在多于一个导致类变更的原因。假设一个类负责两个职责，一旦发生需求变更，修改其中一个职责的逻辑代码，有可能导致另一个职责的功能发生故障。一个类，接口或方法只负责一项职责。

#### 接口隔离原则

用多个专门的接口，而不是依赖一个总接口，客户端不应该依赖他不需要的接口。

1. 一个类对另一个类的依赖应该建立在最小的接口之上。
2. 建立单一接口，不要建立庞大臃肿的接口。
3. 尽量细化接口，接口中的方法尽量少（不是越少越好，一定要适度）。

#### 迪米特原则（最少知道原则）

一个对象应该对其他对象保持最少了解。只和朋友交流，不和陌生人说话。

- 朋友：

出现在成员变量，方法的输入，输出参数中的类。

> 出现在方法内部的类不属于朋友类。

#### 里式替换

如果对每个类型为T1的对象o1，都有类型为T2的对象o2，使得所有以T1定义的所有程序p，在所有的对象o1都替换成o2时，程序p的行为没有发生变化，那么类型T2是类型T1的子类型。

可以理解为：一个软件实体如果适用于一个父类，那么一定适用于其子类，所有引用父类的地方必须能透明的适用其子类对象，子类对象能够替换其父类对象，而程序逻辑不变。

> 子类可以扩展父类的功能，但不能改变其原有的功能。

1. 子类可以实现父类的抽象方法，但不能覆盖非抽象方法。
2. 子类可以增加其特有的方法。
3. 当子类的方法重载父类的方法时，方法的前置条件（即方法的输入/入参）要比父类的输入方法更宽松。
4. 当子类的方法实现父类的方法时，方法的后置条件（即方法的输出/返回值）要比父类更严格或与父类一样。

### 常用设计模式

#### 工厂模式

##### 简单工厂

不属于GoF的23种设计模式，简单工厂模式适用于工厂类负责创建的对象较少的场景，且客户端只需要传入工厂类的参数，对于如何创建对象不需要关心。

- 缺点

工厂类职责相对过重，不易于扩展过于复杂的产品结构。

##### 工厂方法模式

定义一个创建对象的接口，但让实现这个接口的类来决定实例化哪个类，工厂方法模式让类的实例化推迟到了子类中进行。在工厂方法模式中用户只需要关心所需产品对应的工厂，无需关心创建细节，而且加入新的产品时符合开闭原则。

- 缺点

1. 类的个数容易过多，增加复杂度。
2. 增加了系统的抽象性，和理解难度。



##### 抽象工厂模式

提供一个创建一系列相关或相互依赖对象的接口，无需指定他们的具体类。客户端（应用层）不依赖产品类如何被创建，如何被实现等细节，强调的是一系列相关的产品对象，一起使用创建对象需要大量重复的代码。需要提供一个产品类的库，所有产品以同样的接口出现，从而使客户端不依赖具体实现。

- 缺点

1. 规定了所有可能被创建的产品集合，产品族中扩展新的产品困难，需要修改抽象工厂的接口。
2. 增加了系统的抽象性和理解难度。



#### 单例模式

> 确保一个类在任何情况下都绝对只有一个实例，并提供一个全局访问点。单例模式时创建型模式。

保证内存里只有一个实例，减少了内存的开销，还可以避免对资源的过重占用。

##### 饿汉式单例模式

在类加载的时候就立即初始化，并且创建单例对象。它绝对线程安全，在线程还没有出现以前就实例化了，不可能存在访问安全问题。

- 优点：没有加任何锁，执行效率比较高，用户体验比懒汉式单例模式更好。
- 缺点：类加载的时候就初始化，不管用与不用都占着空间，浪费了内存。

> spring中IOC容器ApplicationContext本身就是典型的饿汉式单例模式。

- 两种写法：

```java
public class HungrySingleton {
    //利用静态属性创建对象。
    private static final HungrySingleton HUNGRY_SINGLETON=new HungrySingleton();
    
    private HungrySingleton(){}
    
    public static HungrySingleton getInstance(){
        return HUNGRY_SINGLETON;
    }
}
```

利用静态代码块：

```java
public class HungrySingleton1 {
    //利用静态代码块创建
    private static final HungrySingleton1 HUNGRY_SINGLETON_1;
    
    static {
        HUNGRY_SINGLETON_1=new HungrySingleton1();
    }
    
    private HungrySingleton1(){}
    
    public static HungrySingleton1 getInstance(){
        return HUNGRY_SINGLETON_1;
    }
}
```



##### 懒汉式单例模式

```java
public class LazySimpleSingleton {

    private LazySimpleSingleton(){}

    private volatile static LazySimpleSingleton lazySimpleSingleton=null;

    public static LazySimpleSingleton getInstance(){
        if (lazySimpleSingleton==null) {
            synchronized (LazySimpleSingleton.class){
                if (lazySimpleSingleton == null) {
                    lazySimpleSingleton = new LazySimpleSingleton();
                }
            }
        }
        return lazySimpleSingleton;
    }
}
```

无论如何，上面的方式还是上锁，对性能还是有所损耗

- 无锁懒汉式（静态内部类）

```java
public class LazyInnerClassSingleton {
    
    private LazyInnerClassSingleton(){}
    
    public static final LazyInnerClassSingleton getInstance(){
        return LazyHolder.LAZY;
    }
    
    private static class LazyHolder{
        private static final LazyInnerClassSingleton LAZY=new LazyInnerClassSingleton();
    }
}
```

> 内部类在方法调用之前初始化，可以避免线程安全问题。

- 利用反射破坏上面的单例

```java
class LazyInnerClassSingletonTest{
    public static void main(String[] args) {
        Class<?> aClass = LazyInnerClassSingleton.class;
        try {
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Object o = declaredConstructor.newInstance();
            Object o1 = declaredConstructor.newInstance();
            System.out.println(o==o1);//false
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

通过反射可以暴力调用另一个类的私有方法，来创建多个实例。

- 防止反射破坏

```java
public class LazyInnerClassSingleton {

    private LazyInnerClassSingleton(){
        if (LazyHolder.LAZY!=null){
            //只能通过内部类的静态属性来创建此类
            new RuntimeException();
        }
    }

    public static final LazyInnerClassSingleton getInstance(){
        return LazyHolder.LAZY;
    }

    private static class LazyHolder{
        private static final LazyInnerClassSingleton LAZY=new LazyInnerClassSingleton();
    }
}
```

##### 注册式单例模式

将每一个实例都登记到一个地方，使用唯一的标识获取实例。

- 枚举式单例

```java
public enum EnumSingleton {
    INSTANCE;
    private Object data;
    public Object getData(){
        return data;
    }
    public void setData(Object data){
        this.data=data;
    }
    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}
```

枚举类单例模式，是一种饿汉式的创建形式，枚举类通过类名和类对象类找到一个唯一的枚举对象，枚举对象不可能被类加载器加载多次。

同时，反射的newInstance()方法中会判断如果是Modifier.ENUM枚举类型，就是直接抛出异常。因此反射也无法破话枚举单例。

- 线程单例实现ThreadLocal

```java
class ThreadLocalSingleton{
    private static final ThreadLocal<ThreadLocalSingleton> THREAD_LOCAL_SINGLETON =
            new ThreadLocal<ThreadLocalSingleton>(){
                @Override
                protected ThreadLocalSingleton initialValue() {
                    return new ThreadLocalSingleton();
                }
            };

    private ThreadLocalSingleton(){}

    public static ThreadLocalSingleton getInstance(){
        return THREAD_LOCAL_SINGLETON.get();
    }
}
```



#### 原型模式

创建对象的种类，通过复制这些原型创建新的对象.



##### 浅克隆

> 浅克隆只是完整复制了值类型数据，没有赋值引用对象。换言之，所有的引用对象仍然指向原来的对象，修改任意一个对象的属性，两个对象的值都会改变。

```java
class ConcretePrototypeA implements Prototype{
    private int age;
    private String name;
    private List hobbies;

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public List getHobbies() {return hobbies;}
    public void setHobbies(List hobbies) {this.hobbies = hobbies;}

    @Override
    public ConcretePrototypeA clone() {
        ConcretePrototypeA concretePrototypeA = new ConcretePrototypeA();
        concretePrototypeA.setAge(this.age);
        concretePrototypeA.setName(this.name);
        concretePrototypeA.setHobbies(this.hobbies);
        return concretePrototypeA;
    }
}
```



##### 深克隆

> 利用io来实现深克隆

```java
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
```



#### 代理模式

##### 动态代理

###### JDK动态代理

```java
public class JDKMeipo implements InvocationHandler {
    //被代理对象
    private Object target;
    //通过反射创建代理类被代理后的对象
    public Object getInstance(Object target){
        this.target=target;
        Class<?> aClass = target.getClass();
        //传入被代理类的类加载器，和它所实现的接口
        return Proxy.newProxyInstance(aClass.getClassLoader(),aClass.getInterfaces(),this);
    }
    //设置对于代理类的代理方法要执行什么操作
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object invoke = method.invoke(this.target, args);
        after();
        return invoke;
    }

    private void before(){
        System.out.println("我是媒婆，已经确认你的需求");
        System.out.println("开始物色");
    }
    private void after(){
        System.out.println("如果觉得合适就办事");
    }
}
```



> 通过字节码分析，jdk动态代理在字节码层面，创建了一个继承proxy,proxy中有受保护的InvocationHandler，我们自定义未代理类实现了 它的invoke方法。通过实现相同接口，来创建一个新类。

```java
public final void findLove() throws  {
    try {
        super.h.invoke(this, m3, (Object[])null);
    } catch (RuntimeException | Error var2) {
        throw var2;
    } catch (Throwable var3) {
        throw new UndeclaredThrowableException(var3);
    }
}
```

- 手写源码

JDK动态代理生成对象的步骤：

1. 获取被代理对象的引用，并且获取它的所有接口，反射获取。
2. JDK代理类重新生成一个新的类，同时新的类要实现被代理类实现的所有接口。
3. 动态生成java代码，新加的业务逻辑方法由一定的逻辑代码调用（在代码中体现）。
4. 编译新生成的java代码.class文件。
5. 重新加载到JVM中运行。



###### 自定义JDK动态代理讲解

