# rust语言的程序与设计

## 常见的编程概念

- 变量与可变性

rust默认创建的变量都是不可改变的，可以用let来重新覆盖此值，但是不能直接修改对象，如果想要修改。需要加mut关键字

``` rust
//默认不可变
let aa=5;
aa=6;//此处会报错不可修改
//可变的量
let mut aa=5;
aa=6;//这样是可以的
//覆盖之前的变量
let aa=5;
let aa=aa+1;//这样是合法的
//创建一个常量
const AA=5;//此值无法修改和覆盖
```



### 标量类型

> 一个单独的值

- 整型

| 长度    | 有符号 | 无符号 |
| ------- | ------ | ------ |
| 8-bit   | i8     | u8     |
| 16-bit  | i16    | u16    |
| 32-bit  | i32    | u32    |
| 64-bit  | i64    | u64    |
| 128-bit | i128   | u128   |
| arch    | isize  | usize  |



- 浮点型

rust有两种浮点型，f32（单精度）和f64（双精度），默认为f64



- 数值运算

``` rust
fn main() {
    //加
    let sum= 5+6;
    //减
    let diffrence = 95.5- 4.0;
    //乘
    let pruduct= 2*3;
    //除
    let a=4/2;
    let b=2/3;//等于0
    //取余
    let c=11%10；
}
```



- 布尔类型

``` rust
fn main(){
    let flag=true;
    let t: bool =false;
}
```



- 字符类型

> rust的字符比传统的ascII表要多，一些表情等在rust里都属于字符

``` rust
fn main() {
    let c = 'z';
    let z: char = 'ℤ'; // with explicit type annotation
    let heart_eyed_cat = '😻';
}
```



### 复合类型

- 元组类型

> 把一个或多个其他类型的值组合成一个复合类型。一旦声明，长度不能增大和减少。

``` rust
fn main() {
    let tup: (i32, f64, u8) = (500, 6.4, 1);
}
```



- 数组类型

> 数组中每个元素的类型必须相同。数组长度是固定的

``` rust
#![allow(unused)]
fn main() {
let a: [i32; 5] = [1, 2, 3, 4, 5];
}

```



## 认识所有权

> Rust最为与众不同的特性，让Rust无需垃圾回收即可保障没存安全。



### 所有权规则

1. Rust每个值都有一个所有者。
2. 值在任意时刻有且只有一个所有者。
3. 当所有者离开作用域，这个值将被丢弃。



> 内存在拥有它的变量离开作用域后就被自动释放。

``` rust
fn main() {
    {
        let s = String::from("hello"); // 从此处起，s 是有效的

        // 使用 s
    }                                  // 此作用域已结束，                                      
    // s 不再有效
}

```



- 变量与数据的交互方式（移动）

在Rust中，某个变量的作用域结束后，此变量引用的对内存也会失效，Rust会自动调用drop函数清理掉此堆内存空间。

情况1：

``` rust
fn main(){
    let x=String ::from("hello");
    let y=x;
}
```

x和y都指向了同一块内存空间，而不是复制一块新的内存空间给y。（有点类似于其他语言的“浅拷贝”概念）

``` rust
fn main(){
    let x=String::from("hello");
    let y=x;
    println!("{}", x);//程序报错，x已经被移动。
}
```



但也补完全是浅拷贝，因为当x赋值给y之后，x就已经失效了，如果后面再使用到x变量，rust会报错。（一种移动的概念）当x作用域失效之后，不会调用drop清理x指向的空间，当y失效后，才会自动调用drop清理。

> 在rust中，引用的传递只会复制栈中的数据，而存储在堆中的数据不会复制，默认的一些标量类型的值就存储在栈中，所以可以直接让引用传递来复制值。

``` rust
fn main(){
    let x=5;
    let y=x;
    println!("{},{}", x,y);//这样是合法的
}
```

Rust 有一个叫做 `Copy` trait 的特殊注解，可以用在类似整型这样的存储在栈上的类型上（[第十章](https://kaisery.github.io/trpl-zh-cn/ch10-00-generics.html)将会详细讲解 trait）。如果一个类型实现了 `Copy` trait，那么一个旧的变量在将其赋值给其他变量后仍然可用。

Rust 不允许自身或其任何部分实现了 `Drop` trait 的类型使用 `Copy` trait。如果我们对其值离开作用域时需要特殊处理的类型使用 `Copy` 注解，将会出现一个编译时错误。要学习如何为你的类型添加 `Copy` 注解以实现该 trait，请阅读附录 C 中的 [“可派生的 trait”](https://kaisery.github.io/trpl-zh-cn/appendix-03-derivable-traits.html)。

``` rust
fn main() {
    let s = String::from("hello");  // s 进入作用域

    takes_ownership(s);             // s 的值移动到函数里 ...
                                    // ... 所以到这里不再有效

    let x = 5;                      // x 进入作用域

    makes_copy(x);                  // x 应该移动函数里，
                                    // 但 i32 是 Copy 的，
                                    // 所以在后面可继续使用 x

} // 这里, x 先移出了作用域，然后是 s。但因为 s 的值已被移走，
  // 没有特殊之处

fn takes_ownership(some_string: String) { // some_string 进入作用域
    println!("{}", some_string);
} // 这里，some_string 移出作用域并调用 `drop` 方法。
  // 占用的内存被释放

fn makes_copy(some_integer: i32) { // some_integer 进入作用域
    println!("{}", some_integer);
} // 这里，some_integer 移出作用域。没有特殊之处

```



- 总结

将值赋值给另一个对象时移动它，当持有堆中数据值的变量离开作用域时，其值将通过drop被清理掉，除非数据被移动为另一个变量所有。



### 引用与借用

- 产生数据竞争的条件

1. 多个指针同时访问同一个数据。
2. 至少有一个指针被用来写入数据。
3. 没有同步数据访问的机制。



- 引用

``` rust
fn main(){
    let str=String::from("hello");
    let len=aab(&str);//把这个值按引用的形式传递给aab函数。
    println!("{},{}",str,len);
}

fn aab (s: &String)->usize{
    return s.len();//可以得到这个值，但是不能修改
}
```



可以在不改变所有权的情况下得到某个对象，但是不用修改这个对象。

- 可变引用

``` rust
fn main() {
    let mut s = String::from("hello");

    let r1 = &s; // 没问题
    let r2 = &s; // 没问题
    println!("{} and {}", r1, r2);
    // 此位置之后 r1 和 r2 不再使用

    let r3 = &mut s; // 没问题
    println!("{}", r3);
}

```

一个变量不能同时拥有可变引用和普通引用（也不能同时拥有两个可变引用），上面的例子是当普通引用被释放后，才创建的可变引用。



## 使用结构体组织相关联的数据

