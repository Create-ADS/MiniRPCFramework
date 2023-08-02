# MiniRPCFramework

## 语言选择
[English](README.md) | [中文](README_CN.md)

## 介绍

这是一个非常有趣的RPC远程调用框架，但虽然已经可用但仍处于没有beta版本的阶段。

当你打开这个项目的测试Demo时，你会发现一些非常逆天的调用方式，你会为对象的奇怪传递方式和序列化感到惊讶的。

## Demo
### 项目结构
```
minirpc-demo
├── README.md
├── pom.xml
└── src
    └── main
        ├── java
        │   └── online
        │       └── pigeonshouse
        │           └── minirpc
        │               └── demo
        │                   ├── ClientStartTest.java
        │                   ├── ServerStartTest.java
        │                   └── service
        │                       ├── impl
        │                       │   └── HelloService.java
        │                       ├── Hello.java
        │                       └── Test.java
        └── resources
```
`ClientStartTest.java` 是一个测试客户端启动类，内部演示了动态调用和静态调用。

`ServerStartTest.java` 是一个测试服务端启动类，启动时会注册`online.pigeonshouse.minirpc.demo.service.impl`包下的所有服务。

`Hello.java` 是静态接口类，客户端调用时标记了服务、版本、组、方法等信息。

`HelloService.java` 是静态服务类，同样也标记了服务、版本、组、方法等信息。

`Test.java` 在这个demo中是客户端和服务端共享的类，不过这个类中的代码有些误导性，因为当这个类在客户端和服务端上同名但内容不同时，它不需要继承`MiniObject`类。