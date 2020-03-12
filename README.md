# JavaSE 手写 多线程web 容器
-------------------
3.12号更新 较大改动
将原来的bio更新成nio模式
-------------------
## 源码出处及见解:

https://www.cnblogs.com/moonlightL/p/7727373.html

https://www.cnblogs.com/moonlightL/p/7727379.html



对其中的多线程进行修改 能符合阿里p3c规则



心得:

###1.通过 Socket API 编写服务端 
Server.java

###2.封装请求对象Requset 

Requset.java

###3.封装相应对象Response

Response.java

###4.根据不同的请求 url 去触发不同的业务逻辑 重点(参考网址2)

XML：将配置信息写到 XML 文件，解决硬编码问题。

创建xml文件 并创建了:

ServletContext.java  能更好的管理多个控制器实例以及请求 url 与控制器的对应关系，我们需要一个类对其封装和管理。

ServletXml.java   处理servlet-name与servlet-class关系 , 

ServletMappingXml.java 处理servlet-name与url-pattern关系



反射：读取 XML 文件配置并实例化对象。

WebApp.java 虽然有了 web 容器上下文，但是 web 容器上下文并不是一开始就存放配置信息的。配置信息在 web 容器启动时被注册或写入到上下文中，因此需要一个管理配置的类负责该操作. 

WebHandler.java 创建 xml 文件解析器，用于解析 web.xml 配置文件

Dispatcher.java多线程run方法重写,，从 WebApp 类中获取信息通过反射实例化对象!!!

### 5.封装控制器

目前手写的 web 容器只能处理一种业务请求，无论发送什么 url 的请求都是只返回一个内容相似的页面。

为了能很好的扩展 web 容器处理不同请求的功能，我们需要将 request 和 response 封装到控制器，让各个业务的控制器处理对应请求和响应。
Servlet.java

### 6.抽离控制器
LoginServlet.java
父类控制器：Servlet
父类中使用了模板方法的设计模式，将业务处理的行为交给子类去处理。
当我们需要一个控制器去处理登陆请求时，我们创建一个 LoginServlet 类去继承 Servlet 并重写 doGet 或 doPost 方法：LoginServlet extends Servlet


### 7.多线程操作
ThreadPoolManager.java 线程池管理
Dispatcher.java 线程run重新







