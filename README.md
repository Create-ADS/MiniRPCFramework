# MiniRPCFramework

## Language selection
[English](README.md) | [中文](README_CN.md)

## Introduction

This is a very interesting RPC remote call framework, but although it is already available, it is still in a stage without even a beta.

When you open the client test for this project, you'll find some very interesting invocation methods. Feel free to take a closer look.

## Demo
### Project Structure

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

`ClientStartTest.java` is a test client start class that demonstrates dynamic invocation and static invocation internally.

`ServerStartTest.java` is a test server start class. When starting, it will register all services under the `online.pigeonshouse.minirpc.demo.service.impl` package.

`Hello.java` is the static interface class called by the client, which marks the service, version, group, methods and other information on the server side.

`HelloService.java` is the static service class, which also marks service, version, group, methods and other information.

`Test.java` in this demo is a class shared by client and server. The code in this class is somewhat misleading, because when this class has the same name but different content on the client and server side, it does not need to extend `MiniObject` class.