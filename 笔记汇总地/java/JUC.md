# 读《并发编程的艺术》有感

# 并发编程的挑战

## 上下文切换

在并发量不超过百万次时，并行速度要比串行慢，因为线程有创建和上下文切换的开销

- 如何减少上下文切换

1. 无锁并发编程：多线程竞争锁时，会引起上下文切换，所以多线程处理数据时，可以用一些办法来避免使用锁，如将数据的ID按照Hash算法取模分段，不同的线程处理不同段的数据。
2. CAS算法，java的Atomic包使用CAS来更新数据，而不是加锁
3. 使用最少线程。避免创建不需要的线程，比如任务很少，但是创建了很多线程来处理，这样会造成大量线程都处于等待状态
4. 协程：在单线程里实现多任务的调度，并在单线程里维持多个任务的切换

- 避免死锁的方法

1. 避免一个线程同时获取多个锁
2. 避免一个线程在锁内同时占用多个资源，尽量保证每个线程只占用一个资源
3. 尝试使用定时锁，使用lock.tryLock(timeout)来代替使用内部锁机制。
4. 对于数据库锁，加锁和解锁必须在同一个数据库连接里，否则会出现解锁失败的情况。

- 资源限制引发的问题
  在并发编程中，将代码速度执行变快的原则是讲串行的部分改成并行，但是如果将某段串行的代码并发执行，因为受限于资源，仍然在串行执行，程序不仅不会加快，甚至会变慢，因为产生了上下文切换和资源调度的时间。

- 如何解决资源受限的问题
  如果是硬件资源受限，可以考虑使用集群并行执行程序。既然单机执行受限，那么就让程序在多机上运行。
  如果软件资源受限，那么可以考虑使用资源池将资源复用。比如使用连接池来使数据库和Socket连接复用，或者在调用对方web-Service接口时，只建立一个连接

# java并发机制的底层实现原理

## volatile

> volatile是轻量级的synchronized，它在多处理器并发中保证了共享变量的“可见性”。（它不会引起线程上下文的切换和调度）
> volatile对单个变量的读写具有原子性，但类似于volatile++这类复合操作不具有原子性。

- volatile的原理
  对volatile修饰的变量进行写操作时，会多加一条Lock前缀的指令，将当前处理器缓存行的数据写回到系统内存，这个写回内存的操作会导致其他CPU里缓存了该内存地址的数据无效。

- volatile的优化
  64位的CPU会一次读取64个字节的数据，所以可以通过追加字节来保证一次读取只会读到一个共享变量，来保证多个cpu同时操作不会互相锁定。
  JDK7会自动优化去除无用的对象引用，JDK8提供了@Contended注解来实现套接字

## synchronized

> JVM基于进入和退出Monitor对象来实现方法同步和代码块同步。

- Mark Word的几种情况
  | 锁状态 | 是否是偏向锁 | 锁标志位 |
  | 无锁 | 0 | 01 |
  | 偏向锁 | 1 | 01 |
  | 轻量级锁 | 0 | 00 |
  | 重量级锁 | 0 | 10 |
  | GC | 0 | 11 |

- 偏向锁
  偏向锁会在程序启动几秒后才会开启，可以通过JVM参数设置来关闭延迟。当加锁代码被执行时，会获取偏向锁，当偏向锁代码被多个CPU竞争时，会释放偏向锁，升级为轻量级锁。

优点：
加锁和解锁不需要额外的消耗，和串行执行相比只存在纳米级别的差距。
缺点：
如果线程间存在竞争，会带来额外的锁撤销的消耗
场景：
只有一个线程访问同步块的场景。

- 轻量级锁
  同步块执行时，JVM会在当前线程的栈帧中创建一块用于存储锁记录的空间，并把MarkWord复制到里面，线程会使用CAS尝试将对象头中的MarkWord替换为指向锁记录的指针。如果成功，当前线程获得锁，如果失败会产生竞争，通过自旋来尝试获得锁。
  轻量级锁解锁的时候，会用CAS操作把MarkWord替换回对象头，如果成功，表示没有竞争。如果失败，表示当前锁存在竞争，锁就会膨胀为重量级锁。

