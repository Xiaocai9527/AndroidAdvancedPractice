# App实战：移动端Mock Api的几种方式

相信做Android的朋友在开发中一定会碰到这样的痛苦：页面写完了,后台接口还没开发完成。这个时候就需要我们自己来mock api了。下面我介绍几种我用到的方式：

## 一·玩Android提供的mock api

很多人应该知道鸿洋做了一个网站叫<a href="http://www.wanandroid.com/">玩Android</a>，但是可能有的朋友不知道鸿洋还提供了一个很实用的功能那就是mock api。以前在首页有介绍，貌似是因为有人利用这个做一些不法的事情，所以入口被取消掉了。但是只是工具类上的入口被屏蔽了，网页路由还是在的。<a href="http://www.wanandroid.com/tools/mockapi">点击进入mock</a>，因为涉及到链接更新和删除，所以这里需要登陆。我在自己平时的练习项目中是经常使用这个，具体参考代码<a href ="https://github.com/xiaokun19931126/AndroidAdvancedPractice">AndroidAdvancedPractice</a>。mock过程中也非常简单,如下所示：

![](..\pictures\1545742847858.png)

![1545743319381](..\pictures\1545743319381.png)

## 二·EasyMock

这是一个网站,登陆之后你可以创建项目,据说还可以自动创建swagger文档,这个我没有深入使用。但是我想如果只是简单的mock数据,这个网站是绝对满足得了的。

![1545743217931](..\pictures\1545743217931.png)

![1545743404596](..\pictures\1545743404596.png)

### 三·利用node.js来启动服务

这个是我以前看到一个开源项目,上手之后发现真的是非常简单,而且有一定的bigger在的哦。<a href="https://github.com/heimashi/easy_mock_api">项目地址easy_mock_api</a>,致敬作者。很多Android开发者看到node.js就怕了,没必要,接下来我列出所有步骤：

* 如果没有安装node.js,<a href="https://nodejs.org/en/">点击下载node.js</a>。

* 用git将项目clone下来,git clone https://github.com/heimashi/easy_mock_api.git

* 打开cmd,cd到当前项目,比如我的是E:\code\easy_mock_api

* 运行npm install自动安装完成后,运行npm start

* 完成。电脑端可以直接访问http://localhost:3000/,同一局域网下将当前电脑的ip地址替换localhost后移动端也可以访问。

* 如果嫌麻烦的话还可以写一个bat文件,快速启动服务

   1.新建一个txt文件,复制一下内容：

  ​    e:
  ​    cd E:\code\easy_mock_api
  ​    npm start

  2.重命名文件后缀名为bat,点击即可开启server服务。

  ![1545745009763](..\pictures\1545745009763.png)


![1545745606751](..\pictures\1545745606751.png)



**注意如果出现了Couldn't find preset "es2015"错误,那么需要cd到当前目录下执行**

**npm install babel-preset-es2015**

### 四·利用AndServer项目

这个项目真的很nb,想开发Spring一样在Android项目上开发后台。这个能做的事情比上面那些mock要多很多很多,比如我想模拟上传文件和上传文件等,都可以通过这个项目来实现。这个项目我没必要多讲的,作者写了非常完善的文档,是真的nb得雅痞。<a href="https://github.com/yanzhenjie/AndServer">AndServer项目地址</a>,当然如果你能结合我的代码来看的话也是非常好的,我的<a href="https://github.com/xiaokun19931126/AndroidAdvancedPractice">AndroidAdvancedPractice项目</a>里面有下载和上传的示例,其中包含下载进度上传进度和断点续传等功能。也可以参考我之前写的文章<a href="https://blog.csdn.net/qq_34184412/article/details/80045637">App网络请求实战三：下载文件以及断点续载</a>。

### 致谢

<a href="http://www.wanandroid.com/">玩Android</a>

<a href="https://easy-mock.com/">Easy mock</a>

<a href="https://github.com/heimashi/easy_mock_api">easy_mock_api</a>

<a href="https://github.com/yanzhenjie/AndServer">AndServer</a>



上篇博客：<a href="https://blog.csdn.net/qq_34184412/article/details/80849094">多类型列表写法(RecyclerView)</a>







