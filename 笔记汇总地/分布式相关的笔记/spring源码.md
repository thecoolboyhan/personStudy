> 所有交给spring管理的对象默认都是单例的，new对象是一个非常消耗性能的操作。
在这些spring管理的类中，最好不要有状态属性，如果有的话一定要非常小心的使用，由于他们都是单例的，所以线程不安全，

# 动态代理

- spring 的动态代理是由ASM来实现的

## JDK动态代理

> jdk动态代理使用的是java的反射机制来实现的，jdk的动态代理需要被代理的类实现一个接口，然后继承接口。

```java
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

```java
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

---

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

#### 简单工厂

不属于GoF的23种设计模式，简单工厂模式适用于工厂类负责创建的对象较少的场景，且客户端只需要传入工厂类的参数，对于如何创建对象不需要关心。

- 缺点

工厂类职责相对过重，不易于扩展过于复杂的产品结构。

#### 工厂方法模式

定义一个创建对象的接口，但让实现这个接口的类来决定实例化哪个类，工厂方法模式让类的实例化推迟到了子类中进行。在工厂方法模式中用户只需要关心所需产品对应的工厂，无需关心创建细节，而且加入新的产品时符合开闭原则。

- 缺点
1. 类的个数容易过多，增加复杂度。
2. 增加了系统的抽象性，和理解难度。

#### 抽象工厂模式

提供一个创建一系列相关或相互依赖对象的接口，无需指定他们的具体类。客户端（应用层）不依赖产品类如何被创建，如何被实现等细节，强调的是一系列相关的产品对象，一起使用创建对象需要大量重复的代码。需要提供一个产品类的库，所有产品以同样的接口出现，从而使客户端不依赖具体实现。

- 缺点
1. 规定了所有可能被创建的产品集合，产品族中扩展新的产品困难，需要修改抽象工厂的接口。
2. 增加了系统的抽象性和理解难度。

#### 单例模式

> 确保一个类在任何情况下都绝对只有一个实例，并提供一个全局访问点。单例模式时创建型模式。

保证内存里只有一个实例，减少了内存的开销，还可以避免对资源的过重占用。

#### 饿汉式单例模式

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

#### 懒汉式单例模式

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

#### 注册式单例模式

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

#### 浅克隆

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

#### 深克隆

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

#### 动态代理

#### JDK动态代理

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

#### 自定义JDK动态代理讲解

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

```java
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

#### CGLib动态代理

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

#### 对比

1. JDK动态代理实现了被代理对象的接口，GCLib代理继承了被代理对象。
2. JDk动态代理和CGLib代理都在运行期生成字节码，JDK动态代理直接写class字节码，CGLib代理使用ASM框架写Class字节码，CGLib代理实现更复杂，生成代理类比JDK动态代理效率低。
3. JDK动态代理调用代理方法是通过反射机制调用的，CGLib代理是通过FastClass机制直接调用方法的，CGLib代理的执行效率更高。
4. 
#### spring动态代理

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

||装饰者模式|适配器模式|
|-|-|-|
|形式|是一种非常特别的适配器模式|没有层级关系，装饰者模式有层级关系|
|定义|装饰者和被装饰者实现同一个接口，主要目的是扩展之后依旧保留OOP关系|适配器和被适配者没有必然的关系，通常采用继承或代理的形式进行包装|
|关系|满足is-a的关系|满足has-a的关系|
|功能|注重覆盖，扩展|注重兼容，转换|
|设计|前置考虑|后置考虑|


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

- SpringMVC的九大组件

> 由DispatcherServlet的initStrategies方法初始化

```java
//多文件上传系统
initMultipartResolver(context);
//初始化本地语言环境
initLocaleResolver(context);
//初始化模板处理器
initThemeResolver(context);
//初始化handlerMapping
initHandlerMappings(context);
//初始化参数适配器
initHandlerAdapters(context);
//初始化异常拦截器
initHandlerExceptionResolvers(context);
//初始化视图预处理器
initRequestToViewNameTranslator(context);
//初始化视图转换器
initViewResolvers(context);
//初始化Flashmap管理器
initFlashMapManager(context);
```

### 基于XML的IOC容器的初始化

> ApplicationContext允许上下文嵌套，通过保持父上下文可以维护一个上下文体系。对于Bean的查找可以在这个上下文体系中进行，首先检查当前上下文，其次检查父上下文，逐级向上，这样可以为不同的Spring应用提速一个共享的Bean定义环境。

- 获取配置路径

在创建ClassPathXmlApplicationContext容器时，构造方法主要进行两个工作：
首先，调用父容器的构造方法super(parent)为容器设置好Bean资源加载器。

然后，调用AbstractRefreshableConfigApplicationContext的setConfigLocations(configLocations)方法设置Bean配置信息的定位路径。

- 开始启动

Spring IoC容器对Bean配置资源的载入是从refresh()方法开始的，refresh方法是一个模板方法，规定了IoC容器的启动流程，有些逻辑要交给其子类实现。

IoC容器载入Bean配置信息从其子类容器的refreshBeanFactory方法启动。载入都是通过

```java
//告诉子类刷新内部bean工厂
// Tell the subclass to refresh the internal bean factory.
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
```

refresh()方法的主要作用是： 在创建IoC容器前，如果已经有容器存在，需要把已有的容器销毁和关闭，以保证在refresh方法之后使用的是新创建的IoC容器。它类似于对IoC容器的重启，在新创建的容器中对容器进行初始化，对Bean配置资源进行载入。

- 创建容器

AbstractApplicationContext类中只抽象定义了refreshBeanFactory方法，容器真正调用的是其子类AbstractRefreshableApplicationContext实现的refreshBeanFactory方法。

```java
/**
此实现执行此context的底层 bean 工厂的实际刷新，关闭先前的 bean 工厂（如果有）并为上下文生命周期的下一阶段初始化一个新的 bean 工厂。
**/
@Override
protected final void refreshBeanFactory() throws BeansException {
   //如果已经有容器，摧毁容器中的Bean，关闭容器
   if (hasBeanFactory()) {
      destroyBeans();
      closeBeanFactory();
   }
   try {
      //创建IoC容器
      DefaultListableBeanFactory beanFactory = createBeanFactory();
      beanFactory.setSerializationId(getId());
      //对IoC容器进行定制化，如设置启动参数，开启注解的自动装配等
      customizeBeanFactory(beanFactory);
      //调用再度Bean定义的方法，这里又使用了一个委派模式
      //在在当前类中只定义了抽象的loadBeanDefinitions()方法，调用子类容器实现
      loadBeanDefinitions(beanFactory);
      synchronized (this.beanFactoryMonitor) {
         this.beanFactory = beanFactory;
      }
   }
   catch (IOException ex) {
      throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
   }
}
```

> 先判断beanFactory是否存在，如果存在则先销毁Bean并关闭beanFactory，接着创建DefaultListableBeanFactory，并调用loadBeanDefinitions方法装载Bean定义。

- 载入配置路径

在AbstractRefreshableApplicationContext中只定义了抽象父类的loadBeanDefinitions方法，容器真正调用的是其子类AbstractXmlApplicationContext的该方法。

```java
@Override
protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
   // Create a new XmlBeanDefinitionReader for the given BeanFactory.
   //创建XmlBeanDefinitionReader，创建Bean读取器
   //并通过回调设置到容器中，容器使用该读取器读取Bean配置资源
   XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

   // Configure the bean definition reader with this context's
   // resource loading environment.
   //为Bean读取器设置Spring资源加载器
   //AbstractXmlApplicationContext的祖先父类AbstractApplicationContext，继承DefaultResourceLoader
   //因此容器本身也是一个资源加载器
   beanDefinitionReader.setEnvironment(this.getEnvironment());
   beanDefinitionReader.setResourceLoader(this);
   //为Bean读取器设置SAX xml解析器
   beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

   // Allow a subclass to provide custom initialization of the reader,
   // then proceed with actually loading the bean definitions.
   //当Bean读读取器读取Bean定义的xml资源文件时，启用xml的校验机制
   initBeanDefinitionReader(beanDefinitionReader);
   //Bean读取器真正实现加载的方法
   loadBeanDefinitions(beanDefinitionReader);
}
```

> xml Bean读取器的一种策略XmlBeanDefinitionReader为例，XmlBeanDefinitionReader调用其父类AbstractBeanDefinitionReader的loadBeanDefinitions方法读取Bean配置资源。

- 分配路径处理策略

AbstractBeanDefinitionReader的loadBeanDefinitions方法：

1. 调用资源加载器的获取资源方法resourceLoader.getResources(location)，获取要加载的资源；
2. 真正执行加载功能，由其子类XmlBeanDefinitionReader的loadBeanDefinitions方法完成。
- 解析配置文件路径

loadBeanDefinitions方法调用了AbstractApplicationContext的gettResources方法。

![](https://s2.loli.net/2022/03/01/Pc389VvdE2zyFXH.png)

DefaultResourceLoader提供了getResourceByPath()方法的实现，就是为了处理不是ClassPath标识又不是URL标识的Resource定位的情况。

> Spring提供了各种资源抽象，如ClassPathResource，UrlResource，FileSystemResource等。

- 开始读取配置内容

利用I/O流把xml读取到内存，然后转换成DOM文档对象，这个过程由documentLoader方法实现。

```java
//准备文档对象
//将XMl文件转换成DOM对象，解析过程由documentLoader方法实现
Document doc = doLoadDocument(inputSource, resource);
//分配解析策略
//启动对Bean定义的解析
return registerBeanDefinitions(doc, resource);
```
- 准备文档对象

IoC容器根据定位的Bean配置信息，利用两次工厂模式，将其读入并转换 成为文档对象。

- 分配解析策略

```java
//注册包含在给定 DOM 文档中的 bean 定义
//按照Spring的Bean语义要求将Bean配置信息解析并转换为容器内部数据结构
public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
   //得到BeanDefinitionDocumentReader来对XML格式的BeanDefinition进行解析
   BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
   //获取容器中注册的Bean数量
   int countBefore = getRegistry().getBeanDefinitionCount();
   //解析过程的入口，这里使用了委派模式，BeanDefinitionDocumentReader只是一个接口
   //具体的解析过程由实现类BeanDefinitionDocumentReader完成
   documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
   //统计解析的Bean数量
   return getRegistry().getBeanDefinitionCount() - countBefore;
}
```
- 将配置载入内存

BeanDefinitionDocumentReader接口通过registerBeanDefinitions（）方法调用其实现类DefaultBeanDefinitionDocumentReader对文档进行解析。

```java
//根据Spring DTD 或者 XSD的自定以规则解析Bean定义的文档对象
public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
   //获得XML描述符
   this.readerContext = readerContext;
   logger.debug("Loading bean definitions");
   //获取Document的根信息
   Element root = doc.getDocumentElement();
   doRegisterBeanDefinitions(root);
}

