**记录下node.js启动简单服务为移动端服务的小案例：**

学习自<a href="https://github.com/heimashi/easy_mock_api">开源项目</a>

1.如果没有安装node.js,<a href="https://nodejs.org/en/">点击下载node.js</a>

2.用git将项目clone下来,git clone https://github.com/heimashi/easy_mock_api.git

3.打开cmd,cd到当前项目,比如我的是D:\mock_api\easy_mock_api

4.运行npm install自动,安装完成后,运行npm start

5.完成。电脑端可以直接访问http://localhost:3000/,同一局域网下将当前电脑的ip地址替换localhost后移动端也可以访问。

6.快速启动服务可以写一个bat文件

* 新建一个txt文件，复制一下内容：

    d:
  cd D:\mock_api\easy_mock_api
  npm start

* 重命名文件为bat,点击此文件即开启server服务