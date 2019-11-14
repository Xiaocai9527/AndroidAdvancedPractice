# 如何多jar合并

做sdk开发中，jar包合并是很常见事情，记录一下jar包合并方法。

## 1.首先需要下载apache-ant-1.9.14-bin.zip

谷歌搜索即可下载

## 2.设置环境变量

注意bin和lib目录都设置，省略如何设置环境变量

## 3.检查环境变量是否设置成功

打开cmd，输入ant回车，提示Buildfile: build.xml does not exist!表示环境变量设置成功

## 4.编写build.xml文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<project
    name="full"
    basedir="./"
    default="makeSuperJar" >

    <target
        name="makeSuperJar"
        description="description" >

        <jar destfile="full.jar" >
            <zipfileset src="single1.jar" />
            <zipfileset src="single2.jar" />
        </jar>
    </target>

</project>
```

## 5.合并jar包

如上所示，single1.jar和single2.jar两个jar包和build需要在同一个目录下，cmd到当前目录

```gas
ant -buildfile build.xml
```

最终会在当前目录下输出full.jar合并后的jar包