```
[不翻译]down vote I would say, that both of these formats has their own specific advantages. Parquet might be better if you have highly nested data, because it stores its elements as a tree like Google Dremel does (See here).
Apache ORC might be better if your filestructure is flatter.
And as far as I know parquet does not support Indexes yet. ORC comes with a light weight Index and since Hive 0.14 an additional Bloom Filter which might be the issue for the better query speed especially when it comes to sum operations.
The Parquet default compression is SNAPPY. Are Table A - B - C and D holding the same Dataset. If yes it looks like there is something shady about it, when it only compresses it to 1.9 GB.
```
```
只有TEXTFILE表能直接加载数据，必须，本地load数据，和external外部表直接加载运路径数据，都只能用TEXTFILE表。
  更深一步，hive默认支持的压缩文件（hadoop默认支持的压缩格式），也只能用TEXTFILE表直接读取。其他格式不行。可以通过TEXTFILE表加载后insert到其他表中。

  换句话说，SequenceFile、RCFile表不能直接加载数据，数据要先导入到textfile表，再从textfile表通过insert select from 导入到SequenceFile,RCFile表。
  SequenceFile、RCFile表的源文件不能直接查看，在hive中用select看。RCFile源文件可以用 hive --service rcfilecat /xxxxxxxxxxxxxxxxxxxxxxxxxxx/000000_0查看，但是格式不同，很乱。

```