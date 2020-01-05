# Android问题记录

## 1.No toolchains found in the NDK toolchains folder for ABI with prefix

答：发现一个更快的，下载的文件大小就1KB，所以尝试了一下，直接添加
toolchains\mips64el-linux-android-4.9\prebuilt\darwin-x86_64
这个层级目录就行了

参考：https://www.jianshu.com/p/fd3d49c7f1f8

## 2.git clone下来的工程不是As工程，其子目录里才是一个As工程，此时打开子目录的As工程是没有git关联？

答：打开settings，点击Version Control，此时可以看到右边有个git关联，点击+号添加即可。

## 3.h264花屏解决方法

增加帧分解符判断
byte[] tmp = new byte[input.length + 5];
System.arraycopy(input, 0, tmp, 0,input.length);
tmp[tmp.length - 1] = 0x09;
tmp[tmp.length - 2] = 0x01;
tmp[tmp.length - 3] = 0x00;
tmp[tmp.length - 4] = 0x00;
tmp[tmp.length - 5] = 0x00;
https://blog.csdn.net/xiaolei251990/article/details/82782976

## 4.android录音并储存到文件时，需要添加录音权限和文件权限

不然会出现文件创建失败，以及startRecording() called on an uninitialized AudioRecord.等问题

## 5.aapt.exe finished with non-zero exit value 1

两种方式定位问题，一般都是资源文件出现了问题。

a.

![1563364936437](C:\Users\83849\AppData\Roaming\Typora\typora-user-images\1563364936437.png)

点击这个按钮可以查看具体错误信息。

b.在as的终端上运行 gradlew processDebugResources --debug，查看错误日志。

### 6.ndk常见错误

fatal signal 4： 常见情况是方法没有返回值，比如一个返回int的方法，到最后没有return ret。
fatal signal 6：常见情况是pthread 线程类阻塞了，比如重复调用join方法，或者一个线程detach之后，然后又调用join就会出现这种情况
fatal signal 11：空指针出错。在多线程环境下，由于对象在另外一个线程释放调用，而该线程并没有停止，仍然在运行阶段，此时调用到该被释放的对象时，就会出现fatal signal 11 的错误。

### 7.Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6

ctrl+shift+A，全局搜索Kotlin Compiler ，设置target jvm version 为1.8，然后gradle重新构建工程。

### 8.AGPBI: {"kind":"error","text":"Program type already present: cn.tee3.avd.AVDEngine$5","sources":[{}],"tool":"D8"}

clean一下

### 9.Minimum supported Gradle version is 5.4.1.

两种可能：

1.本机项目中的gradle版本比较低，修改一下gradle.properties文件中的url地址

2.本机设置的gradle环境变量，版本地较低，重置设置一下新版本即可。

### 10.Android项目中manifest中的package和build中的application id区别

package应用于源代码和R类中

applicationId作用应用的唯一标识包名

### 11.JNI DETECTED ERROR IN APPLICATION: use of deleted weak global reference 0xb21519fb

### 12.JNI DETECTED ERROR IN APPLICATION: use of invalid jobject

 记一下jni环境下使用得数据类型必须是jni得数据类型,不能直接传入msg，需要转换成jstring类型，否则会报JNI DETECTED ERROR IN APPLICATION: use of invalid jobject错误