protected void doRegisterBeanDefinitions(Element root) {
    //具体的解析过程由BeanDefinitionParserDelegate实现
    //BeanDefinitionParserDelegate中定义了Spring Bean定义XML文件的各种元素
    BeanDefinitionParserDelegate parent = this.delegate;
    this.delegate = createDelegate(getReaderContext(), root, parent);

    if (this.delegate.isDefaultNamespace(root)) {
      String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
      if (StringUtils.hasText(profileSpec)) {
        String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
            profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
        if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
          if (logger.isInfoEnabled()) {
            logger.info("Skipped XML bean definition file due to specified profiles [" + profileSpec +
                "] not matching: " + getReaderContext().getResource());
          }
          return;
        }
      }
    }

    //在解析Bean定义之前，进行自定义解析，增强解析过程的可扩拽性
    preProcessXml(root);
    //从文档的根元素开始进行Bean定义的文档对象的解析
    parseBeanDefinitions(root, this.delegate);
    //在解析Bean定义之后，进行自定义解析，增加解析过程的可扩展性
    postProcessXml(root);

    this.delegate = parent;
}


```

在Spring配置文件中可以使用<import>元素来导入IoC容器所需要的其他元素，Spring IoC容器在解析时首先将指定的资源加载到容器中。使用<alias>别名时，Spring IoC容器首先将别名元素所定义的别名注册到容器中。

对于既不是<import>元素又不是<alias>元素的元素，即Spring配置文件中普通的<bean>元素，由BeanDefinitionParserDelegate类的parseBeanDefinitionElement（）方法实现解析。

- 载入<bean>元素

Bean配置信息中的<import>和<alias>元素解析在DefaultBeanDefinitionDocumentReader中已经完成，Bean配置信息中使用最多的<bean>元素交由BeanDefinitionParserDelegate来解析。

在解析<bean>元素的过程中没有创建和实例化Bean对象，只是创建了Bean对象的定义类BeanDefinition，将<bean> 元素中的配置信息设置到BeanDefinition中作为记录，当依赖注入时才使用这些记录信息创建和实例化具体的Bean对象。

- 载入<property>元素

BeanDefinitionParserDelegate调用parsePropertyElements方法解析property元素。

1. ref被封装为指向依赖对象的一个引用。
2. value被封装成一个字符串类型的对象
3. ref和value都通过解析的数据类型属性值.setSource(extractSource(ele));方法将属性值与所引用的属性关联起来。
- 载入<property>子元素

BeanDefinitionParserDelegate类中的parsePropertySubElement方法解析。

在Spring配置文件中，对property元素中配置的array，list，set，map，props等集合子元素都通过parsePropertySubElement方法解析，生成对应的数据对象。比如ManagedList，ManagedArray，ManagedSet等。这些Managed类时Spring对象BeanDefinition的数据封装，对集合元素数据类型的解析由各自的解析方法实现。

- 载入<list>子元素

BeanDefinitionParserDelegate类中的parseListElement方法用于解析<property>元素中的list集合子元素。

```java
public List<Object> parseListElement(Element collectionEle, @Nullable BeanDefinition bd) {
   //获取元素list元素中的value-Type属性，即获取集合元素的数据类型
   String defaultElementType = collectionEle.getAttribute(VALUE_TYPE_ATTRIBUTE);
   //获取list元素中所有子节点
   NodeList nl = collectionEle.getChildNodes();
   //Spring将List封装为ManagedList
   ManagedList<Object> target = new ManagedList<>(nl.getLength());
   target.setSource(extractSource(collectionEle));
   //设置集合目标的数据类型
   target.setElementTypeName(defaultElementType);
   target.setMergeEnabled(parseMergeAttribute(collectionEle));
   //具体list元素的解析
   parseCollectionElements(nl, target, bd, defaultElementType);
   return target;
}
```

通过Spring IoC容器对Bean配置信息的解析，Spring IoC容器大致完成了管理Bean对象的准备工作，即初始化过程。但最重要的依赖注入还没有发生，在Spring IoC容器中BeanDefinition存储的还只是一些静态信息，接下来需要向容器注册Bean定义信息。

- 分配注册策略

DefaultBeanDefinitionDocumentReader对Bean定义转换的文档对象解析的流程中，在parseBeanDefinitionElement（）方法中完成对文档对象的解析后得到封装BeanDefinition的BeanDefinitionHold对象，然后调用BeanDefinitionReaderUtils的registerBeanDefinition（）方法向Spring IoC容器注册解析的Bean。

```java
public static void registerBeanDefinition(
      BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
      throws BeanDefinitionStoreException {
   //获取解析的BeanDefinition的名称
   String beanName = definitionHolder.getBeanName();
   //向SpringIoC容器注册BeanDefinition
   registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
   //如果BeanDefinition由别名，向IoC注册别名
   String[] aliases = definitionHolder.getAliases();
   if (aliases != null) {
      for (String alias : aliases) {
         registry.registerAlias(beanName, alias);
      }
   }
}
```

真正完成注册功能的是DefaultListableBeanFactory。

- 向容器注册

DefaultListableBeanFactory中使用一个ConcurrentHashMap的集合对象存放IoC容器中注册解析的BeanDefinition对象。

![](https://s2.loli.net/2022/03/02/GOUjWyvMxizBl4f.png)

```java
//注册的过程中需要线程同步，以保证数据的一致性
synchronized (this.beanDefinitionMap) {
   this.beanDefinitionMap.put(beanName, beanDefinition);
   List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
   updatedDefinitions.addAll(this.beanDefinitionNames);
   updatedDefinitions.add(beanName);
   this.beanDefinitionNames = updatedDefinitions;
   if (this.manualSingletonNames.contains(beanName)) {
      Set<String> updatedSingletons = new LinkedHashSet<>(this.manualSingletonNames);
      updatedSingletons.remove(beanName);
      this.manualSingletonNames = updatedSingletons;
   }
}
```

### 基于注解的IoC初始化

```java
//为容器注册一个被处理的注解Bean，新注册的Bean，必须手动调用容器的refresh()方法刷新容器，触发容器对新注册的Bean的处理
public void register(Class<?>... annotatedClasses) {
   Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
   this.reader.register(annotatedClasses);
}

```

> 必须手动调用refresh方法，不然注册不生效

Spring对注解的处理分为两种方式：

1. 直接将注解Bean注册到容器中：可以在初始化容器时注册；也可以在容器创建之后手动调用注册方法向容器注册，然后通过手动刷新容器使容器对注册的注解Bean进行处理。
2. 通过扫描指定的包及其子包下的所有类处理：在初始化注解容器时指定要自动扫描的路径，如果容器创建以后向给定路径动态添加了注解Bean，则需要手动调用容器扫描的方法手动刷新容器，时容器对所注册的主频Bean进行处理。
- 读取注解的元数据

```java
<T> void doRegisterBean(Class<T> annotatedClass, @Nullable Supplier<T> instanceSupplier, @Nullable String name,
      @Nullable Class<? extends Annotation>[] qualifiers, BeanDefinitionCustomizer... definitionCustomizers) {

   //根据指定的注解Bean定义类，创建Spring容器中对注解Bean的封装的数据结构
   AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(annotatedClass);
   if (this.conditionEvaluator.shouldSkip(abd.getMetadata())) {
      return;
   }

   abd.setInstanceSupplier(instanceSupplier);
   //解析Bean定义的作用域，若@Scope("prototype")，则Bean为原型模式
   ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
   //为注解Bean定义设置作用域
   abd.setScope(scopeMetadata.getScopeName());
   //定义名称
   String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry));

   AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
   //主要配置autowiring自动依赖注入装配的限定条件，即@Qualifier注解
   //Spring默认按类型装配，如果使用@Qualifier则按名称装配
   if (qualifiers != null) {
      for (Class<? extends Annotation> qualifier : qualifiers) {
         //如果配置了@Primary注解，设置该Bean为autowiring自动依赖注入的首选
         if (Primary.class == qualifier) {
            abd.setPrimary(true);
         }
         //如果设置了@Lazy注解，则设置该Bean为非延迟初始化，如果没有配置，则该Bean为预实例化
         else if (Lazy.class == qualifier) {
            abd.setLazyInit(true);
         }
         else {
            abd.addQualifier(new AutowireCandidateQualifier(qualifier));
         }
      }
   }
   for (BeanDefinitionCustomizer customizer : definitionCustomizers) {
      customizer.customize(abd);
   }

   //创建一个指定Bean名称的Bean定义对象，封装注解Bean定义类型
   BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
   //根据注解中配置的作用域，创建相应的代理对象
   definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
   //向IoC容器注册注解Bean类定义对象
   BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
}
```

注册注解Bean定义类的基本步骤如下：

1. 使用注解元数据解析器，解析注解Bean中关于作用域的配置。
2. 使用AnnotationConfigUtils的processCommonDefinitionAnnotations()方法处理注解Bean定义类中通用的注解。
3. 使用AnnotationConfigUtils的applyScopedProxyMode（）方法创建作用域的代理对象。
4. 通过BeanDefinitionReaderUtils向容器注册Bean。

### IoC容器初始化小结

1. 初始化的入口由容器实现中的refresh（）方法调用来完成。
2. 对Bean定义载入IoC容器使用的方法是loadBeanDefinitions（）；
- 大致过程

通过ResourceLoader来完成资源文件的定位，DefaultResourceLoader是默认的实现，同时上下文本身就给出了ResourceLoader的实现，可以通过类路径、文件系统、URL等方式来定位资源。如果是XmlBeanFactory作为IoC容器，那么通过为它指定Bean定义的资源，也就是说Bean定义文件时通过抽象成Resource来被IoC容器处理，容器通过BeanDefinitionReader来完成定义信息的解析和Bean信息的注册，往往使用XmlBeanDefinitionReader来解析Bean的XML定义文件--实际的处理过程是委托给BeanDefinitionParserDelegate来完成的，从而得到Bean的定义信息，这些信息在Spring中使用BeanDefinition来表示---这个名字可以让我们想到loadBeanDefinition（）、registerBeanDefinition()这些相关方法。容器解析得到BeanDefinition以后，需要在IoC容器中注册，这由IoC实现BeanDefinitionRegistry接口来实现。注册过程就是在IoC容器内部维护的一个HashMap来保存得到的BeanDefinition的过程。这个HashMap是IoC容器持有Bean信息的场所，以后对Bean的操作都是围绕这个HashMap来实现的。

### Spring DI

#### 依赖注入

- 依赖注入发生的时间

当IoC容器完成了Bean定义资源的定位、载入和解析注册，IoC容器就可以管理Bean定义的相关数据了，但此时IoC还没有对Bean进行依赖注入，依赖注入在以下两种情况下发生：

1. 用户第一次调用getBean（）方法时，IoC容器触发依赖注入
2. 当用户在配置文件中将<Bean>元素配置了 lazy-init=fasle属性时，即让容器在解析注册Bean定义时进行预实例化，触发依赖注入。

在Spring中如果Bean定义为单例模式的，则容器在创建之前先从缓存中查找，以确保缓存中只存在一个实例对象。如果Bean定义为原型模式的，则容器每次都会创建一个新的实例对象。Bean定义还可以指定器生命周期范围。

具体的Bean实例对象的创建过程由实现了ObjectFactory接口的匿名内部类的createBean方法完成，ObjectFactory接口使用委派模式。具体的Bean实例创建过程交由器实现类AbstractAutowireCapableBeanFactory完成。

- 开始实例化

具体依赖注入实现其实就在以下两个方法：

1. createBeanInstance()方法，生成Bean所包含的Java对象实例。
2. populateBean（），对Bean属性的依赖注入进行处理。
- 选择Bean实例化策略

在createBeanInstance（）方法中，根据指定的初始化策略，使用简单工厂、工厂方法或者容器的自动装配特性生成java实例对象。

对使用工厂方法和自动装配特性的Bean，调用相应的工厂方法或者参数匹配的构造方法即可完成实例化对象的工作，但是最常使用的默认无参构造方法就是需要使用相应的初始化策略（JDK的反射机制或者CGLib）来进行实例化，在getInstantiationStrategy().instantiate(mbd, beanName, parent);中实现了实例化。

- 执行Bean的实例化

如果Bean的方法被覆盖了，则使用CGLib进行实例化，否则使用JDK的反射机制进行实例化。

CGLib是一个常用的字节码生成器的类库，他提供了一系列API实现Java字节码的生成和转换功能，JDK的动态代理只能针对接口，如果一个类没有实现任何接口，要对器进行动态代理只能使用CGLib。

- 准备依赖注入

属性的注入过程分以下两种情况：

1. 属性值类型不需要强制转换时，不需要解析属性值，直接进行依赖注入。
2. 属性值类型需要进行强制转换时，如对其他对象的引用等，首先需要解析属性值，然后对解析后的属性值进行依赖注入。

> 对属性的解析是在BeanDefinitionValueResolver类的resolveValueIfNecessary（）方法中进行的，对属性值的依赖注入是通过bw.setPropertyValues(new MutablePropertyValues(deepCopy))方法实现的。

- 解析属性依赖注入规则

容器对属性进行依赖注入时，如果发现属性值需要进行类型转换，例如属性值是容器中另一个Bean实例化的对象，则容器首先需要根据属性值解析出所引用的对象， 然后才能将该引用对象注入到目标实例对象的属性上。

- 注入赋值
1. 对于集合类型的属性，将属性解析为目标类型的集合后直接赋值给属性。
2. 对于非集合类型的属性，大量使用JDK反射机制，通过属性的getter（）方法获取指定属性注入前的值，同时调用属性的setter（）方法为属性设置注入后的值。

#### Spring IoC容器中那些鲜为人知的细节

- 关于延时加载

IoC容器的初始化过程就是对Bean定义资源的定位、载入和注册，此时容器对Bean的依赖注入并没有发生，依赖注入是在应用程序第一次向容器索取Bean时通过getBean（）方法完成的。

当Bean定义资源的<Bean>元素中配置了lazy-init=false属性时，容器将会在初始化时对所配置的Bean进行预实例化，Bean的依赖注入在容器初始化时就已经完成。改善了程序第一次向容器获取Bean的性能。

- 关于FactoryBean和BeanFactory

BeanFactory： Spring IoC容器的最高层接口就是BeanFactory，他的作用时管理Bean，即实例化、定位、配置应用程序中的对象及建立这些对象之间的依赖。

FactoryBean： 产生其他Bean实例。

- 再述autowiring

IoC容器提供了两种管理Bean依赖关系的方式：

1. 显性管理：通过BeanDefinition的属性值和构造方法实现Bean依赖关系管理
2. autowiring：IoC容器有依赖自动装配功能，不需要对Bean属性的依赖关系做显性的声明，只需要配置好autowiring属性，IoC容器会自动使用反射查找属性的类型和名称，然后基于属性的类型或名称自动匹配容器中的Bean，从而自动完成依赖注入。

autowiring实现过程如下：

1. 对Bean的属性调用getBean（）方法，完成依赖Bean的初始化和依赖注入。
2. 将依赖Bean的属性引用设置到被依赖的Bean属性上。
3. 将依赖Bean的名称和被依赖Bean的名称存储在IoC容器的集合中。

### Spring AOP

#### 源码

- 寻找入口

Spring AOP是由接入BeanPostProcessor后置处理器开始的，它是Spring IoC容器经常使用的一个特性，这个Bean后置处理器是一个监听器，可以监听容器触发的Bean声明周期事件。

BeanPostProcessor后置处理器的调用发生在Spring IoC容器完成Bean实例对象的创建和属性的依赖注入之后。

- 选择代理策略

先判断是否代理这个类，是否为基础建设类，判断是否配置了shouldSkip，最终调用的是proxyFactory.getPorxy（）方法。

- 完整流程

```text
       1）、传入配置类，创建ioc容器
          2）、注册配置类，调用refresh（）刷新容器；
          3）、registerBeanPostProcessors(beanFactory);注册bean的后置处理器来方便拦截bean的创建；
              1）、先获取ioc容器已经定义了的需要创建对象的所有BeanPostProcessor
              2）、给容器中加别的BeanPostProcessor
              3）、优先注册实现了PriorityOrdered接口的BeanPostProcessor；
              4）、再给容器中注册实现了Ordered接口的BeanPostProcessor；
              5）、注册没实现优先级接口的BeanPostProcessor；
              6）、注册BeanPostProcessor，实际上就是创建BeanPostProcessor对象，保存在容器中；
                  创建internalAutoProxyCreator的BeanPostProcessor【AnnotationAwareAspectJAutoProxyCreator】
                  1）、创建Bean的实例
                  2）、populateBean；给bean的各种属性赋值
                  3）、initializeBean：初始化bean；
                          1）、invokeAwareMethods()：处理Aware接口的方法回调
                          2）、applyBeanPostProcessorsBeforeInitialization()：应用后置处理器的postProcessBeforeInitialization（）
                          3）、invokeInitMethods()；执行自定义的初始化方法
                          4）、applyBeanPostProcessorsAfterInitialization()；执行后置处理器的postProcessAfterInitialization（）；
                  4）、BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)创建成功；--》aspectJAdvisorsBuilder
              7）、把BeanPostProcessor注册到BeanFactory中；
                  beanFactory.addBeanPostProcessor(postProcessor);
  =======以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程========
  
              AnnotationAwareAspectJAutoProxyCreator => InstantiationAwareBeanPostProcessor
          4）、finishBeanFactoryInitialization(beanFactory);完成BeanFactory初始化工作；创建剩下的单实例bean
              1）、遍历获取容器中所有的Bean，依次创建对象getBean(beanName);
                  getBean->doGetBean()->getSingleton()->
              2）、创建bean
                  【AnnotationAwareAspectJAutoProxyCreator在所有bean创建之前会有一个拦截，InstantiationAwareBeanPostProcessor，
