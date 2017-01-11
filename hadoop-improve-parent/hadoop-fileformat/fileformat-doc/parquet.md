``业界公认HDFS是最佳的分布式文件存储最佳系统标准，而Parquet是最佳文件存储格式标准``
### 一：Spark SQL下的Parquet意义
> 存储空间（包括磁盘和内存）的成本，还有计算的成本，Parquet会降低7成。

**(1)** 速度更快：性能测试表明，从使用Spark SQL操作普通文件CSV和Parquet文件的速度对比来看，基本上绝大多数情况下，使用Parquet速度提升10倍左右（在一些普通文件系统无法在Spark上成功运行程序的情况下，使用Parquet很多时候都可以成功运行）

**(2)** Parquet的压缩技术非常稳定出色，在Spark SQL中对压缩技术的处理可能无法正常的完成工作（例如会导致Lost Task，Lost Executor），但是此时如果使用Parquet就可以正常完成

**(3)** 极大的减少磁盘IO，通常情况下能够减少75%的存储空间，由此可以极大的减少Spark SQL处理数据的时候输入数据的量。尤其在Spark 1.6.x中下推过滤器在一些情况下可以极大的进一步减少磁盘的IO和内存的占用

**(4)** Spark 1.6.x＋Parquet极大的提升了数据扫描的吞吐量，这极大的提高了数据的查找速度。Spark1.6和Spark1.5相比而言提升了大约1倍的速度。在Spark1.6.x中操作Parquet时候CPU的使用也进行了极大的优化，有效的降低了CPU的使用

**(5)** 采用Parquet可以极大的优化Spark的调度和执行。我们的测试表明Spark如果采用Parquet可以有效的减少Stage的执行消耗，同时可以优化执行路径

### 二：Spark SQL下的Parquet内幕
**(1)** 日志是非常复杂的数据结构。自定义的一些结构体,日志是一行一行的,但是数据结构有可能设计得比较复杂,嵌套多层的数据结构。列式存储是以什么基本格式存储数据的？
无论Parquet存储的什么数据,都和计算框架解耦合。Parquet表现上是树状数据结构,在内部有元数据的Table

**(2)** 在具体的Parquet文件存储的时候有三个核心组成部分。
> a）Storage Format：Parquet定义了具体的数据内部的类型和存储格式。表明上看不到

> b）Object Model Converters：Parquet中负责计算框架中数据对象和Parquet文件中具体数据类型的映射

> c）Object Models：在Parquet中具有自己的Object Model定义的存储格式,例如说Avro具有自己的Object Model,但是Parquet在处理相关的格式的数据的时候会使用自己的Object Model来存储

映射完成后Parquet会进行自己的Column Encoding,然后存储成为Parquet格式的文件。
``Parquet本身就是一个框架,也算是一个软件,是一个独立的数据结构,不依赖其他任何的数据结构``

```
message AddressBook {
        required string owner;
        repeated string ownerPhoneNumbers;
        repeated group contacts
               { required string name; optional string phoneNumber; }
 }
```

``required(出现1次),optional（出现0次或者1次）,repeated（出现0次或者多次）``

![](http://p1.bqimg.com/567571/3bdca0f50de2d1f1.png)

**第一点**：就数据存储本身而言,只考虑叶子阶段

**第二点**：在逻辑上,Schema实质上是一个Table

![](http://p1.bqimg.com/567571/2556c75bcedb2827.jpg)

**第三点**：对于一个Parquet文件而言,数据会被分成Row Group（里面包含很多Column,每个Column具有几个非常重要的特性,例如Repetition、Definition Level）。

**第四点**：Column在Parquet中是以Page的方式存在的。Page里有Repetition，Definition Level。实际上会划分为矩阵。

**第五点**：``Row Group``在Parquet中是数据读写的缓存单元。所以对``Row Group``的设置会极大的影响Parquet和使用速度和效率。如果是分析日志的话，我们一般建议把Row Group的缓存大小配置成大于256M。很多人的配置都是大于1G。如果想带来最大化的运行效率,强烈建议HDFS的Block大小和Row Group一致。
事实上存储的时候,会将AddressBook正向存储为4列,读取的时候会逆向还原出AddressBook对象

``从根节点往叶子节点遍历的时候,会记录深度的东西,这个就是Definition Level``
> 需要Definition Level是方便我们精准找到数据
```
owner是required,所以将其Definition Level可以定义为0
ownerPhoneNumber节点,其没有叶子节点,定义其1
name节点,是required,定义为1
phoneNumber是optional,有可能出现也有可能不出现,定义为2
```

> Repetition Level：重复级别。
```
owner为0
ownerPhoneNumber为1
name和phoneNumber都是1
```

基于上面的逻辑树,映射成物理结构是非常容易的,其实就是一个基本的Table。

![](http://i1.piimg.com/567571/63c54873fa8bf325.png)

**第六点**：在实际存储的时候把一个树状结构,通过巧妙的编码算法,转换成二维表结构。
