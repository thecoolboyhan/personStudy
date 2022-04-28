# IOC

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

## spring内功心法

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

##### JDK动态代理

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

1.先创建一个自定义的InvocationHandler接口，用来找到不同的代理工具类

```java
public interface GPInvocationHandler {
    Object invoke(Object var1, Method var2, Object[] var3) throws Throwable;
}
```

2.创建代理工具类时，要更具传入的代理对象，通过proxy类来生成一个实现了被代理类相同接口的类。利用proxy来实现此类的java源代码，在编译成.class文件，然后通过自定义类加载器把class文件加载到jVM中。

```java
Class proxyClass = classLoader.findClass("$Proxy0");
Constructor constructor = proxyClass.getConstructor(GPInvocationHandler.class);
```

3.让代理工具类实现上面的invoke方法，在invoke方法中，利用反射，执行原方法，和自己想要添加的方法。

```java
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    before();
    method.invoke(target,args);
    after();
    return null;
}
```



> JDK动态代理实际上执行的对象并非原来的，而是执行了下面这个实现了和原对象相同接口的新对象，而此对象又利用反射执行了相应代理工具类的invoke方法。

``` java
package com.代理.dongDJ.myJDKdong;
import com.代理.dongDJ.Person;
import java.lang.reflect.*;
public class $Proxy0 implements com.代理.dongDJ.Person{
GPInvocationHandler h;
public $Proxy0(GPInvocationHandler h) {
this.h=h;}
public void findLove() {
try{
Method m= com.代理.dongDJ.Person.class.getMethod("findLove",new Class[]{});
this.h.invoke(this,m,new Object[]{});
}catch(Error _ex) { }catch(Throwable e){
throw new UndeclaredThrowableException(e);
}}}

```



##### CGLib动态代理

- 调用过程

  代理对象调用 this.findLove()方法 ，调用拦截器，methodProxy.invokeSuper，CGLIB$findLove$0，被代理对象findLove()方法。

> CGlib采用了FastClass机制：为代理类和被代理类各生成一个类，这个类会为代理类和被代理类的方法分配一个index（int类型）；这个index当作一个入参，FastClass就可以直接定位要调用的方法并直接进行调用，省去反射调用，所以调用效率比JDk代理通过反射调用高。

FastClass并不是跟代理类一起生成的，而是在第一次执行MethodProxy的invoke()或invokeSuper()方法时生成的，并放在了缓存中。

> FastClass是单例的

```java
private void init() {
    if (this.fastClassInfo == null) {
        synchronized(this.initLock) {
            if (this.fastClassInfo == null) {
                MethodProxy.CreateInfo ci = this.createInfo;
                MethodProxy.FastClassInfo fci = new MethodProxy.FastClassInfo();
                fci.f1 = helper(ci, ci.c1);
                fci.f2 = helper(ci, ci.c2);
                fci.i1 = fci.f1.getIndex(this.sig1);
                fci.i2 = fci.f2.getIndex(this.sig2);
                this.fastClassInfo = fci;
                this.createInfo = null;
            }
        }
    }
}
```



###### 对比

1. JDK动态代理实现了被代理对象的接口，GCLib代理继承了被代理对象。
2. JDk动态代理和CGLib代理都在运行期生成字节码，JDK动态代理直接写class字节码，CGLib代理使用ASM框架写Class字节码，CGLib代理实现更复杂，生成代理类比JDK动态代理效率低。
3. JDK动态代理调用代理方法是通过反射机制调用的，CGLib代理是通过FastClass机制直接调用方法的，CGLib代理的执行效率更高。



##### spring动态代理

- sspring中的代理选择原则

1. 当bean有实现接口时，spring就会用JDK动态代理。
2. 当bean没有实现接口时，spring会选择CGLib代理。
3. spring可以通过配置强制使用CGLib代理，只需在spring的配置文件中加入如下代码：

```xml
<aop:aspectj-autoproxy proxy-target-class="true"/>
```

- 代理模式的优缺点

优点：

1. 代理模式能把代理对象和真实被调用对象分离。
2. 在一定程度上降低了系统的耦合性，扩展性好。
3. 可以起到保护目标对象的作用。
4. 可以增强目标对象的功能。

缺点：

1. 代理模式会造成系统设计中类的数量增加。
2. 在客户端和目标对象中增加一个代理对象，会导致请求处理速度变慢。
3. 增加了系统的复杂度。



#### 委派模式详解

委派模式不属于GoF23种设计模式。委派模式的基本作用就是负责任务的调度和分配，跟代理模式很像，可以看作一种特殊情况下的静态的全权代理，但是代理模式注重过程，而委派模式注重结果。委派模式在spring中应用的非常多，常用的DispatcherServlet就是用到了委派模式。

例如：老板给项目经理下达任务，项目经理会更具实际情况给每个员工派发任务，待员工把任务完成后，再由项目经理汇报结果。

