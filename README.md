# Bookshelf
simple webapp developed with java, spring boot family, wicket, H2database, Flyway

# Bookshelf REST APIs
Rest api will be started with Spring Boot under http://localhost:4040/api/
Please see REST APIs Doc for Details under doc/index.html

# Bookshelf Webapp
Wicket Webapp will be started under http://localhost:8080/bookshelf

# How to run
```console
{ProjectRoot}/mvn clean install
```

```console
{ProjectRoot}/bookshelf_api/mvn clean package exec:java
```

```console
{ProjectRoot}/bookshelf_web/mvn clean tomcat7:run
```
