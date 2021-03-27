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