　　　　　　　　　　　　会调用postProcessBeforeInstantiation()】
                  1）、先从缓存中获取当前bean，如果能获取到，说明bean是之前被创建过的，直接使用，否则再创建；
                      只要创建好的Bean都会被缓存起来
                  2）、createBean（）;创建bean；
                      AnnotationAwareAspectJAutoProxyCreator 会在任何bean创建之前先尝试返回bean的实例
                      【BeanPostProcessor是在Bean对象创建完成初始化前后调用的】
                      【InstantiationAwareBeanPostProcessor是在创建Bean实例之前先尝试用后置处理器返回对象的】
                      1）、resolveBeforeInstantiation(beanName, mbdToUse);解析BeforeInstantiation
                          希望后置处理器在此能返回一个代理对象；如果能返回代理对象就使用，如果不能就继续
                          1）、后置处理器先尝试返回对象；
                              bean = applyBeanPostProcessorsBeforeInstantiation（）：
                                  拿到所有后置处理器，如果是InstantiationAwareBeanPostProcessor;
                                  就执行postProcessBeforeInstantiation
                              if (bean != null) {
                                bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                            }
  
                      2）、doCreateBean(beanName, mbdToUse, args);真正的去创建一个bean实例；和3.6流程一样；
                      3）、
AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】    的作用：
 1）、每一个bean创建之前，调用postProcessBeforeInstantiation()；
          关心MathCalculator和LogAspect的创建
          1）、判断当前bean是否在advisedBeans中（保存了所有需要增强bean）
          2）、判断当前bean是否是基础类型的Advice、Pointcut、Advisor、AopInfrastructureBean，
              或者是否是切面（@Aspect）
          3）、是否需要跳过
              1）、获取候选的增强器（切面里面的通知方法）【List<Advisor> candidateAdvisors】
                  每一个封装的通知方法的增强器是 InstantiationModelAwarePointcutAdvisor；
                  判断每一个增强器是否是 AspectJPointcutAdvisor 类型的；返回true
              2）、永远返回false
 2）、创建对象
  postProcessAfterInitialization；
          return wrapIfNecessary(bean, beanName, cacheKey);//包装如果需要的情况下
          1）、获取当前bean的所有增强器（通知方法）  Object[]  specificInterceptors
             1、找到候选的所有的增强器（找哪些通知方法是需要切入当前bean方法的）
             2、获取到能在bean使用的增强器。
             3、给增强器排序
         2）、保存当前bean在advisedBeans中；
         3）、如果当前bean需要增强，创建当前bean的代理对象；
             1）、获取所有增强器（通知方法）
             2）、保存到proxyFactory
             3）、创建代理对象：Spring自动决定
                 JdkDynamicAopProxy(config);jdk动态代理；
                 ObjenesisCglibAopProxy(config);cglib的动态代理；
         4）、给容器中返回当前组件使用cglib增强了的代理对象；
         5）、以后容器中获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程；       
  3）、目标方法执行    ；         容器中保存了组件的代理对象（cglib增强后的对象），这个对象里面保存了详细信息（比如增强器，目标对象，xxx）；
         1）、CglibAopProxy.intercept();拦截目标方法的执行
         2）、根据ProxyFactory对象获取将要执行的目标方法拦截器链；
             List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
             1）、List<Object> interceptorList保存所有拦截器 5
                 一个默认的ExposeInvocationInterceptor 和 4个增强器；
             2）、遍历所有的增强器，将其转为Interceptor；
                 registry.getInterceptors(advisor);
             3）、将增强器转为List<MethodInterceptor>；
                 如果是MethodInterceptor，直接加入到集合中
                 如果不是，使用AdvisorAdapter将增强器转为MethodInterceptor；
                 转换完成返回MethodInterceptor数组；
