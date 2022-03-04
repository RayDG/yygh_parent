# 医院预约挂号平台

## 项目介绍

`yygh-parent`项目是一套网上预约挂号系统，采用了 Spring Cloud Alibaba、Spring Boot、MyBatis、Redis、RabbitMQ 等核心技术，同时提供了基于 vue- element-admin 的后台管理系统。网上预约挂号是近年来开展的一项便民就医服务，旨在缓解看病难、挂号难的就医难题，许多患者为看一次病要跑很多次医院，最终还不一定能保证看得上医生。网上预约挂号全面提供的预约挂号业务从根本上解决了这一就医难题。随时随地轻松挂号！不用排长队！

## 系统架构图

![尚医通架构图](https://cdn.jsdelivr.net/gh/RayDG/dgImg/img/202203042046978.png)

## 业务流程图

![尚医通业务流程](https://cdn.jsdelivr.net/gh/RayDG/dgImg/img/202203042132451.png)

## 组织结构

```
yygh-parent
├── yygh-parent-common -- 工具类及通用代码模块
├── yygh-parent-model -- 实体类模块
├── yygh-parent-service -- 业务模块
	├── service-user -- 用户模块
	├── service-cmn -- 数据字典模块
	├── service-hosp -- 数据字典模块
	├── service-msm -- 短信/邮件模块
	├── service-order -- 订单模块
	├── service-oss -- 对象存储模块
	├── service-user -- 对象存储模块
	├── service-statistics -- 统计图表模块
├── yygh-parent-service-client -- 远程调用模块
├── yygh-parent-gateway -- 基于Spring Cloud Gateway的微服务API网关服务
```

## 技术选型

### 后端技术

| 技术                 | 说明             | 官网                                            |
| -------------------- | ---------------- | ----------------------------------------------- |
| Spring Cloud         | 微服务框架       | https://spring.io/projects/spring-cloud         |
| Spring Cloud Alibaba | 微服务框架       | https://github.com/alibaba/spring-cloud-alibaba |
| Spring Boot          | 容器+MVC 框架    | https://spring.io/projects/spring-boot          |
| MySQL                | 关系型数据库     | https://www.mysql.com/                          |
| MyBatis              | ORM 框架         | http://www.mybatis.org/mybatis-3/zh/index.html  |
| MyBatis-Plus         | ORM 框架         | https://baomidou.com/                           |
| RabbitMq             | 消息队列         | https://www.rabbitmq.com/                       |
| Redis                | 分布式缓存       | https://redis.io/                               |
| MongoDb              | NoSQL 数据库     | https://www.mongodb.com/                        |
| Docker               | 应用容器引擎     | https://www.docker.com/                         |
| OSS                  | 对象存储         | https://github.com/aliyun/aliyun-oss-java-sdk   |
| JWT                  | JWT 登录支持     | https://github.com/jwtk/jjwt                    |
| Lombok               | 简化对象封装工具 | https://github.com/rzwitserloot/lombok          |
| Swagger-UI           | 文档生成工具     | https://github.com/swagger-api/swagger-ui       |

### 前端技术

| 技术       | 说明                | 官网                                                  |
| ---------- | ------------------- | ----------------------------------------------------- |
| Vue        | 前端框架            | https://vuejs.org/                                    |
| Vue-router | 路由框架            | https://router.vuejs.org/                             |
| Element    | 前端 UI 框架        | [https://element.eleme.io](https://element.eleme.io/) |
| Axios      | 前端 HTTP 框架      | https://github.com/axios/axios                        |
| Echarts    | 图表框架            | https://echarts.apache.org/zh/index.html              |
| Node.js    | JavaScript 运行环境 | https://nodejs.org/en/                                |
| Webpack    | 模块打包器          | https://webpack.docschina.org/                        |