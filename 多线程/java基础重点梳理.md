> 写在前面的话

目前已经看过的书

- java

java核心技术 卷1 基础知识 原书第8版

Java 8实战



# java基础（重点梳理）



## 1. 关于计算机的原码，反码和补码

> ### 这是底层重点，下面的各级规定必须背会

1. 二进制的最高位是符号位：0表示正数，1表示负数
2. 正数的原码，反码和补码都一样。
3. 负数的反码=它的原码**符号位不变**，其他位取反（0->1,1->0）
4. 负数的补码=它的**反码+1**，负数的反码=它的**补码-1**
5. 0的反码，补码都是0
6. java没有无符号数，换言之java中的数都是有符号的。
7. 在计算机运行时都是以**补码的方式**运行的。
8. 当我们看它的运行结果时，要看它的原码


### 四个位运算符

- 按位与&：两位全为1，结果为1，否则为0
- 按位或|：两位有一位为1，结果为1，否则为0
- 按位异或^：一个为0，一个为1，结果为1，否则为0
- 按位取反~：0->1,1->0

#### 举例

1. 2&3=?
   2的补码：

   0000 0000 0000 0000 0000 0000 0000 0010

   3的补码：

   0000 0000 0000 0000 0000 0000 0000 0011

   2&3的补码：

   0000 0000 0000 0000 0000 0000 0000 0010

   2&3的原码：

   0000 0000 0000 0000 0000 0000 0000 0010	(2)

2. ~-2=？
   -2的反码:

   1111 1111 1111 1111 1111 1111 1111 1101

   -2的补码：

   1111 1111 1111 1111 1111 1111 1111 1110

   取反：

   0000 0000 0000 0000 0000 0000 0000 0001

   原码：

   0000 0000 0000 0000 0000 0000 0000 0001（1）

3. ~2=?
   2的补码：

   0000 0000 0000 0000 0000 0000 0000 0010

   取反：

   1111 1111 1111 1111 1111 1111 1111 1101

   补码转反码：

   1111 1111 1111 1111 1111 1111 1111 1100

   反码再变回原码：

   1000 0000 0000 0000 0000 0000 0000 0011（-3）

4. 2|3=？

   2的补码:

   0000 0000 0000 0000 0000 0000 0000 0010

   3的补码：

   0000 0000 0000 0000 0000 0000 0000 0011

   或运算：

   0000 0000 0000 0000 0000 0000 0000 0011

   补码变反码变原码：

   0000 0000 0000 0000 0000 0000 0000 0011（3）

5. 2^3=?
   2的补码：

   0000 0000 0000 0000 0000 0000 0000 0010

   3的补码：

   0000 0000 0000 0000 0000 0000 0000 0011

   异或运算：

   0000 0000 0000 0000 0000 0000 0000 0001（1）

#### 位运算的妙用

>  可直接使用 *(m&1)==1?奇数：偶数* 获得结果*，如：

```java
    boolean a = (3&1)==1              //true

    boolean b = (4&1)==1            //false

```

> 不用临时变量交换两个整数的值:

``` java
    int a = 3, b = 4

    a = a^b

    b = a^b        // b = 3

    a = a^b        // a = 4
```

> 原理：
>
> 异或0具有保持的特点，即1010^0000 = 1010;
>
> 异或1具有翻转的特点，即1010^1111 = 0101;
>
> 由此可推导：
>
> b^(a^b) = a
>
> a^(b^(a^b)) = b

### 三个位移运算符

- 算数右移>>:低位溢出。符号位不变，并用符号位补溢出的高位。
- 算数左移<<：符号位不变，低位左移补0
- 逻辑右移>>>：也叫无符号右移，低位溢出高位补0

``` java
int a=1>>2;  	//相当于1/2/2
int a=1<<2;		//相当于1*2*2
```



## 单例模式

### 说明：

> 所谓的单例模式就是在整个程序的巡行过程中，一个类只能被创建一次，且这个类只提供一个创建盖实例的方法。