3）、如果没有拦截器链，直接执行目标方法;
        拦截器链（每一个通知方法又被包装为方法拦截器，利用MethodInterceptor机制）
4）、如果有拦截器链，把需要执行的目标对象，目标方法，
        拦截器链等信息传入创建一个 CglibMethodInvocation 对象，
        并调用 Object retVal =  mi.proceed();
5）、拦截器链的触发过程;
        1)、如果没有拦截器执行执行目标方法，或者拦截器的索引和拦截器数组-1大小一样（指定到了最后一个拦截器）执行目标方法；
        2)、链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截器执行完成返回以后再来执行；
           拦截器链的机制，保证通知方法与目标方法的执行顺序；
```

### SpringMVC

![](https://s2.loli.net/2022/03/09/DqHvFoPcrNCX5fY.png)

1. dispatcherServlet是Spring MVC中的前端控制器，负责接收request并将request转发给对应的处理组件。
2. HanlerMapping是Spring MVC中完成URL到Controller映射的组件。dispatcherServlet接收Request，然后从HanlerMapping查找处理request的Controller。
3. controller处理Request，并返回ModelAndView对象，Controller是Spring MVC中负责处理Request的组件，ModelAndView是封装结果视图的组件。

容器初始化时会建立所有URL和Controller中方法的对应关系，保存到Handler Mapping中，用户请求时根据请求的URL快速定位到Controller中的某个方法。在Spring中现将URL和Controller的对应关系保存到Map<url,Controller>中。web容器启动时会通知Spring初始化容器（加载Bean的定义信息和初始化所有单例Bean），然后Spring MVC会遍历容器中的Bean，获取每个Controller中的所有方法访问的URL，将URL和Controller保存到一个Map中，这样就可以根据请求快速定位到Controller，因为最终处理请求的是Controller中的方法，Map中只保留URL和Controller的对应关系，所以要根据请求的URL近一步确认Controller中的方法。其原理就是拼接Controller的URL和方法的URL，与请求的URL进行匹配，找到匹配的方法。接下来的任务就是参数绑定，把请求中的参数绑定到方法的形参上，这是整个请求处理过程中最复杂的一步。

#### Spring MVC九大组件

- HandlerMapping

用来查找Handler的，也就是处理器，具体的表现形式可以是类，也可以是方法。比如标记了@RequestMapping的每个方法都可以看成一个Handler。Handler负责实际的请求处理，在请求到达后，HandlerMapping的作用便是找到请求对应的处理器Handler和Interceptor。

- Handler Adapter

适配器，因为Spring MV中Handler可以是任意形式的，只要能够处理请求便可。但把请求交给Servlet的时候，由于Servlet的方法j结构都是doService(HttpServletRequest request, HttpServletResponse response)形式的，要让固定的Servlet处理方法调用Handler来进行处理，这一步工作便是HandlerAdapter要做的。

- HandlerExceptionResolver

用来处理Handler产生异常的组件。此组件的作用是根据异常设置ModelAndView，之后交给渲染方法进行渲染，渲染方法会把ModelAndView渲染成页面。此组件只用于解析对请求做处理阶段产生的异常，渲染阶段的异常不归它管。

- ViewRessolver

视图解析器，通常在Spring MVC的配置文件中，都会配上一个实现类来进行视图解析。这个组件的主要作用是将Spring类型的视图名和Locale解析为View类型的视图，只有一个resolveViewName（）方法。Controller层返回的String类型的视图名ViewName最终会在这里被解析成View。View是用来渲染页面的，也就是说，它会将程序返回的参数和数据填入模板中，生成HTML文件，ViewResolver在这个过程中主要做两件大事：ViewResolver会找到渲染所用的模板和所用的技术并填入参数。默认情况下Spring MVC会为我们自动配置一个InternalResourceViewResolver,是针对JSP类型视图的。

- RequestToViewNameTranslator

从请求中获取ViewName。因为ViewResolver根据ViewName查找View，但尤的Handler处理完成之后，没有设置View，也没有设置ViewName，便要通过这个组件来从请求中查找ViewName。

- LocaleResolver

LocaleResolver组件的resolveViewName（）方法寻要两个参数：一个是视图名，另一个就是Locale。LocaleResolver用于从请求中解析出Locale，比如在中国Locale就是zh-CN，用来表示一个区域。这个组件是i18n的基础。

- ThemeResolver

用来解析主题的。主题就是样式、图片及它们所形成的显示效果的集合。Spring MVC中一套主题对应一个properties文件。

- MultipartResolver

用于处理上传请求，通过将普通的请求包装成MultipartHttpServletRequest来实现。让普通的请求拥有文件上传功能。

MultipartHttpServletRequest可以通过getFile（）方法直接获取文件。如果上传多个文件，调用getFileMap（）方法得到Map<Name,File>的结构。

- FlashMapManager
  用于重定向时的参数传递，比如在处理用户订单时，为了避免重复提交，可以处理完post请求后重定向到一个gett请求，这个gett请求可以用来显示订单详情之类的信息。因为重定向是没有传递参数这一功能的，如果不想把参数写进URL，那么可以通过FlashMap来传递。只需要在重定向之前将数据写入flashMap中，这样重定向之后的Handler中Spring就会自动将其设置到Model中，在显示订单信息的页面上就可以直接从Mpdel中获取数据。
    - FlashMap

FlashMapManager就是用来管理FlashMap的。

#### 源码

1. ApplicationContext初始化时用Map保存所有URL和Controller类的对应关系。
2. 根据请求URL找到对应的Controller，并从Conntroller中找到处理请求的方法。
3. 将Request参数绑定到方法的形参上，执行方法处理请求，并返回具体视图。
- 初始化阶段

主要先初始化IoC容器，最终会调用refresh（）方法。初始化之后又调用onRefresh（）方法：

```java
protected void initStrategies(ApplicationContext context) {
   //多文件上传的
   initMultipartResolver(context);
   //本地语言环境
   initLocaleResolver(context);
   //模板处理器
   initThemeResolver(context);
   //handlerMapping
   initHandlerMappings(context);
   //参数适配器
   initHandlerAdapters(context);
   //异常拦截器
   initHandlerExceptionResolvers(context);
   //视图预处理器
   initRequestToViewNameTranslator(context);
   //视图转换器
   initViewResolvers(context);
   //FlashMap管理器
   initFlashMapManager(context);
}
```

URL和Controller的关系，在HandlerMapping的子类中的initApplicationContext（）方法中：

```java
//建立当前ApplicationContext中的所有Controller和URL的对应关系
protected void detectHandlers() throws BeansException {
   ApplicationContext applicationContext = obtainApplicationContext();
   if (logger.isDebugEnabled()) {
      logger.debug("Looking for URL mappings in application context: " + applicationContext);
   }
   //获取applicationContext容器中所有Bean的名字
   String[] beanNames = (this.detectHandlersInAncestorContexts ?
         BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class) :
         applicationContext.getBeanNamesForType(Object.class));
   //遍历BeanNames并找到对应的URL
   for (String beanName : beanNames) {
      //查找Bean上的所有URL，该方法由对应的子类实现
      String[] urls = determineUrlsForHandler(beanName);
      if (!ObjectUtils.isEmpty(urls)) {
         //保存urls和beanName的对应关系，放入Map<urls,beanName>
         //该方法在父类AbstractUrlHandlerMapping中实现
         registerHandler(urls, beanName);
      }
      else {
         if (logger.isDebugEnabled()) {
            logger.debug("Rejected bean name '" + beanName + "': no URL paths identified");
         }
      }
   }
}