> 在spring源码中，以Delegate结尾的地方都实现了委派模式。

#### 策略模式

> 定义了算法家族并分别封装起来，让它们之间可以互相替换，此模式使得算法的变化不会影响使用算法的用户。

- 使用场景

1. 系统中有很多类，而它们的区别仅仅在于行为不同。
2. 一个系统需要动态的在几种算法中选择一种。



- JDK中的策略模式

比较器 Comparator接口，compare()方法是策略模式的抽象实现，我们常把Comparator接口作为参数实现排序策略。Arrays类的parallelSort方法，TreeMap的构造方法等。

- 优缺点

优点：

1. 策略模式符合开闭原则。
2. 策略模式可以避免使用多重条件语句，如if...else语句，swithc语句。
3. 使用策略模式可以提高算法的保密性和安全性。

缺点：

1. 客户端必须知道所有策略，并且自行决定使用哪一个策略类。
2. 代码中产生非常多的策略类，增加了代码的维护难度。

#### 模板模式

> 又叫模板方法模式，指定义一个算法骨架，并允许子类为一个或者多个步骤提供实现。模板模式使得子类可以在不改变算法结构的情况下，重新定义算法的某些步骤，属于行为型设计模式。

- 适用场景

1. 一次性实现一个算法的不变部分，并将可变的行为留给子类来实现。
2. 各子类中公共的行为被提取出来，并集中到一个公共的父类中，从而避免代码的重复。



- 源码中的模板模式

AbstractList的get()方法

HttpServlet的service(),doGet(),doPost()方法。

- 优缺点

优点：

1. 利用模板模式，将相同处理逻辑的代码放到抽象父类中，可以提高代码的复用性。
2. 将不同的代码放到不同的子类中，通过对子类的扩展增加新的行为，可以提高代码的扩展性。
3. 把不变的行为写在父类中，去除子类的重复代码，提供了一个很好的代码复用平台，符合开闭原则。

缺点：

1. 每个抽象类都需要一个子类来实现，导致了类的数量增加。
2. 类数量的增加间接的增加了系统的复杂度。
3. 因为继承关系自身的缺陷，如果父类添加新的抽象方法，所有子类都要改一遍。



#### 适配器模式

指将一个类的接口转换成用户期望的另一个接口，使原本接口不兼容的类可以一起工作，属于结构型设计模式。

- 业务场景

1. 已经存在的类的方法和需求不匹配（方法结果相同或相似）的情况。
2. 适配器模式不是软件初始阶段考虑的设计模式，使随着软件的发展，由于不同厂家、不同产品造成功能类似而接口不同的解决方案，有点亡羊补牢的感觉。

- 适配器模式的优缺点

优点：

1. 能提高类的透明性和复用性，现有的类会被复用但不需要改变。
2. 目标类和适配器类解耦，可以提高程序的扩展性。
3. 在很多业务场景中符合开闭原则。

缺点：

1. 在适配器代码编写过程中需要进行全面考虑，可能会增加系统的复杂性。
2. 增加了代码的阅读难度，降低了代码的可读性，过多使用适配器会使代码变得凌乱。



#### 装饰者模式

> 在不改变原有对象的基础上，将功能附加到对象上，提供了比继承更有弹性的方案（扩展原有对象的功能），属于结构型模式。

- 场景

1. 扩展一个类或给一个类添加附加职责。
2. 动态给一个对象添加功能，这项功能可以再动态的撤消。

- 装饰者模式和适配器模式对比

装饰者模式和适配器模式都是包装模式，装饰者模式也是一种特殊的代理模式。

|      | 装饰者模式                                                   | 适配器模式                                                   |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 形式 | 是一种非常特别的适配器模式                                   | 没有层级关系，装饰者模式有层级关系                           |
| 定义 | 装饰者和被装饰者实现同一个接口，主要目的是扩展之后依旧保留OOP关系 | 适配器和被适配者没有必然的关系，通常采用继承或代理的形式进行包装 |
| 关系 | 满足is-a的关系                                               | 满足has-a的关系                                              |
| 功能 | 注重覆盖，扩展                                               | 注重兼容，转换                                               |
| 设计 | 前置考虑                                                     | 后置考虑                                                     |

is-a：这种事物(绵羊)是那种事物(羊)中的一个种类。

has-a：这种事物(羊毛)隶属于那种事物(绵羊)，是它的一个部分、部件。

- 装饰者模式的优缺点

优点：

1. 装饰者模式是继承的有力补充，且比继承灵活，可以在不改变原有对象的情况下动态地给一个对象扩展功能，即插即用。
2. 使用不同的装饰类和这些类的排列组合，可以实现不同效果。
3. 装饰者模式完全符合开闭原则。

缺点：

