# 认识nqcx-commons
  nqcx-commons是老黄日常累积的一些通用工具类
## 模块
  nqcx-commons-3rd
  nqcx-commons-dao
  nqcx-commons-lang
  nqcx-commons-mq
  nqcx-commons-service
  nqcx-commons-solr
  nqcx-commons-util
  nqcx-commons-web
  nqcx-commons-zk
***
Maven用法：

将以下依赖加入工程的pom.xml中的<dependencies>...</dependencies>部分。
    <dependency>
        <groupId>org.wltea.ik-analyzer</groupId>
        <artifactId>ik-analyzer</artifactId>
        <version>3.2.8</version>
	</dependency>

在IK Analyzer加入Maven Central Repository之前，你需要手动安装，安装到本地的repository，或者上传到自己的Maven repository服务器上。

要安装到本地Maven repository，使用如下命令，将自动编译，打包并安装：
mvn install -Dmaven.test.skip=true
