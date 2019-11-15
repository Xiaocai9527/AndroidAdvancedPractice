# Android打印调用栈方法

## 1.全面的调用栈,类似程序崩溃log

```java
//利用三个参数的android sdk自带的Log函数
Log.e(TAG, "xxxxxxx", new Throwable("log"));
```

## 2.简洁的源码调用顺序,参考陈小缘博客,不过感觉并没有方法1来的简单

```java
StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            Log.i("computeScroll", String.format(Locale.getDefault(), "%s----->%s\tline: %d",
                    element.getClassName(), element.getMethodName(), element.getLineNumber()));
        }
————————————————
版权声明：本文为CSDN博主「陈小缘」的原创文章，遵循 CC 4.0 BY 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u011387817/article/details/80313184
```