优点：
竞争的线程不会阻塞，提高了程序的响应速度
缺点：
如果始终得不到锁竞争的线程，使用自旋会消耗CPU
场景：
追求响应时间，同步块执行速度非常快。

- 重量级锁
  优点：
  线程竞争不使用自旋，不会消耗CPU
  缺点：
  线程阻塞，响应时间缓慢
  场景：
  追求吞吐量，响应时间缓慢

- 院子操作的锁定
  Lock前缀指令，在新的CPU中不再加总线锁（总线锁会导致其他CPU无法获取到新的指令），而是采用缓存锁的形式来加锁，如果修改了缓存行的数据，其他线程读取了缓存行的数据，其他CPU读取的数据会失效，让他们去重新读取缓存行。如果被锁定的缓存行被修改，多个CPU就不能同时缓存此缓存行。

- 两种不会使用缓存锁定的情况
  1.如果被锁定的数据不能被缓存到处理内部或者操作的数据跨多个缓存行，CPU会采用总线锁。
  2.有些CPU不支持缓存锁定。

- CAS三大问题
  1.ABA问题：给数据加版本号
  2.循环时长，开销大：给每次自旋循环加一个等待时间。
  3.多个对象被CAS，无法保证一个对象同时CAS所有对象：可以将对象封装成一个对象，一次CAS完成所有操作。（类似数据库事务）

# 线程：

线程的状态：
初始化（NEW）,运行状态（RUNABLE），阻塞状态（blocked），等待状态（waiting），超时等待状态（time waiting），终止状态。

- 初始化（NEW）不占用CPU
  进入运行指令：Thread.start()

- 运行（RUNNING 和READY）占用CPU
  java将操作系统中的运行和就绪合并称为运行状态。

- 等待（WAITING）不占用CPU
  需要其他线程通知才能返回运行状态，阻塞在Lock接口的线程状态是等待状态，因为Lock接口对于阻塞的实现均使用了LockSupport类中的相关方法。
  进入：Object.wait() ,Object.jion(), LockSupport.park()
  离开：Object.notify(), Object.notifyAll(), LockSupport.unpark(Thread)

- 超时等待(TIMED_WAITING) 不占用CPU
  在等待的状态上加了超时限制，达到超时时间自动返回运行状态
  进入：Thread.sleep(long), Object.wait(long), Thread.jion(long), LockSupport.parkNanos(),LockSupport.parkUntil()
  离开：Object.notify(), Object.notifyAll(), LockSupport.unpark(Thread), 超时时间到

- 阻塞(BLOCKED)：不占用CPU
  当线程调用同步方法时，在没有获得锁的情况下，线程就会进入阻塞状态。
  进入：等待进入synchronized方法，等待进入synachronized代码块
  离开：获取到锁。

- 终止（TERMINATED）不占用CPU
  线程执行完成进入终止状态。

- wait,sleep,yield
  wait：会释放CPU资源和锁释放monitor对象，wait只能在synachronized同步环境中调用，wait是针对同步代码块，让某个资源的锁被释放，退出CPU，wait后能够被notify(),和notifyAll()唤醒。
  sleep：会释放CPU但不释放锁不释放monitor对象，sleep是针对线程的静态方法，sleep不能被notify方法唤醒
  yield仅仅是释放CPU，来和其他线程一起争抢CPU。

- 个人用jstack分析分析的线程状态：
  运行：RUNNABLE，备注：runnable
  在等待获取锁的阻塞:BLOCKED，备注：waiting for monitor entry
  调用sleep方法：TIME_WAITING，备注：waiting on condition
  调用wait方法：WAITING，备注：in Object.wait()

- 线程过期的暂停，恢复，停止方法
  暂停不会释放资源，不会释放锁等，只释放CPU进入sleep，停止也可能会导致线程占用的资源没有被正确释放，而强制停止。类似于linux的kill -9