### 实现原理：

> 将该类的构造方法的访问权限设置成private，这样这个类就不能在类的外部用new关键字来创建对象，但类的内部仍可以产生该类的对象。因为在类的外部无法得到类的对象，只能调用该类的某个静态方法来返回类内部创建的对象，静态方法只能访问类中的静态成员变量，指向类内部产生的该类对象的变量也必须是静态的。

java中最经典的单例模式：Runtime

### 饿汉式与懒汉式

```java
public class Api {
    public static void main(String[] args) {

      GirlFriend girlFriend = GirlFriend.getGirlFriend();
      GirlFriend girlFriend2 = GirlFriend.getGirlFriend();
//      GirlFriend girlFriend1 = new GirlFriend(); //报错
      System.out.println(girlFriend.hashCode());  //2052001577
      System.out.println(girlFriend2.hashCode()); //2052001577

      //懒汉
//      Wifi wifi = new Wifi("abc");  //报错
      Wifi wifi = Wifi.getWifi("123");
      Wifi wifi1 = Wifi.getWifi("1233");
      System.out.println(wifi.hashCode());    //1160264930
      System.out.println(wifi1.hashCode());   //1160264930
    }


}
//饿汉式
class GirlFriend{

  private String name;
  static GirlFriend gf=new GirlFriend("小红");

  private GirlFriend(String name){
    this.name=name;
  }

  public static GirlFriend getGirlFriend(){
    return gf;
  }
}

//懒汉式
class Wifi{

  private String name;

  //静态的属性
  private static Wifi wifi;  //默认初始值为null

  private Wifi(String name){
    this.name=name;
  }

  public static Wifi getWifi(String name){
    if (wifi ==null){
      wifi=new Wifi(name);
    }
    return wifi;
  }
}
```

- 两者在创建的对象的时机不同：饿汉式是在类加载就创建对象实例，懒汉式在使用时才创建。
- 饿汉式不存在线程安全问题，懒汉式存在线程安全问题。
- 饿汉式浪费资源，懒汉式不浪费资源。



## java注解中常用的几个参数分别代表



- TYPE：类型，也就是我们自己创建的类
- FIELD：字段，我们创建的类中定义的字段
- METHOD：方法，类中常用的方法
- PARAMETER，参数。
- CONSTRUCTOR，构造器。
- LOCAL_VARIABLE，局部变量。 



## String

### 介绍：

1. String是final类，不可被继承
2. 实现了Serializable接口：可以保存到文件或者网络传输
3. 实现了Comparable接口：可以进行比较
4. String的内容是存在final char value[]中

源码图：