//获取Controller中所有的URL，由子类实现，典型的模板模式
protected abstract String[] determineUrlsForHandler(String beanName);
```

这里以BeanNameUrlHandlerMapping为例，来查找beanName上的URL：

```java
protected String[] determineUrlsForHandler(String beanName) {
   List<String> urls = new ArrayList<>();
   if (beanName.startsWith("/")) {
      urls.add(beanName);
   }
   String[] aliases = obtainApplicationContext().getAliases(beanName);
   for (String alias : aliases) {
      if (alias.startsWith("/")) {
         urls.add(alias);
      }
   }
   return StringUtils.toStringArray(urls);
}
```

- 运行调用阶段

运行调用是由请求触发的，入口为DispatcherServlet的核心方法doService（），diService的核心由doDispatch（）实现。

Spring MVC中提供两种从请求参数到方法中参数的绑定方式：

1. 通过注解进行绑定，@RequestParam
2. 通过参数名称进行绑定。

只要方法的参数前声明@RequestParam（”name“），就可以将请求中参数name的值绑定到反方法的行参上。

通过参数名称进行绑定的前提是必须获取方法中参数的名称，java反射只提供了获取方法参数类型的方法，并没有提供获取参数名称的方法。Spring MVC解决这个问题的方法是用asm框架读取字节码文件。

![](https://s2.loli.net/2022/03/14/sZVNAoLxaCWBtwd.png)

#### Spring MVC优化建议

1. Controller如果能保持单例模式，尽量使用单例模式

这样可以减小创建对象和回收对象的开销。

1. 处理请求的方法中的行参务必加上@RequestParam注解

这样可以避免Spring MVC使用asm框架读取.class文件获取方法行参名。即便Spring MVC对读取出的方法参数名进行了缓存，如果能不读取.class文件当然更好。

### Spring手写实战

# 拉钩Spring

## spring三级缓存（可以解决spring循环依赖问题）

三级缓存：singletonFactorys

二级缓存：early

一级：单例池

假设有两个Bean对象A和B，A中有成员B，

1. A会先创建一个自己为赋值的对象放到三级缓存中（提前暴露自己）
2. B在创建过程中发现依赖于A，那么在三级缓存使用尚未成型的Bean A。把A升级放入二级缓存（放入过程中可以进行一些附加操作）
3. B创建成功后放入一级缓存
4. A去一级缓存中找创建好的Bean B。

> spring无法解决构造函数的单例bean循环依赖

## Spring声明式事务

编程式事务：在业务代码中添加业务事务

声明式事务：通过XMl或者注解配置的方式达到控制事务的方式

# springboot

## 依赖管理

- 为什么导入依赖是不需要指定版本？

spring-starter-parent父依赖起动器下面已经给部分依赖指定了默认版本号，如果手动配置版本号以手动配置为准。

- 项目运行依赖的jar包是从何而来的？

Springboot-starter-web直接依赖了json，tomcat，springMVC等相关依赖，会自动导入相关的依赖

## 自动配置

添加jar包依赖时，自动配置相关信息

- spring自动配置是如何进行的，整体的配置流程是什么样的？
1. springboot应用启动
2. @springBootApplication起作用
3. @EnableAutoConfiguration
4. AutoConfigurationPackage通过Registrar类导入到容器中，registrar扫描主配置信息类同级目录以及子包，并将相应的组件导入springboot创建管理的容器中。
5. 加载所有依赖包下/META-INF/spring.factories的配置路经来完成自动配置。

## 执行原理

1. 创建springbootApplication对象
2. 执行springbootApplication对象的run方法
3. 获取并启动监听器
4. 项目运行环境预配置
5. 创建spring容器
6. spring容器的前置处理器
7. 刷新容器refresh
8. 后置处理器
9. 发出结束执行的事件通知
10. 执行Runners（执行自定义的stater）
11. 返回context对象



# Spring迭代史

> spring的第一个版本于2002年10发布，由一个带有易于配置和使用的控制反转（IoC）容器的小型内核组成。多年来spring已经成为JavaEE的主要替代品，并且发展成一个由许多不同项目组成的成熟技术。

## Spring 0.9

该框架第一个公开的版本，以Exper One-on-One：J2EE一书为基础，提供了bean配置基础、AOP支持、JDBC抽象框架、抽象事务支持等。该版本没有官方参考文档，但可以在SourceForge上找到现有的源代码和文档。

## Spring 1.x

第一个带有官方参考文档的版本。它由下图所示的七个模块组成。

![2024-1-1711:02:42-1705460562385.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-1-1711:02:42-1705460562385.png)

- Spring Core：bean容器以及支持的实用程序。
- SpringContext：ApplicationContext、UI、验证、JNDI、Enterprise JavaBean（EJB）、远程处理和邮件支持。
- Spring ORM：Hibernate、iBATIS和Java Data Object（JDO）支持。

1. Hibernate：在Java对象和关系数据库之间建立映射，已实现直接存取Java对象。
2. JDO：和JPA一样，是对象持久化的规范。

- Spring AOP：符合AOP联盟的面向切面编程（AOP）实现。
- Spring Web：基本集成功能，比如多部分功能、通过servlet监听器进行上下文初始化以及面向Web的应用程序上下文。
- Spring Web MVC：基于Web 的Model-View-Controller（MVC）框架。



## Spring 2.x

该版本由下图所示六个模块组成。现在Spring Context模块包含在Spring Core中，而在Spring 2.x版本中，所有的Spring Web组件都由单个项目表示。

![2024-1-1714:16:50-1705472209888.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-1-1714:16:50-1705472209888.png)



- 通过使用新的基于XML Schema的配置而不是DTO格式来简化XML配置。值得注意的改进方面包括bean定义、AOP以及声明式事务。
- 用于Web和门户的新bean作用域（请求、会话和全局会话）。
- 支持AOP开发的@AspectJ注解
- Java Persistence API（JPA）抽象层。
- 完全支持异步JMS消息驱动的POJO（用于普通的旧Java对象）。

1. JMS：一种消息发布订阅机制，一个服务端维护消息，所有使用者注册在此服务端上，各服务之间通讯不需要对方在线，所有客户端接收消息只要去服务端接收数据。通常用来和RMI对比。

- JDBC简化包括在使用Java5+时的SimpleJDBCTemplate。
- JDBC命名参数支持（NamedParameterJdbcTemplate）。
- 针对Spring MVC的表单标签库。
- 对Porlet MVC框架的介绍。
- 动态语言支持。可以使用JRuby、Groovy以及BeanShell来编写bean。
- JMX中的通知支持以及可控的MBean注册。

1. JMX：用于监控和管理Java应用程序运行状态、设备和资源信息、Java虚拟机运行情况等信息。

- 为调度任务而引入的TaskExecutor注册。
- 为调度任务而引入的TaskExecutor抽象。
- Java注解支持，特别针对@Transactional、@Required和@AspectJ。



## Spring 2.5.x

该版本包含以下功能。

- 名为@Autowired的新配置注解以及对JSR-250注解（@Resource、@PostConstruct和PreDestroy）支持。

1. @PostConstruct：JDK默认的注解，用来实现Bean初始化之前的操作。

执行顺序：

Construct（构造方法）-》@Autowired（依赖注入）-> @PostConstruct（注释的初始化方法）

依赖注入完成后用于执行的初始化方法，并且只会被执行一次

2. @PreDestroy

在服务器卸载Servlet的时候运行，并且只会被执行一次。

- 新的构造型注解：@Component、@Repository、@Service、和@Controller。
- 自动类路径扫描支持，可以检测和连接带有构造型注解的类。
- AOP更新，包括一个新的bean切入点元素以及AspectJ加载时织入（weaving）。
- 完整的WebSphere事务管理支持。

1. WebSphere

类似于tomcat，IBM公司推出的集成软件平台，收费不开源。

- 除了SpringMVC@Controller注解，还添加了@RequestMapping、@RequestParam和@ModelAttribute注解，从而支持通过注解配置进行请求处理。

1. ModelAttribute

标注在方法，被标注此注解的方法可以用来给model添加属性。

- 支持Tiles2。

1. tiles2：XML的新的配置方式。

- 支持JSF1.2。

1. JSF1.2：JSP新的配置方式

- 支持JAX-WS2.0/2.1。
- 引入了Spring TestContext Framework，提供注解驱动和集成测试支持，不受所用测试框架的影响。
- 能够将Spring应用程序上下文部署为JCA配置器。

1. JVA：应用和外部连接的规范。

## Spring 3.x

这是基于Java5的第一个版本，旨在充分利用Java5的功能，如泛型、可变参数和其他语言改进。该版本引入基于Java的@Configuration模型。目前已经对框架进行了修改，分别针对每个模块JAR使用一棵源代码树进行管理。

如下图所示的抽象描述

![2024-1-1714:53:57-1705474437048.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-1-1714:53:57-1705474437048.png)



- 支持Java5功能，例如泛型、可变参数以及其他改进。
- 对Callables、Futures、ExeutoService适配器和ThreadFactory集成提供很好的支持。
- 框架模块目前针对每个模块JAR都使用一棵源代码树进行分别管理。
- Spring Expression Language（SpEL）的引入。

1. SPEL

能在运行时构建复杂表达式、存取对象图属性、对象方法调用等等，并且能与Spring功能完美整合，如能用来配置Bean定义。



- 核心Java Config功能和注解的集成。
- 通用型转换系统和字段格式化系统。
- 全面支持REST。

1. Rest风格：

对于同一个类或者对象，增删改查操作，传统可能需要调用不同的接口，rest风格可以根据调用的请求类型（POST，GET，DELETE,PUT)来用同一个接口，直接完成上述的所有操作。

- 新的MVC XML 名称空间和其他注解，例如Spring MVC中的@CookieValue和RequestHeaders。
- 验证增强功能和JSR-303（bean验证）支持。
- 对JavaEE的早期支持，包括@Async @Asynchronous注解、JSR303、JSF2.0、JPA2.0等。
- 支持嵌入式数据库、例如HSQL、H2和Derby。



## Spring 3.1.x

该版本包含以下功能。

- 新的缓冲对象。
- 可以用XML定义bean定义配置文件，同时也支持@Profile注解。
- 针对统一属性的环境抽象。
- 与常见Spring XML名称空间元素等价的注解，如@ComponentScan、@EnableTransationManagement、@EnableCaching、@EnableScheduling、@EnableAsync、@EnableAspectAutoProxy、@EnableLoadTimeWeaving和@EnableSpringConfigured。
- 支持Hibernate 4.
- Spring TestContext Framework对@Configuration类和bean定义配置文件的支持。
- 名称空间c：简化了构造函数注入。

![2024-1-1715:05:02-1705475102295.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-1-1715:05:02-1705475102295.png)

- 支持Servlet 3中Servlet容器的基于代码的配置。
- 能够在不使用persistence.xml的情况下启动JPA EntityManageFactory。
- 将Flash和RedirectAttributes添加到Spring MVC中，从而允许使用HTTP会话重定向属性。
- URI模块变量增强功能。
- 能够使用@Valid来注解Spring MVC @RequestBody控制器方法参数。

## Spring 3.2.x

该版本包含以下功能。

- 支持基于Servlet 3的异步请求处理。
- 新的Spring MVC测试框架。
- 新的Spring MVC注解 @ControllerAdvice和Matrix Variable。
- 支持RestTemplate和@RequestBody参数中泛型类型。
- 支持Jackson JSON2.
- 支持Tiles 3.
- 现在，@RequestBody或@RequestPart参数的后面可以跟着一个Errors参数，从而可以对验证错误进行处理。
- 能够通过使用MVC名称空间和Java Config配置选项来排除URL模式。
- 支持没有Joda Time的@DateTimeFormat。
- 全局日期和时间格式化。
- 跨框架的并发优化，从而最小化锁定，并改进了作用域，原型bean的并发创建。
- 新的基于Gradle的构建系统。
- 迁移到GIthub
- 在框架和第三方依赖中支持精简的Java SE7/OpenJDK 7。现在，CGLIB和ASM已经成为Spring的一部分。除了AspectJ 1.6，其他版本都支持AspectJ 1.7。



## Spring 4.0.x

这是一个重要的Spring版本，也是第一个完全支持Java 8的版本，虽然仍然可以使用较久版本的Java，但Java SE6已经提出了最低版本要求。弃用的类和方法已经被删除，但模块组织几乎相同。

![2024-1-1715:25:01-1705476300515.png](https://gitee.com/grsswh/drawing-bed/raw/master/image/2024-1-1715:25:01-1705476300515.png)

- 通过spring.io网站上的一系列入门指南提高了入门体验。
- 从先前的Spring 3 版本中删除弃用的软件包和方法。
- 支持Java8，将最低Java版本提高到6 Update 18.
- Java EE6及以上版本现在被认为是Spring Framework 4.0的基准。
- Groovy bean定义DSL，允许通过Groovy语法配置bean定义。
- 核心容器、测试和一般web改进。
- WebSocket、SocJS、和STOMP消息。

## Spring 4.2.x

该版本包含以下功能。

- 核心改进（例如，引入@AliaFor，并修改现有注解以使用它）。
- 全面支持Hiermate ORM 5.0.
- JMS和Web改进。
- 对WebSocket消息传递的改进。
- 测试改进，最引人注目的是引入了@Commit来替换Rollback（false），并引入AopTestUtils使用工具类，允许访问隐藏在Spring代理后面的底层对象。



## Spring 4.3.x

该版本包含以下功能。

- 完善了编程模型。
- 在核心容器（包含ASM 5.1、CGLIB 3.2.4以及spring-core.jar中的Objenesis 2.4） 和MVC方面有了相关大的改进。
- 添加了组合注解。
- Spring TestContext Framework需要JUnit 4.12或更高版本。
- 支持新的库，包括Hibernate ORM 5.2、Hibernate Validafor 5.3、Tomcat 8.5和9.0、Jacson 2.8等。



## Spring 5.x

- 这是一个主要版本。整个框架代码都基于Java 8，并且自2016年7月完全兼容。
- 支持Portlet、Velocity、JaspReports、XMLBeans、JDO、Guava、Tiles 2和Hibernate 3。
- 现在XML配置名称空间被流式传输到未版本化的模式；虽然特定版本的声明仍然被支持，但要针对最新的XSD架构进行验证。
- 充分利用Java 8的强大功能，从而在性能上得到极大的改进。
- Resource抽象为防御getFile访问提供了isFile指示符。
- Spring提供的Filter实现完全支持Servlet 3.1 签名。
- 支持Portobuf 3.0
- 支持JMS 2.0+和JPA 2.0+。
- 引入了Spring Web Flow，这是一个用于替代Spring MVC的项目，构建在反应式基础之上，这异味着他完全是异步非阻塞的，主要用于事件循环执行模型，而非传统的每个请求执行模式都带有一个线程的最大线程池（基于Project Reactor构建）。
- Web和核心模块适用于反应式编程模型。
- Spring测试模块有了很大的改进。现在支持JUnit5，引入了新的注解来支持Jupiter编程和扩展模型，例如@SpringUnitConfig、@SpringJUnitWebConfig、@Enabledlf和Disabledlf
- 支持在Spring TestContext Framework 中实现并行测试执行。

## Spring 6.x

- 整个代码库基于JDK17
- Servlet，JPA等从javax迁移到Jakarta命名空间。
- 运行时与Jakarta EE 9以及Jakarta EE 10 API的兼容性。
- 与最新的Web服务器兼容：Tomcat 10.1，Jetty 11，Undertow 2.3，
- 早期兼容虚拟线程（JDK19预览）。

### 核心修改

- 升级到ASM 9.4和Kotlin 1.7
- 完整的CGLIB fork，支持捕获CGLIB生成的类。
- 全面的向AOT（Ahead-Of-Time Porcessing，提前处理）转型。
- 对GraalVM原生映像的一流支持。

### 核心容器

- 默认情况下，无需java.beans.Introspector来确定基本bean属性。
- 在GenericApplicationContext (refreshForAotProcessing)中支持AOT处理。
- 基于预解析构造函数和工厂方法的Bean定义转换。
- 支持AOP代理和配置类的早期代理类确定。
- PathMatchingResourcePatternResolver使用NIO和模块路径API进行扫描，分别支持GraalVM本机映像和Java模块路径中的类路径扫描。
- DefaultFormattingConversionService支持基于ISO的默认Java.time类型解析。

### 数据访问和事务

- 支持预定JPA托管类型（用于包含AOT处理中）。
- JPA支持Hibernate ORM 6.1 （保持与Hibernate ORM 5.6的兼容）。
- 升级到R2DBC 1.0 （包括R2DBC事务定义）。
- 删除JCA CCI支持。

### Spring消息传递

- 基于@RSocketExchange服务接口的RSocket接口客户端。
- 基于Netty 5 Alpha的Reactor Netty 2的早期支持。
- 支持Jakarta WebSocket 2.1以及其标准WebSocket协议升级机制。



### 通用Web修订

- 基于@HttpExchange服务接口的Http接口客户端。
- 支持RFC 7807问题详细信息。
- 统一HTTP状态码处理。
- 支持Jackson 2.14
- 与Servlet 6.0对齐（同时保留与Servlet 5.0的运行时兼容性）。

### Spring MVC

- 默认情况下使用PathPatternParser（能够选择进入PathMathcer）
- 删除过时的Tiles和FreeMarker JSP支持。



### Spring WebFlux

- 新的PartEvent API 用于流式传输多部分表单上传（两者都在客户端和服务器）。
- 新的ResponseEntityExceptionHandler用于自定义WebFlux异常并呈现RFC 7807错误响应。
- 非流媒体类型的Flux返回值（写入前不再收集到List）。
- 基于Netty 5 Alpha的Reactor Netty 2的早期支持。
- JDK HttpClient与WebClient集成。

### 可观察性

- Micrometer Observation 直接可观察性在Spring框架中的部分应用。Spring-web模块现在需要io.micrometer:micrometer-observation:1.10+作为编译依赖项。
- RestTemplate和WebClient被检测为生成HTTP客户端请求观察。
- Spring MVC可以使用新的org.springframework.web.filter.ServerHttpObservationFilter检测HTTP服务器观察。
- Spring WebFlux可以使用新的org.springframework.web.filter.reactive.ServerHttpObservationFilter检测HTTP服务器观察。
- 对于Flux和Mono的Micrometer Context Propagation集成，从控制器方法返回值。



### 测试

- 支持在JVM或GraalVM本机映像中测试AOT处理的应用程序上下文。
- 集成HtmlUnit 2.64+请求参数处理。
- Servlet模拟（MockHttpServletRequest、MockHttpSession）现在基于Servlet API 6.0。
