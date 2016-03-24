# framework-core
spring-boot+mybatis+redis+memcache+guava

该框架为了后期分布式扩展和拆分，分为API、SERVICE、APP三个模块，API为后期项目的接口；SERVICE为数据层，主要是简单的增删改；
APP为前端框架。为了前期开发便利，一开始不引入RPC框架，但项目已经加入dubbo和zookeeper的依赖，便于后期转向SOA编程。
