# git总结

**1.首先 add 需要提交的文件，以一次bug或者feature等为一次提交**

```gas
git add 文件名
```



**2.commit  提交到本地仓库，写清楚提交的信息**

```gas
git commit -m  "提交的信息"
```



**3.fetch和rebase远程公共仓库**

拉取远程公共工程，然后rebase

```gas
git fetch upstream
git rebase upstream/enterprise_wechat
```



**4.push到origin远程仓库**

```gas
git push origin enterprise_wechat
# push reject拒绝的话，可以使用：
git push -f origin enterprise_wechat
```



**5.create merge request到远程公共仓库**

创建合并 request 到 code review 责任人那里



### 场景一：当修改了很多代码，但是本人只想提交其中一小部分代码

**git stash使用**

****

```gas
1.git status 查看状态

2.把不想提交的文件，但是已经add过处于暂存区域，git reset HEAD 取消暂缓

3.git add 想要提交的文件

4.git commit -m "描述"

# 将代码藏起来
5.git stash

# 将upstream远程主机的更新，全部取回本地，此时并没有更新本地代码
6.git fetch upstream

# 在当前分支上，合并upstream/rongshi
7.git rebase upstream/rongshi

# 将本地rongshi分支推送到远程主机origin的分支rongshi上，如果远程不存在，则会被创建
8.git push origin rongshi

# 查看代码藏列表
9.git stash list 

# 将藏起来列表第一列还原
10.git stash pop stash@{0}
```



### 场景二：commit之后发现本次提交还有文件没有添加上

**将新的内容追加到最后的commit上**

```gas
# 也就是保持changeId不变
git commit --amend
```

> :x表示w 写+q 退出



### 场景三：push origin后发现少提交或者多提交了文件

**版本回退：回退到之前某个版本**

```gas
#查看log记录
1.git log
#回退到之前某个版本 有软回退和硬回退  不指定是软回退
2.git reset 上一次提交的版本  硬回退 git reset --hard ***
#查看状态
3.git status

4.把不想提交的文件，但是已经add过处于暂存区域，git reset HEAD 取消暂缓

5.git add 想要提交的文件

6.git commit -m "描述"

7.git stash

8.git fetch upstream

9.git rebase upstream/enterprise_wechat

10.git push origin enterprise_wechat

11.git stash list 

12.git stash pop stash@{0}

```

​	

### 场景四：创建分支/切换分支/合并分支/删除分支

**git branch 和 git checkout 和 git merge 的运用**

```gas
# 查看分支
git branch
# 创建分支
git branch <分支名>
# 切换分支
git checkout <分支名>
# 创建并切换分支
git checkout -b <分支名>
# 合并某分支到当前分支
git merge <分支名>
# 删除本地分支
git branch -d <分支名>
# 创建新分支并切换到新分支且跟踪到远程分支
git checkout -b <分知名> origin/<分知名>

# 删除远程分支
git branch -r -d origin/develop  # 删除本地的远程分支跟踪
git push origin :develop  #真正删除远程仓库分支

```

> 注意对于任何分支而言：工作区和暂存区都是公共的！！！



### 场景五：删除不需要上传到远程但是已经上传的文件或文件夹

**git rm的运用**

```gas
# 预览将要删除的文件
git rm -r -n --cached 文件/文件夹名称 
# 确定无误后删除文件,不会删除本地文件
git rm -r --cached 文件/文件夹名称
# commit 后push到远程即可
git commit -m "remove some file dir"
git push origin
```



### 场景六：添加远程主机

```gas
 # git reomote add 主机名 url
 git remote add upstream http://git.yuntongxun.com/platform_sdk/IMPlusAndroidSDK.git
 
 # 首次推送 develop 分支到 upstream 主机
 git push -u upstream develop
```



### 场景七：push远程主机新分支

```gas
# git push 远程主机名 本地分支名:远端希望创建的分支 (一般选择本地和远程名字一样)
git push origin develop:develop
```



### 场景八：切换当前本地分支的远程分支跟踪

```bash
# git branch --set-upstream-to= 将要切换到的远程主机/远程分支名  本地分支名
git branch --set-upstream-to=origin/NewSDK NewSDK
```



### 场景九：回退的一些操作

```gas
# 既可以从新版本完全回退到旧版本，也可以从旧版本回到新版本
git reset --hard commit版本号
```



### git add作用

**作用1：尚未暂存以备提交的变更：**

```gas
#更新要提交的内容，将暂存区的改动进行跟新
git add <文件>

```

**作用2：未跟踪的文件：**

```gas
# 以包含要提交的内容  将工作区的文件提交到暂存区并更新
git add <文件>
```



### git reset作用

**作用1：可以回退到某个版本**

```gas
git reset log_version
```

**作用2：把暂存区的修改回退到工作区**

```gas
#以取消暂存
git reset HEAD <文件> 
```



### git checkout作用

**作用1：git checkout -- <文件>，把文件在工作区的修改全部撤销**

```gas
#丢弃工作区的改动
git checkout --<文件>
```



### git show 展示某次commit的代码变动

```gas
git show commitId
```



### git tag 给某个版本打标签

```gas
# tag意义在于能够快速定位版本commit，方便后续回退
# 在当前commit创建标签
git tag  v1.1.2
# 删除本地标签
git tag -d v2.5.3
# 删除远程标签
git push origin :refs/tags/v1.1.2
# 查看tag信息
git show v1.1.2
# 标签推送到远程主机
git push origin v1.1.2
# 新建一个分支，指向某个tag
git checkout -b develop v1.1.2_develop
```



### git中--的作用

In git, a ' -- ' before the file list tells git that all the next arguments should be interpreted as filenames, not as branch-names or anything else. It's a helpful disambiguator sometimes.

**在git中，文件列表前面的' - '告诉git所有下一个参数都应该被解释为文件名，而不是分支名称或其他任何东西。**