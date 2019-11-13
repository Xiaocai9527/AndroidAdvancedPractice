# Android导出anr的trace文件

如下步骤：

```bash
1.adb pull data/anr/trace.txt    拉取到当前目录

如果出现下面错误，则执行2

> adb: error: cannot create file/directory 'e:/': No such file or directory

2.adb bugreport    此操作会拉取一个zip压缩文件到当前目录，解压后在FS文件夹找trace文件

> 可以通过adb shell -> cd data/anr -> ls -a   查看anr的目录文件

这个时候使用adb pull data/anr/anr_xxxxxx 有可能会报
> adb: error: failed to copy 'data/anr/anr_2019-11-13-10-54-47-712' to '.\anr_2019-11-13-10-54-47-712': remote open failed: Permission denied


```