1. 会出现更多的代码，更多的类，增加程序的复杂性。
2. 动态装饰时，多层装饰会更复杂。



#### 观察者模式

- 场景

定义了对象之间的一对多依赖，让多个观察者对象同时监听一个主体对象，当主体对象发生变化时，他的所有依赖者（观察者）都会收到通知并更新，属于行为型模式。观察者模式有时也叫发布订阅模式。观察者模式主要用于在关联行为之间建立一套触发机制的场景。

- 观察者模式的优缺点

优点：

1. 在观察者和被观察者之间建立了一个抽象的耦合。
2. 观察者模式支持广播通信。

缺点：

1. 观察者之间有过多的细节依赖，时间消耗多，程序的复杂性更高。
2. 使用不当会出现循环调用。



### 各设计模式的总结与对比

23种设计模式汇总表：

![](https://s2.loli.net/2022/02/15/kLGxXVCOzNf8pME.png)

各设计模式关联关系：

![](https://s2.loli.net/2022/02/15/ypMj6m7xkKcTwEI.png)

1. 单例模式和工厂模式

   在实际业务代码中，通常会把工厂类设计为单例模式。

2. 策略模式和工厂模式

   工厂模式的主要目的是封装好创建逻辑，策略模式接收工厂创建好的对象，从而实现不同的行为。

3. 策略模式和委派模式

   策略模式是委派模式内部的一种实现形式，策略模式关注结果是否能相互替代。

   委派模式更关注分发和调度过程。

4. 模板方法模式和工厂方法模式

   工厂方法模式是模板方法模式的一种特殊实现。

5. 模板方法模式和策略模式

   模板方法模式和策略模式都有封装算法。

   策略模式使不同算法可以相互替换，且不影响客户端应用层的使用。

   模板方法模式针对定义一个算法的流程，将一些有细微差异的部分交于子类实现。

   模板方法模式不能改变算法流程，策略模式可以改变算法流程且可替换。策略模式通常用来代替if..else等条件分支语句。

6. 装饰者模式和代理模式

   装饰者模式的关注点在于给对象动态添加方法，而代理模式更加关注控制对象的访问。

   代理模式通常会在代理类中创建被代理对象的实例，而装饰者模式通常会把被装饰者作为构造参数。

   装饰者和代理者虽然都持有对方的引用，但处理重心不一样的。

7. 装饰者模式和适配器模式

   装饰者模式可以实现与被装饰者相同的接口，或者继承被装饰者作为他的子类，而适配器和被适配者可以实现不同的接口。

8. 适配器模式和静态代理模式

   适配器模式可以结合静态代理来实现，保存被适配对象而引用，但不是唯一的实现方式。

9. 适配器模式和策略模式

   在业务比较复杂的情况下，可利用策略模式来优化适配器模式。



- spring中常用的设计模式

![](https://s2.loli.net/2022/02/15/IlW3ShjoPerJkKE.png)

spring中常用的编程思想汇总

![](https://s2.loli.net/2022/02/15/5i3RENoydaUFusn.png)

## spring5的系统架构

![](https://s2.loli.net/2022/02/15/lIEKRoS82rHzdvX.png)

### 核心容器

> 核心容器由 spring-beans、spring-core、spring-context和spring-expression 4个模块组成。

spring-beans和spring-core模块是spring框架的核心模块，包含了控制反转（Inversion of Control，IOC）和依赖注入（Dependency Injection，DI）。beanFactory使用控制反转反转对应程序的配置和依赖性规范与实际的应用程序代码进行了分离。但beanFactory实例化后并不会自动实例化Bean，只有当bean被使用时，BeanFactory才会对该Bean进行实例化与依赖关系的装配。

- spring-context模块架构于核心模块之上，扩展了BeanFactory，为它添加了Bean生命周期控制、架构事件体系及资源加载透明化等功能。此外，该模块还提供了许多企业级支持，如邮件访问、远程访问、任务调度等，ApplicationContext是该模块的核心接口，它的超类是BeanFactory、与BeanFactory不同，ApplicationContext实例化后会自动对所有的单实例Bean进行实例化与依赖关系的装配，使之处于待用状态。

- Spring-context-support模块是对Spring IoC容器及IoC子容器的扩展支持。
- spring-context-indeexer模块是Spring的类管理组件和Classpath扫描组件。
- spring-expression模块是统一表达式语言（EL）的扩展模块，可以查询、管理运行中的对象，同时也可以方便的调用对象方法，以及操作数组、集合等。它的语法类似于传统EL，但提供了额外功能，最出色的要数函数调用和简单字符串的模块函数。EL的特性是基于Spring产品的需求而设计的，可以非常方便地同Spirng IoC进行交互。



### AOP和设备支持

> AOP和设备支持由spring-aop、spring-aspects和spring-instrument 3个模块组成。

- spring-aop是Spring的另一个核心模块，是AOP主要的实现模块。作为继OOP后对程序员影响最大的编程思想之一，AOP极大地扩展了人们的编程思路。Spring以JVM的动态代理技术为基础，设计出了一系列的AOP横切实现，比如前置通知、返回通知、异常通知等。同时，Pointcut接口可以匹配切入点，可以使用现有的切入点来设计横切面，也可以扩展相关方法根据需求进行切入。
- spring-aspects模块集成自AspectJ框架，主语是为Spring提供了多种AOP实现方法。
- spring-instrument模块是基于Java SE中的java.lang.instrument进行设计的，应该算AOP的一个支援模块，主要是在JVM启动时生成一个代理类，程序员通过代理类在运行时修改类的字节，从而改变一个类的功能，实现AOP。



### 数据访问和集成

> 数据访问与集成由spring-jdbc、spring-tx、spring-orm、spring-oxm和spring-jms5个模块组成。

- spring-jdbc模块是Spring提供的JDBC抽象框架的主要实现模块，用于简化Spring JDBC操作。主要提供JDBC模块方式、关系数据库对象化方式、SimpleJdbc方式、事务管理来简化JDBC编程，主要实现类有JDBC-Template、SimpleJdbcTemplate及NamedParameterJdbcTemplate。
- spring-tx模块是Spring JDBC事务控制实现模块。Spring对事务做了很好的封装，通过它的AOP配置，可以灵活的在任何一层配置。但是在很多需求和应用中，直接使用JDBC事务控制还是有优势的。事务是以业务逻辑为基础的，一个完整的业务应该对应业务层里的一个方法，如果业务操作失败，则整个事务回滚，所以事务控制是应该放在业务层的。持久层的设计应该遵循一个很重要的原则：保证操作的原子性，及持久层里的每个方法都应该是不可分割的。在使用Spring JDBC控制事务时，应该注意其特殊性。
- spring-orm模块是ORM框架支持模块，主要集成Hibernate，Java Persistence API（JPA）和Java Data Objects（JDO）用于资源管理、数据访问对象（DAO）的实现和事务策略。
- spring-oxm模块主要提供一个抽象层以支撑OXM（OXM是Object-to-XML-Mapping的缩写，它是一个O/M-mapper，将Java对象映射成XML数据，或者将XML数据映射成Java对象），例如JAXB、Castor、XMLBeans、JiBX和XStream等。
- spring-jms模块能够发送和接收信息，自Spring 4.1开始，它还提供了对spring-messageing模块的支撑。

### Web组件

> Web组件由spring-web、spring-webmvc、spring-websocket和spring-webflux 4个模块组成。

- spring-web模块为Spring提供了最基础的Web支持，主要建立在核心容器之上，通过Servlet或者Listeners来初始化IoC容器，也包含一些与Web相关的支持。

- 众所周知，spring-webmvc模块是一个Web-Servlet模块，实现了spring MVC的Web应用。
- spring-websocket模块是与Web前端进行全双工通信的协议。
- spring-webflux是一个新的非阻塞函数式Reactive Web框架，可以用来建立异步的、非阻塞的、事件驱动的服务，并且扩展性非常好。



### 通信报文

> 通信报文即spring-messaging模块，它是Spring 4新加入的一个模块，主要职责是为Spring框架集成一些基础的报文传送应用。

### 集成测试

> 集成测试即spring-test模块，主要为测试提供支持，使得在不需要将程序发布到应用服务器或者连接到其他设施的情况下能够进行一些集成测试或者其他测试，这对于任何企业都是非常重要的。

### 集成兼容

集成兼容即spring-framework-bom模块，主要解决Spring的不同模块依赖版本不同的问题。

### 各模块之间的依赖关系

![](https://s2.loli.net/2022/02/16/8tmTVPSNlyqJdpr.png)



## Spring核心原理

### Spring核心容器类图

- BeanFactory

BeanFactory不关心对象是怎么定义和加载的，只关心能从工厂中得到什么样的对象。



![](https://s2.loli.net/2022/02/25/q4buDHlIQ2wz8Ax.png)

ApplicationContext是Spring提供的一个高级的IoC容器，它除了能够提供IoC容器的基本功能，还为用户提供了以下附加服务。

1. 支持信息源，可以实现国际化（实现MessageSource接口）。
2. 访问资源（实现ResourcePatternResolver接口）。
3. 支持应用事件（实现ApplicationEventPublisher接口）。



- BeanDefinition

Bean对象在Spring实现中是以BeanDefinition来描述的。

![](https://s2.loli.net/2022/02/25/nPSYcv1r3zGIUJL.png)

- BeanDefinitionReader

Bean的解析主要就是对Spring配置文件的解析。这个解析过程主要通过BeanDefinitionReader来完成。

![](https://s2.loli.net/2022/02/25/sMUXuajJg6zfe1I.png)