- 如何优雅的关闭线程
  可以通过标识位或者中断操作的方式使线程在终止时有机会去清理资源。

- 等待/通知机制
  细节：
  1.调用wait（），notify(),notifyAll()需要先给对象加锁
  2.调用wait()后，线程由RUNNING变成WAITING并将线程放到对象的等待队列
  3.notify()或notifyAll()方法调用后，等待线程依旧不会从wait()返回，需要调用notify()或notifAll()的线程释放锁之后，等待线程才有机会从wait()返回。
  4.notify()方法将等待队列中的一个等待线程从等待队列中移到同步队列中，而notifyAll()方法则是将等待队列中所有的线程全部移到同步队列，被移动的线程状态由WAITING变为BLOCKED。
  从上述细节中可以看到，等待/通知机制依托于同步机制，其目的就是确保等待线程从wait()方法返回时能够感知到通知线程对变量做出的修改。

- ThreadLocal（可以在单个线程上传递值）
  是一个以ThreadLocal为键，任意对象为值得存储结构。这个结构被附带在线程上，一个线程可以根据一个ThreadLocal查询到绑定在这个线程上的值。

# 队列同步器（AQS）

- 常用的方法：
  getState():获取当前同步状态
  setState（int newState）：设置当前同步状态
  compareAndSetState（int expect,int update）：使用CAS设置当前状态，该方法能够保证设置状态的原子性

- 可重写的方法
  protected boolean tryAcquire（int arg）:独占式获取同步状态，实现该方法需要查询当前状态并判断同步状态是否符合预期
  protected boolean tryRelease（int arg）:独占时释放同步状态，等待获取同步状态的线程将有机会获取同步状态
  protected int tryAcquireShared（int arg）:共享式获取同步状态，返回大于等于0的值，表示获取成功，反之获取失败。
  protected int tryReleaseShared（int arg）:共享式释放同步状态
  protected boolean isHeldExclusively（）:当前同步器是否在独占模式下被线程占用，一般该方法表示是否被当前线程所独占