![](https://ae01.alicdn.com/kf/U1924f08ccc7d41b9a9b6517eeb52aad2Z.jpg)

### String 赋值

- 方式一：String s1 = "hello"
- 方式二：String s2 = new String("hello");

> 方式一：从常量查看是否有”hello“的数据空间，如果有，直接指向，如果没有重新创建，然后指向，s1最终指向的是常量池中的数据空间。

> 方式二：现在堆中创建空间，里面维护了value属性，然后指向常量池中的Hello空间，如果有，通过value指向。s2最终指向的是堆中的空间地址。

#### 模型图

![](https://ae01.alicdn.com/kf/Ubcb55447cb2248f5a4815395832baa25L.jpg)

####  扩展

> String 中的intern ()方法表示指向常量池中的对象。所以对比intern()时相等

``` java
	@Test
    public void Datetest(){
       String s1= "hello";
       String s2= new String("hello");
      System.out.println(s1==s2); //f
      System.out.println(s1.intern()==s2.intern());   //true
        System.out.println(s1==s2.intern());	//true
    }
```

### String的特性

- String s1= "ab"+"c";

这个只在常量池中只创建了一个”abc“的空间。

这是由于jvm的智能优化。

``` java
String s1="ab";
String s2="c";
String s3=s1+s2;
```

s3指向是堆中的地址。s3创建调用stringBuilder的append方法来拼接创建，指向的地址为堆。



#### String的效率问题

> String类保存的是字符串常量，每次更新都要重新开辟空间，效率特别低。



#### String的equals方法说明

string的equals方法，是先比较两个对象的类型是否相同，然后再比较值是否相同。如果类型不相同，会直接返回false。

![](https://ae01.alicdn.com/kf/U169b6afd609a490085677fcd06db57fdr.jpg)



### String和StringBuffer的对比

- String保存的是字符串常量，里面的值不能更改，每次String类的更新实际上都是在更改地址，效率低。（private final char value[];）
- StringBuffer保存的是字符串变量，里面的值可以更改，每次StringBuffer的更新实际上可以更新内容，不用更新地址，效率高（private transient char[] toStringCache;）
- 更重要的，**StringBuffer是放在堆中的**。
- StringBuffer的equals方法并没有像String一样重写了equals方法，而是直接调用了OBject的equals，所以StringBuffer的equals也是比较地址是否相同，而不是比较值是否相同。



### StringBuilder

> StringBuilder的方法和StringBuffer相同，只是StringBuilder比StringBuffer少了synchronized关键字。
>
> 所以，**StringBuilder的大部分运行速度要比StringBuffer还要快，但是StringBuilder线程不安全。**



## Collection

### 迭代器Iterator

- 关于remove

remove方法回删除当前指针所扫过的元素，如果调用next方法，就删除指针之前的一个元素，如果调用previous方法，就删除当前指针之后的一个元素。



#### 介绍

1. Iterator被称为迭代器（设计模式的一种），主要用于遍历集合中的元素。
2. 所有的实现了Collection接口的集合类都有一个iterator()方法，用来返回迭代器。
3. iterator只用来遍历集合，内有承载对象的能力。
4. ArrayList中的iterator()方法实际上返回了ArrayList中的一个内部类Itr，此类实现了iterator方法。

> 实际上是ArrayList的迭代器都是用的Itr中的方法。

``` java
public Iterator<E> iterator() {
        return new Itr();
    }

private class Itr implements Iterator<E>{
    ...
}
```



#### 关于迭代器的一个实例

```java
@Test
    public void CollectionTest(){
      ArrayList<Object> list = new ArrayList<>();
      Book book1 =new Book("1","1","1");
      Book book2 =new Book("2","2","2");
      Book book3 =new Book("3","3","3");
      list.add(book1);
      list.add(book3);
      list.add(book2);
      list.add("abc");

      System.out.println(list);

      Iterator<Object> iterator = list.iterator();
      while (iterator.hasNext()){
        System.out.println(iterator.next());
      }

      System.out.println("第二遍遍历");

      //重置迭代器,如果不重置，迭代器只能用一次
      iterator=list.iterator();
      while (iterator.hasNext()){
        System.out.println(iterator.next());
      }


    }
```



> 增强for循环的底层依然还是迭代器

### ArrayList的扩容机制

- ArrayList中有一个object类型的数组elementData用来存值。

``` java
private static final Object[] EMPTY_ELEMENTDATA = {};

    transient Object[] elementData; // non-private to simplify nested class access

```

- 创建对象时，如果使用无参构造器创建对象时，初始化elementData的容量为0（JDK7为10）；
- 添加元素时，先判断是否需要扩容，如果需要，就调用grow方法，否则就直接添加元素到合适的位置。

``` java
private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```



- 使用无参构造创建，第一次添加时，就elementData扩容为，如果需要再次扩容时，就扩容elementData为1.5倍。
- vector和ArrayList类似，只是它添加了synchronized关键字，vector是线程安全的，但是很少有运用。

### LinkedList

#### 介绍：

- LinkList底层是由双向链表构成的
- LinkList实现了List接口的所有操作
- 线程不安全，没有实现同步



#### 实现代码

![](https://ae01.alicdn.com/kf/Uf115db4057364f409eb37db8adccd2adn.jpg)

> LinkedList由于是由双向链表构成，所以它添加和修改的速度都特别快，而且不设计到扩容的问题。但是查询慢。
>
> ArrayList底层是由数组构成，而且还设计到数组扩容的问题。所以它添加和修改慢，但是查询快。



### set

#### hashSet

- hashSet实际上是一个hashMap

![](https://ae01.alicdn.com/kf/U770ea241bbb04000b5b1c951ac538428l.jpg)

- 可以存放空值，但是只能存一个null
- hashSet不能保证元素是有序的，顺序取决于hash后，再取索引值的结果。
- hashSet其实是把对应的值当hashMap的key，所以不能存储重复的元素。

#### hashSet添加元素

- 首先先调用map的put方法来添加元素

```java
private static final Object PRESENT = new Object();
public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
```

> PRESENT是一个空的占位，里面什么都没有

- 对key进行hash算法来求出hash值再偏移

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

- 然后调用HashMap的putVal方法来把key和空value放到map中

```java
public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

> 如果得到多个相同的hash值，此处会存储一个单项链表，当此单项链表存储的值超过一定的数量后就会树化。（在map那里再详细说）

因为hashSet会进行树化，所以它对比list，删除和添加的效率都要更好。



#### treeset

treeset可以自定义key规则来排序。

只要在new此set时new一个Comparator方法

``` java
TreeSet<Object> objects = new TreeSet<>(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (((String) o1).length() - ((String) o1).length() > 0) {
                    return 1;
                }
                return 0;
            }
        });
```

treeset的去重也是通过自己定义的这个compare方法返回值来决定是否添加，如果返回值为0就不添加。如果没有传入compare方法，则用string下的compareTo方法来去重



## map

### hashMap



#### map的体系结构

![](https://ae01.alicdn.com/kf/U5dc4e8c4ff1a48de97cb3579bfa45af6M.jpg)



#### hashMap的储存数据结构详情

hashMap的key是由set组成

在添加时，先算出他的hashcode，根据根据hashcode算出数组对应的下标。当所算出的下标重复时，先生成单向列表，新加入的数据放到尾节点（头插法）。



#### 关于hashmap的树化元素个数问题（1.8）

- 到底同一table下标中，当第几个元素插入时才会树化？

> - 结果：
>
>   当放入第十个元素时，此时的hashmap还没有红黑二叉树。
>
>   当放入第十一个元素时，此时的node变成了treeNode，
>
>   在极限状态下，hashmap在同一索引中，放入第十一个元素时，才会变成红黑二叉树。

- **真正的扩容机制**

1. 它一开始是默认长度为十六的数组。
2. 当一个索引中的元素超过到8（9）时，他会优先选择将数组长度扩容为两倍（即长度为32）的数组，它会认为是不是由于数组的长度不够才会导致一个索引中的元素过多
3. 当它发现长度为32时，某一个索引中的元素还是超过8（10），它还是会选择将数组再次扩容为两倍的数组。（64）
4. 当数组长度为64时，它发现某一索引中的元素还是超过8（11）时，它就会对该索引所在的链表转化为红黑二叉树。当第十一个元素时为红黑二叉树。

> 两个条件，数组长度为64，且同一索引下长度大于8



#### hashmap报cpu100%问题（JDK7）

> 当多个线程同时运行，且一个相同的hashmap需要扩容时，hashmap底层数组中存的值为单项链表，扩容时通过hash算出固定的数组下标后，还是采用头插法来存储。实际上扩容是把原来index下标不变的链表倒序。
>
> 当第一个线程运行结束后，第二线程会出现下标循环问题，所以就会一直死循环。（cpu100%）







### 红黑树

- 红黑树底层的数据结构？

（特殊的二叉树）二叉查找树

#### 红黑树的特性

- 每个节点不是红色就是黑色
- 不可能有连在一起的红色结点
- 根节点都是黑色 root

根节点：

> 入度为零，深度为零，没有父结点

- 每个红色结点的两个子结点都是黑色

#### 红黑树的左旋和右旋

> 好处：只需修改两个结点的数据就可以完成

- 图解

![](https://ae01.alicdn.com/kf/U2c355547105f43f5bc63839b84b9dec85.jpg)

- 左旋

向左旋转，复制需要变成y结点的左结点地址，把原来y结点的右结点改成复制的结点，再把新变成y结点的左结点变成原来y结点的地址。

> 右旋同理，更改上面的左右。



#### 红黑树变化规则

**所有插入的点的默认颜色是红色**

##### 颜色的变化规则：

- 当前结点的父亲是红色，且它的祖父结点的另一个子结点也是红色（叔叔结点）：

1. 把父亲结点设置为黑色
2. 把叔叔结点也设置为黑色
3. 把祖父也就是父亲的父亲设置为红色（爷爷）
4. 把指针定义的祖父结点也就是当前要操作的爷爷结点

- 左旋：

当前父结点为红色，叔叔是黑的时候，且当前为右子树的时候，左旋以父结点作为左旋

- 右旋：当前父结点是红色，且叔叔结点黑色时，且当前结点是左子树，右旋：
  1. 把父结点变为黑色
  2. 把祖父结点变为红色
  3. 以祖父结点旋转

![](https://ae01.alicdn.com/kf/U0f0c9f3112eb470b9fb53be4483ac67d5.jpg)



### hashtable

1. hashtable的键和值都不能为空。
2. hashtable和hashmap表面上看是一样的
3. hashtable有synchronized关键字，线程安全，hashmap不安全。

### linkedhashmap

键插入和取出的顺序是一致的



### treemap

treemap和treeset相同，可以自定义key的排序规则

``` java
TreeMap<String, String> stringStringTreeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() - o2.length() > 0) {
                    return 1;
                }
                return 0;
            }
        });
