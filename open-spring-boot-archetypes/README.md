## 使用脚手架创建工程
web工程
```
mvn archetype:generate                                  \
  -DarchetypeCatalog=internal                           \
  -DarchetypeGroupId=org.hongxi.spring.boot             \
  -DarchetypeArtifactId=web-spring-boot-archetype      \
  -DarchetypeVersion=1.0.0-SNAPSHOT
```
dubbo工程
```
mvn archetype:generate                                  \
  -DarchetypeCatalog=internal                           \
  -DarchetypeGroupId=org.hongxi.spring.boot             \
  -DarchetypeArtifactId=dubbo-spring-boot-archetype      \
  -DarchetypeVersion=1.0.0-SNAPSHOT
```

可以开发一个web工程，在页面上输入相关参数，调用后端接口，接口逻辑为调用上述脚本生成项目，然后压缩输出到页面（下载）。