- 同步器提供的模板方法
  void acquire（int arg）:独占式获取同步状态，如果当前线程获取同步状态成功，则由该方法返回，否则，将进入同步队列等待，该方法将会调用重写的tryAcquire（int arg）方法
  void AcquireInterruptibly（int arg）:与acquire(int arg)相同，但是该方法响应中断，当前线程未获取到同步状态而进入同步队列中，如果当前线程被中断，则该方法会抛出InterruptedException并返回
  boolean tryAcquireNanos（int arg,long nanos）:在AcquireInterruptibly（int arg）基础上增加了超时限制，如果当前线程在超时时间内没有获取到同步状态，就返回false，如果获取到返回true。
  void acquireShared（int arg）:共享式的获取同步状态，如果当前线程未获取到同步状态，将会进入同步队列等待，与独占式获取的主要区别时在同一时间可以有多个线程获取到同步状态
  void acquireSharedInterruptibly（int arg）:该方法响应中断
  boolean tryAcquireSharedNanos(int arg,long nanos)：在acquireSharedInterruptibly（int arg）基础上增加了超时限制
  boolean release(int arg)：独占式释放同步状态，该方法会在释放同步状态之后，将同步队列中第一个节点包含的线程唤醒
  boolean releaseShared(int arg)：共享式的释放同步状态
  Collection<Thread> getQueuedThreads(）:获取等待在同步队列上的线程集合

同步器依赖内部的同步队列（一个FIFO双向队列）来完成同步状态的管理，当前线程获取同步状态失败时，同步器会将当前线程以及等待状态等信息构造成为一个节点（Node）并将其加入同步队列，同时会阻塞当前线程，当同步状态释放时，会把首节点中的线程唤醒，使其再次尝试获取同步状态。

- 独占式同步状态获取和释放：
  在获取同步状态时，同步器维护一个同步队列，获取状态失败的线程都会被加入到队列中进行自旋，移除队列（或停止自旋）的条件是前驱节点是头节点且成功获取了同步状态。在释放同步状态时，同步器调用tryReiease(int arg)方法释放同步状态，然后唤醒头节点的后继节点

- 共享式同步状态获取与释放
  同步器先调用tryAcquireShared(int arg)方法尝试获取同步状态，返回值大于等于0，表示能够获取到同步状态。

- LockSupport
  构建同步组件的基础工具，定义了park（）阻塞当前线程，unpark（Thread thread）唤醒一个被阻塞的线程，lock接口上锁和解锁就是用此工具实现的。

- Condition接口
  Condition是AQS工具的一个内部类，可以实现类似Object的通知/等待模型。await（）当前线程进入等待状态，直到被通知signal或被中断。signal（）唤醒一个等待的线程，signalAll（）唤醒所有等待的线程。

# 并发工具和集合

- ConcurrentLinkedQueue ：非阻塞队列
  由head节点和tail节点组成，入队时的条件，如果tail节点的next节点不为空，则新入队的节点为tail节点。入队时先定位出尾节点，然后使用CAS算法将新入队的节点设置为尾节点的next节点，如果不成功则重试。

## 阻塞队列

阻塞队列java提供了四种处理方式：
抛出异常：当队列满时，再插入元素，会抛出异常，当队列空时，再取出元素会抛出异常
返回特殊值：当插入元素时，如果插入成功，就返回true，取出元素时，如果没有就返回null
一直阻塞：如果生产者线程往队列里put元素，如果队列满，生产者线程会一直处于阻塞状态，知道队列可用或者响应中断退出。take元素，如果队列空，就一直阻塞，直到队列中有元素。
超时退出：当队列满，如果生产者插入元素，队列会阻塞生产者一段时间，如果超出等待时间，生产者线程就退出。

- 七种阻塞队列
  ArarryBlockingQueue:一个由数组组成的有界阻塞队列。
  LinkedBlockingQueue：一个由链表组成的有界阻塞队列。
  PriorityBlockingQueue：一个支持优先级排序的无界阻塞队列。可以实现compareTo（）方法来指定元素排序规则。
  DelayQueue：一个由优先级队列实现的无界阻塞队列。创建的元素指定多久才能从队列中获取到。
  SynchronousQueue：一个存储元素的无界阻塞队列。一个不存储元素的队列，每个put必须等待take，否则无法继续put。
  ·LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。可以进行插队，通过特定api将新插入的元素直接给消费者。
  ·LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。可以从队列的两端来插入和取出元素的队列。

- 阻塞队列的实现
  多数阻塞队列是由Condition实现的，上锁过程由LockSupport的park方法实现，LockSupport调用unsafe的park方法来上锁。

# java中的并发工具类

- CountDownLatch（只能用一次）
  计数器，允许一个或多个线程等待其他线程结束再操作。

- CyclicBarrier（可重复使用）
  一个可循环使用的屏障，当指定的线程都到达屏障后，屏障才会开门，所有被拦截的线程才会继续运行。

- Semaphore（信号量）
  用来控制并发数量。

- Exchager
  线程间数据交换的工具。它提供了一个同步点，当两个线程都到达同步点后，交换数据，线程继续运行。如果一个先到达同步点，会一直等到另一个也到达然后交换再运行。

# 线程池

- 核心线程数:先来的任务由核心线程执行，如果核心线程满，把任务放入等待队列
- 任务队列：存储即将执行的任务，如果满，把任务交给最大线程。
- - ArrayBlockingQueue
- - LinkedBlockingQueue
- - SynchronousQueue
- - PriorityBlockingQueue
- 最大线程数：如果等待队列满，会临时创建一个线程来执行新的任务，如果线程创建数量达到最大值，执行拒绝策略
- 创建线程的工厂
- 拒绝策略
- - 直接抛异常
- - 用调用者所在线程来执行任务
- - 丢掉队列里最近的一个任务，并执行当前任务
- - 不处理，丢弃掉
- 线程的等待时间：如果任务超过等待时间，抛弃任务
- 超时时间的单位