```



![](https://ae01.alicdn.com/kf/U83099c02aff049f0a79408b2eefbd7ed5.jpg)



## 多线程

- 计算机的组成

![](https://ae01.alicdn.com/kf/Ubc80b6a3ad7d4b4aa0766be78a292a947.jpg)

- 继承Thread和实现Runnable

1. 从java的设计角度来看，继承Thread和实现Runnable接口来创建线程本质上没有区别，因为Thread类本身就实现了Runnable接口
2. 实现Runnable接口更适合多个线程共享同一个资源的情况，并且避免了单继承的限制

- 守护线程：

```java
t1.setDaemon(true);
```

为其它线程服务的线程，当主线程结束时，守护线程自动结束。

> 守护线程不会管自己是否运行结束，只要主线程运行结束，自己就结束。

### 线程的生命周期

> 线程的生命周期即线程的几种状态。
>
> 线程的生命周期可以通过getState()方法来获得。
>
> getState()实际上是一个枚举类



- NEW

  新建状态，创建了线程对象在调用start()之前的状态。

- RUNNABLE

  可运行状态，这是一个复合状态分为：READY和RUNNING两个状态

  - READY

    该线程可以被线程调度器进行调用，调用后变为RUNNING状态

  - RUNNING

    该线程正在被执行

    > thread.yield()方法可以把线程由RUNNING状态变为READY

- BLOCKED

  阻塞状态，线程发起阻塞的i/o操作，或者申请由其他线程占用的独占资源，线程会转换为阻塞状态，处于阻塞状态的线程不会占用cpu资源，当阻塞i/o操作执行完或者线程获得了其申请的资源，线程会变为RUNNABLE状态。

- WAITING

  等待状态，当线程执行了Object().wait()，或者thread.join()方法后，线程变为等待状态，当执行了Object().notify()方法，或者插入的线程执行完毕之后，当前线程会转换为RUNNABLE状态。

- TIMED_WAITING

  与WAITING相似，区别在于此线程不会一直无限的等待，当在预订的时间之内没有完成预期的操作，线程会自动转换为RUNNABLE状态。

- TERMINATED

  终止状态，线程结束，处于终止状态。



#### 线程的生命周期图解

![](https://ae01.alicdn.com/kf/U005c0b101fdd4d6bb3049f5c6d87b469k.jpg)







----



- synchronized的本质是什么？

就是更改对象的markword。



- isAlive()

判断当前线程是否处于活动状态





### 互斥锁

- java在java语言中，引入了对象互斥锁的概念，来保证共享数据操作的完整性。
- 每个对象都对应于一个可称为“互斥锁”的标记，这个标记用来保证在任意时刻，只能有一个线程来访问该对象。(cpu级别的标记)
- 关键字synchronized来于对象的互斥锁联系。当某个对象用synchronized修饰时，表明该对象在任意时刻只能有一个线程来访问。
- 同步的局限性：导致程序的执行效率被降低。
- 同步方法（非静态）的锁为this。
- 同步方法（静态）的锁为当前类本身。



### 单例模式双检测

- DCL

![copy.png](https://ae03.alicdn.com/kf/Ud055b9c3b26f4dee9f05b48bdb8c40ddz.jpg)

if判断两遍，效率高。





- 线程和进程

进程：

> 进程是一个静态的概念，一个程序所有的东西都加载到内存中，这叫一个进程。

线程：

> 线程是说程序执行中，cpu要从内存读取数据然后执行，这个过程叫一个线程。多线程就是说cpu的多个核心同时读取内存中的数据运行。



### 线程安全问题的三个特性

#### 可见性

- 什么是可见性问题

  两个线程操作同一个对象的数据，当A线程更改了数据，B线程还只是读到A更改前的数据。

- 造成可见性的原因：

  > 1. 编辑器对循环判断条件进行优化，变成只进行一次判断。
  > 2. cpu中一个内核修改数据，另一个内核没有读到这个被修改的数据

- cpu三级缓存：

![](https://ae01.alicdn.com/kf/U089e539e671746248aa76b1fb8508e2bc.jpg)



- 三级缓存的大致执行流程

当程序加载到内存中时，cpu的核心要去读取这些数据，要先从内存中读取到L3，然后不同的核心再读取到L2，现在要执行的数据再读取到L1。



- cpu对于多线程可见性的机制

> 64位的cpu的一个核心去内存中一次读取64bytes的数据（64字节）。称为缓存行。
>
> 一次读取的64行数据中存在不同对象的属性。而且有两个核心（线程）各操作此缓存行中的不同属性。为了保证为了保证两个缓存行中的数据相同。不同的品牌的cpu会采用不同的协议来同步缓存行中的数据。（intel为MESI协议）这样就会影响到数据处理的速度。
>
> jdk中常用padding来补全缓存行。
>
> 比如：

``` java
private long p1,p2,p3,p4,p5,p6,p7;          		//7*8=56
public long x=0L;                           					//8
private long p11,p12,p13,p14,p15,p16,p17;   //7*8=56
```

通过上面的方法可以保证要操作的数据一定会占满一个缓存行，来提高性能。（牺牲空间换时间）



#### 有序性



- cpu执行的乱序性

一个cpu在同一时刻只能执行一条指令。当上一行代码需要去某个空间读取数据时，cpu有时会预先执行它后面的指令。 cpu的乱序执行必须保证单线程的数据一致性。



- cpu为什么要乱序执行

为了加快执行速度（节省时间）



##### 两种重排序

- 指令重排序

  > 源码顺序与程序顺序不一致，程序顺序和执行顺序不一致，这种就是发生了指令重排序。javac编译器重一般不会发生指令重排序，JIT（JVM内置编译器）可能执行指令重排序

- 存储子系统重排序

  > 由于cpu的读写速度要比内存的读写速度快的多（一般是100:1），所以有三级缓存的概念。就算程序没有发生指令重排序，而各缓存之间读写的顺序也可能不一样。这就是存储子系统重排序



#### 原子性

原子性就是说一段代码中的数据，我们只能得到他开始和结束的值，没法拿到它中间状态的值，这就是原子性。



- java实现原子性的两种方式

一种是使用锁，另一种是使用计算机的CAS(Compare And Swap)指令。

> 锁具有排他性，保证共享变量在某一时刻只能被一个线程访问。
>
> CAS指令直接在硬件（处理器和内存）层次上实现，看作是硬件锁。



### 线程同步机制

> 线程同步机制是用于协调线程之间的数据访问的机制，该机制可以保证线程的安全
>
> java平台提供的线程同步机制包括，锁，volatile关键字，final关键字，static关键字，以及相关的api，如Object.wait()/Object.notify()等。



#### 锁

让原本并行的线程变成串行

- 内部锁

通过synchronized关键字实现

- 显示锁

通过java.concurrent.locks,lock接口的实现类实现



- 锁是如何保证多线程三大安全问题的

原子性 ：

> 通过互斥，一个锁只能被一个线程持有，使得临界区代码只能被一个线程执行，即具备了原子性。

可见性：

> 通过写线程冲刷处理器的缓存，读线程刷新处理器的缓存来实现的。
>
> - 获得锁
>
> 刷新处理器的缓存。
>
> - 释放锁
>
> 冲刷处理器的缓存。

有序性：

> 写线程在临界区执行嗯在读线程所执行ed临界区看来完全按照源码顺序执行的。



- 锁的可重入性

一个线程在持有该锁时，能否再次（多次）申请该锁。

- 锁的争用与调度

java平台内部锁属于非公平锁，显示lock锁既支持公平锁又支持非公平锁。

- 锁的粒度

一个锁可以保护的共享数据的数量大小。



### synchronized（内部锁）

内部锁是一种排他锁，可以保障原子性，可见性与有序性。



### cas

- Cas的原理

在把数据更新到主内存时，再次读取主内存变量的值，如果现在变量的值与期望的值（操作起始时读取的值）一样就更新。

- CAS算法

在把数据更新到主内存的共享变量之前，再次读取主内存里共享变量的值，如果现在读取的共享变量值与期望的值一样就更新。

- cas的ABA问题

多个线程共同操作同一个对象的同一个值，1线程把值A改为了B，2线程又把B改回了A，在C线程读取时，由于cas，会认为此值没有改变过。出现ABA问题。解决ABA问题的方案，给此值加一个版本号，每次检测时也检测版本号。



### 线程间的通信（管道通信）

管道通信用io包中的PipedInputStream，和PipedoutputSteam来实现，out负责写入，input负责读取。写入时调用，write方法，读取时调用read方法。

### ThreadLocal的使用

> 除了控制资源的访问以外，还可以通过增加资源的方式来保证线程的安全。ThreadLocal主要是为每一个线程增加自己的值



### lock显示锁

> 在jdk1.5中增加了lock锁接口，有 ReentrantLock实现类，ReentrantLock锁称为可重入锁，它功能比synchronized多。

#### 锁的可重入性

> 当一个线程获得一 个对象锁后，再次请求该对象锁时还可以获得该对象锁。



- lock()获得锁
- unLock()释放锁
- lockInterruptibly()：如果当前线程未被中断则获得锁,如果当前线程被中断则出现异常



# java核心技术基础



## 集合

- Collection接口扩展了Iterable接口，任何集合都可以使用foreacch循环

- 省略掉线程安全集合的常用集合

![](https://s2.loli.net/2021/12/21/45yZqm9eYLiBCwr.png)



- 各非线程安全集合间的关系

![](https://s2.loli.net/2021/12/22/ZbgsN5OFRSajtA4.png)



# JAVA8

- 函数式接口

函数式接口就是只定义一个抽象方法的接口。

- java8中常见的函数式接口

![](https://s2.loli.net/2021/12/24/KyJkctmRranl2S6.png)

![](https://s2.loli.net/2021/12/24/gYQHkJTft3I6P5m.png)





