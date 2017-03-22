## 科普知识
#### ThreadFactory
>在JAVA中，处理并发问题，使用工厂模式的例子很常见。ThreadFactory是线程工厂，用于生产线程

>工厂模式是最常用的模式之一，在创建线程的时候，我们当然也能使用工厂模式来生产Thread，这样就能替代默
 认的new Thread，而且在自定义工厂里面，我们能创建自定义化的Thread，并且计数，或则限制创建Thread的数量,给每个Thread设置对应的好听的名字，或则其他的很多很多事情

#### SecurityManager
>java.lang.SecurityManager.getThreadGroup()方法返回的线程组到其中的任何新的线程被创建的时候，这是被称为实例化。默认情况下，它返回当前线程的线程组。这应该是覆盖特定的安全管理器返回适当的线程组

#### ThreadGroup
>在Java中每一个线程都归属于某个线程组管理的一员，例如在主函数main()主工作流程中产生一个线程，则产生的线程属于main这个线程组管理的一员。简单地说，线程组就是由线程组成的管理线程的类，这个类是java.lang.ThreadGroup类。
可以通过使用如下代码获取此线程所属线程组的名称。
```Thread.currentThread().getThreadGroup().getName();
currentThread()：取得当前线程。
getThreadGroup()：取得当前线程所在的组。
getName()：取得组的名称。
```
定义一个线程组，通过以下代码可以实现
```
ThreadGroup group=new ThreadGroup("group");
Thread thread=new Thread(group,"the first thread of group");
```
ThreadGroup类中的某些方法，可以对线程组中的线程产生作用。例如，setMaxPriority()方法可以设定线程组中的所有线程拥有最大的优先权

#### ThreadPoolExecutor
``自定义线程池``
```
new ThreadPoolExecutor(int corePoolSize, int maximumPoolSize,long keepAliveTime, TimeUnit unit,BlockingQueue<Runnable> workQueue,RejectedExecutionHandler handler)
```
>corePoolSize： 线程池维护线程的最少数量

>maximumPoolSize：线程池维护线程的最大数量

>keepAliveTime： 线程池维护线程所允许的空闲时间

>unit： 线程池维护线程所允许的空闲时间的单位

>workQueue： 线程池所使用的缓冲队列

>handler： 线程池对拒绝任务的处理策略

 ``ThreadPoolExecutor是ExecutorService的子类``

```
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
}
```
``ExecutorService``
```
ExecutorService pool = Executors. newSingleThreadExecutor()
pool.execute(t1)
```


* newSingleThreadExecutor
>创建一个单线程的线程池。这个线程池只有一个线程在工作，也就是相当于单线程串行执行所有任务。如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它。此线程池保证所有任务的执行顺序按照任务的提交顺序执行

* newFixedThreadPool
>创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程

* newCachedThreadPool
>创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，
 那么就会回收部分空闲（60秒不执行任务）的线程，当任务数增加时，此线程池又可以智能的添加新线程来处理任务。此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小

* newScheduledThreadPool
>创建一个大小无限的线程池。此线程池支持定时以及周期性执行任务的需求

#### 阻塞队列（BlockingQueue)
>一个支持两个附加操作的队列

>这两个附加的操作是:在队列为空时,获取元素的线程会等待队列变为非空

>当队列满时,存储元素的线程会等待队列可用

>阻塞队列常用于生产者和消费者的场景:生产者是往队列里添加元素的线程，消费者是从队列里拿元素的线程。阻塞队列就是生产者存放元素的容器，而消费者也只从容器里拿元素

#### 图片分析
![](http://a3.qpic.cn/psb?/V13qMjpA1HNCKd/*kW6Pu6RoeycHfLJisBM9gHGJOkijRRE1f495xK7MjE!/b/dB8BAAAAAAAA&bo=SgNaAQAAAAAFBzY!&rf=viewer_4)