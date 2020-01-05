# ndk 开发问题

##### 1.改了 setting.gradle 中的 rootProject.name = 'FFmpegSummary'

##### 2.改了 CMakeLists.txt 中的 jniLibs 路径，cmake 设置 jniLibs 路径时，不能使用相对路径

##### 3.需要在 application 模块的 build.gradle 文件中，添加 ndk 过滤

##### 4.退出线程调用 pthread_exit(&decodeThread);

##### 5.创建线程 pthread_create(&decodeThread, NULL, onPrepareCallback, (void *) url)，第四个参数是一个空类型指针

##### 6.LOGD("jobj地址 %p",jobj); 打印对象内存地址

##### 7.创建全局引用，jobj = env->NewGlobalRef(thiz)，可以跨多个线程

