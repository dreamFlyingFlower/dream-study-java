# Git



# 概述



Head:指向当前分支,并非指向master.切换到那个分支就是指向那个分支

工作区:当前分支存储目录

暂存区:修改后的文件,经过add之后添加到的区域,只是一个概念

本地仓库:当使用commit之后,会将修提交到本地仓库中,而暂存区就被清空

远程仓库:push之后会将本地仓库中的修改提交到远程仓库,仓库的其他成员就可以拉取最新的代码



# 常用命令

* git init:将一个目录初始化为git仓库,必须是空目录

* git clone [] url:将git远程仓库中的内容拉去到本地

  * -b branchname:拉取指定分支到本地，branchname为要拉取的分支名称
  * --shallow: 克隆仓库,但是不包括版本历史信息

* git status []:查看本地仓库和远程仓库的差异

  * -s:查看详情,会出现2个M,后面跟着文件名.第一列M表示版本库和处理中间状态有差异;第二M表示工作区和当前文件有差异.有时候只会有1列

* git pull []:从远程仓库拉取最新的代码到本地仓库中

  * version:从远程仓库中拉去指定版本的,该版本号可从git log中获取

* git add -A:将修改的所有文件都添加到本地的预提交程序中

* git add filename:将某个文件添加到本地的预提交程序中

* git commit [] :将git add中预提交的文件提交到本地仓库

  * -am comment:提交所有同时提交注释,comment为注释,必须填写,a功能和git add -A相同
  * --amend:编辑提交的内容或信息

* git push [] [origin master]:将本地仓库中的修改同步到远程仓库中,默认提交到origin master;若有多个git远程仓库,origin要自定义命名,且和origin不一样,此时必须加上自定义名称和分支,如git push github master.若只是分支不一样,可以是git push origin aa,aa为分支名

  * -f:强制推送到远程仓库

* git checkout [<file>/<folder>]:将文件/目录恢复到初始状态,已经被add进暂存区的文件不会被恢复

* git rebase:将分支进行合并

* git merge aa:将aa分支中的代码合并到当前分支,注意可能需要解决冲突.git merge的3种情况

  * allow merge commits:直接合并到当前分支中,同时保留当前分支和被合并分支的commits等信息,但是各自的commits信息保留在各自的分支中.从github的inlights查看
  * allow squash commits:直接合并到当前分支中,但是不保留合并分支的路线,而是直接从2个分支的共同点上开始,直接将被合并分支的结果合并到当前分支中,舍弃被合并分支的commits等信息
  * allow rebase commits:直接合并到当前分支中,但是不保留合并分支的路线,而是直接从2个分支的共同点上开始,将被合并分支的commit都提交到合并分支中

* git mv oldfilename newfilename:将文件改名之后重新添加到暂存区中



# git config

* --local:只对某个仓库有效
* --global:当前用户所有仓库有效
* --system:对系统所有登录用户有效
* --list/-l --local/--global/--system:显示config的配置
* --global user.name <username>:设置提交的用户名
* --global user.email <email>:设置提交的邮箱地址
* --global core.autocrlf []:windows,mac的换行符不一样,win是CRLF,mac是LF,不同开发者使用系统不一样会导致在比对时因为换行符的问题而出现差异
  * true:提交时转换为LF,检出时转换为CRLF
  * false:提交检出均不转换
  * input:提交时转换为LF,检出时不转换
* --global core.safecrlf []:全局设置git对换行符的行为权限
  * true:拒绝提交包含混合换行符的文件
  * false:允许提交包含混合换行符的文件
  * warn:提交包含混合换行符的文件时给出警告
* core.ignorecase false:设置忽略大小写配置,可检测到文件名大小写变更
* --global core.compression -1:默认zlib压缩方式,0不压缩
* --global http.postBuffer 524288000:配置git缓存大小500M或更大,需要拉取的文件比较大时使用
* --global http.lowSpeedLimit 0:配置git最低速度,git拉取速度较低时使用
* --global http.lowSpeedTime 99999:配置git最低速度可持续时间,单位秒,git拉取速度较低时使用
* git update-index --assue-unchanged config.conf:设置config.conf文件忽略更新,不提交,但是也不从远程仓库删除
* git update-index --no-assume-unchanged config.conf:取消config.conf的忽略更新



# git remote

* 查看本地库对应的远程仓库名称,默认都是origin,是一个标签

* `-v`:查看默认拉取和提交的远程仓库名称和地址,名称和地址是一对一的关系

* `add name [branch] url`:添加一个新的源地址,比如github和gitee2个源

  * name:和origin一样,是一个标签,可自定义,不可重复
  * branch:远程仓库分支名,默认为master
  * url:新的远程仓库地址

  ```shell
  # 当新添加了一个源地址后,首次拉取代码需要带上参数
  git pull name master --allow-unrelated-histories
  ```

* `rm name`:删除一个源,若只有一个源的时候,不可删除.origin不可删除



# git branch

* `git branch []`:查看本地仓库当前分支
  * -a:查看所有分支
  * -v:查看版本以及注释
* `git branch aa`:根据当前分支创建一个新的分支aa,aa的所有代码和当前分支一样
* `git branch -d aa`:删除分支aa,如果该分支没有被完全的merge,不能删除
* `git branch -D aa`:强制删除分支,即便该分支没有被完全的merge
* `git checkout -b aa`:根据当前分支创建一个新的分支aa,并切换到aa分支上
* `git checkout -b 本地分支名 origin/远程分支名`:将远程分支拉取到本地,本地分支不存在
* `git checkout aa`:切换到aa分支上
* `git pull origin aa`:从aa分支上拉取最新代码,需要显示的指定用户名和分支名
* `git push origin aa[:master]`:将新建的本地分支提交到远程仓库.若远程仓库不存在aa分支,则会自动创建,也可以提交到指定的分支上.需要显示的指定用户名和分支名
* `git merge [revision]`: 合并到当前分支
* `git mergetool`: 使用工具来处理合并冲突
* `git branch prune origin`: 删除本地仓库中已经被远程分支删除的分支



# git log

* `git log`: 显示历史日志
* `--all`:查看所有的历史日志,包括分支的创建等
* `--graph`:以图形化的形式展示历史日志
* `--all --graph --decorate`: 可视化历史记录(有向无环图)
* `--oneline`:显示历史日志在同一行
* `-nx`:只显示最近的x条历史日志
* `-Sstr`:查找str第一次出现的commit
* `-Gstr`:查找str有改变的commit,包括第一次提交
* `-p -- filename`:输出指定文件的详细改变信息
* `-L begin,+n:filename`:查看指定文件从begin开始的n行历史记录
* `-n filename`:查看最近n个版本的历史信息
* `git reflog`:查看所有的历史版本记录,包括回退的版本记录



# git stash

* git stash [save message]:执行存储时,保存当前工作进度,将工作区和暂存区恢复到修改之前
  * 该命令会将当前所有修改过的文件都恢复到没有修改之前,不能单独指定
  * 被恢复到修改之前的文件将不再显示在git的修改里
  * 该命令只对已经加入了工作区的文件有效,新文件无效.即add过的才有效
  * 被该命令暂存的文件不会更新,在本地的修改也不会有提示
  * 若有其他开发人员修改了被stash的文件,不会应用到这些文件.当stash的文件被恢复时,只会恢复到被stash的版本,可能会和当前版本存在极大差异,需慎重使用
* git stash list:查看stash了哪些存储
* git stash show [] [stash@{num}]:显示做了哪些改动,默认show第一个存储,如果要显示其他存贮,比如第二个,stash@{1},num从0开始
  * -p:显示改动的详情,如冲突,修改行等
* git stash apply [stash@{num}]:应用某个存储,但不会把存储从存储列表中删除,默认使用第一个存储,若想应用其他存储,可修改num值,num从0开始
* git stash pop [stash@{num}]:应用某个存储,将缓存堆栈中的对应stash删除,并将对应修改应用到当前的工作目录下,默认为第一个stash
* git stash drop stash@{$num}:丢弃stash@{$num}存储,从列表中删除这个存储
* git stash clear:删除所有缓存的stash



# git update-index

* git update-index --skip-worktree path:将某个已经添加到工作区的文件从工作区忽略,但是有不同的更新出现时,会造成冲突.即本地忽略提交,但是pull到不同内容还是会冲突
* git update-index --no-skip-worktree path:将已经skip的文件重新添加到工作区
* -q:continue refresh even when index needs update
* --ignore-submodules:refresh,ignore submodules
* --add:不忽略新文件
* --replace:let files replace directories and vice-versa
* --remove:notice files missing from worktree
* --unmerged:refresh even if index contains unmerged entries
* --refresh:refresh stat information
* --really-refresh:like --refresh, but ignore assume-unchanged setting
* --cacheinfo <mode>,<object>,<path>:add the specified entry to the index
* --chmod (+/-)x:override the executable bit of the listed files
* --assume-unchanged:mark files as "not changing"
* --no-assume-unchanged:clear assumed-unchanged bit
* --skip-worktree:mark files as "index-only"
* --no-skip-worktree:clear skip-worktree bit
* --info-only:add to index only; do not add content to object database
* --force-remove:remove named paths even if present in worktree
* -z:和--stdin配合使用,input lines are terminated by null bytes
* --stdin:read list of paths to be updated from standard input
* --index-info:add entries from standard input to the index
* --unresolve:repopulate stages #2 and #3 for the listed paths
* -g, --again:only update entries that differ from HEAD
* --ignore-missing:ignore files missing from worktree
* --verbose:report actions to standard output
* --clear-resolve-undo:(for porcelains) forget saved unresolved conflicts
* --index-version <n>:write index in this format
* --split-index:enable or disable split index
* --untracked-cache:enable/disable untracked cache
* --test-untracked-cache:test if the filesystem supports untracked cache
* --force-untracked-cache:enable untracked cache without testing the filesystem



# git diff

* git diff <filename>: 显示与上一次提交之间的差异
* git diff <revision> <filename>: 显示某个文件两个版本之间的差异
* git diff --cached:暂存区和上一次提交的差异
* git diff b1 b2:比较2个分支(b1,b2)之间的差异
* git diff b1 b2 -- filename1 filename2:比较2个分支之间指定文件之间的差异
* git diff commit1 commit2:比较2个提交之间的差异.commit1和2是提交的hash值,从git log中查看



# git reset

* git reset HEAD <file>:将当前分支暂存区的文件恢复到上一个版本,即将已经add的文件从暂存区退回到工作区,但是修改仍然存在.和checkout不同的是:
  * reset恢复的是已经add到暂存区的,且恢复之后修改仍然存在,只是重新回到了工作区
  * checkout恢复的是没有add到暂存区的,且将文件直接回退到上一个版本
* git reset --hard [HEAD^]:将本地仓库中的数据回滚到上个版本
* git reset --hard HEAD^2:将本地仓库中的数据回滚到上2个版本
* git reset version:将本地仓库中的数据回滚到指定版本,version可从git log中查看
* git reset version filename:将指定文件回滚到指定版本
* git reset --hard commtid:已经提交到暂存区的文件,恢复到指定commit
* git reset --soft commtid:将当前分支撤销到指定版本,但是暂存区和工作区不做撤销



# git bisect

* 主要是问题定位.当commit很多时,可以通过类似2分法的操作,从一个good版本和一个bad版本中间开始查找有问题的版本
* git bisect start:开始定位问题,此时会对指定commit版本进行标记,只有good和bad
* git bisect good/bad commitid:指定某个提交为good或bad,如果同时存在一个good和一个bad时,就会自动跳到中间版本的commitid上.以此类推,直到没有bad版本时,就会定位到问题的最终commitid
* git bisect skip commitid:若某个版本不确定是否有问题,可以跳过.若已经在当前commitid上,commitid可省略
* 当查找到最终有问题的版本时,会显示该版本所有的文件变更
* git bisect reset:结束问题定位
* git bisect log>file.log:输出定位日志.若定位有问题,将bad判断成good,可以删除其中的日志,使用reset结束bisect,再从file.log开始定位问题
* git bisect replay file.log:从指定的bisect日志文件开始定位问题





# git blame

* 定位代码的修改人,修改时间,由那一个commitid提交
* git blame filename:显示指定文件的修改人等信息
* git blame -L begin,end filename:只显示文件中从begin行号到end行号的修改信息
* git blame -L begin,+n filename:显示文件中从begin以及后面的n行信息,包括begin行



# git grep

* 功能和linux上的grep类型
* git grep str:从当前分支的所有文件中查找str字符串
* git grep -n str:查找字符串的同时显示行号
* git grep --count str:输出待查找字符串总共出现的次数
* git grep -p str *.java:查找待查找字符串在指定类型文件中出现的方法名
* git grep -e str:使用正则表达式查找待查找字符串
* git grep -e str1 --or/--and -e str2:多条件查找
* git grep -e --not str:查找不包含待查找字符串的行
* git grep str commitid/HEAD/HEAD~:从指定版本中查找待查找字符串

```shell
git grep -e 'abc' --and \( -e 'bfdfd' --or --not -e 'fdsfd' \)
```



# .git目录



## hooks目录



## info目录



## logs目录



## objects目录



### pack

* 将objects目录中的历史修改文件信息打包的目录



### 其他目录

* 保存每次修改的信息,会有多个文件
* 查看文件信息.如目录名为xx,文件名为ffffffffff
  * git cat-file -t xxffffffffff:查看当前文件的提交信息,可能返回tree,commit,blob(文件)
  * git cat-file -p xxffffffffff:
    * tree类型:展示本次提交的所有信息列表,同样都是tree类型,每个tree都会有一个hash值,再次使用git cat-file -p 该hash值,可以查看具体的文件内容
    * blob类型:直接查看文件的内容
    * commit:提交信息



## refs目录



### heads

* 本地仓库信息,包含所有本地的分支文件,每个文件内容是一个类似uuid的字符串



### remotes

* 远程仓库地址信息
* 通常情况下,只会有一个origin目录,包含的是默认的远程仓库地址以及分支信息
* 如果同时提交多个远程仓库,该目录下会有多个目录,和默认的分支一样



### tags

* 标签信息,保存是已经稳定版本的分支信息,不可更改分支里的文件,只能新增,覆盖(整体覆盖),删除



## COMMIT_EDITMSG



## config

* 当前项目的配置文件,包括远程仓库地址,用户信息以及其他通用信息



## description



## FETCH_HEAD



## Head

* 文本文件,保存当前分支地址



## index



## ORIG_HEAD



## packed-refs





# 本地.git清理

* 本地.git目录会越用越大,需要清理
* git verify-pack -v .git/objects/pack/pack-*.idx | sort -k 3 -g | tail -5:找出大文件前5个
  * git verify-pack -v .git/objects/pack/pack-*.idx:查看本地.git目录中的pack文件,所有的历史修改都会打包到该文件中
  * sort []:排序
    * -k:指定排序参照列,此处是第3列,文件的大小
    * -n:依照数值的大小排序,-g同-n
* git rev-list --objects --all:按照默认反向时间顺序,输出命令指定的commit objects
  * --objects:列出的提交引用的任何对象的对象ID
  * --all:全部匹配结果
* git filter-branch []:重写git历史
  * -f:拒绝从现有的临时目录开始,强制执行改写操作
  * --index-filter:与tree-filter相比,不检查树,和git rm搭配使用,更快的生成版本
  * --ignore-unmatch:如果你想完全删除一个文件,在输入历史记录时无关紧要
  * --prune-empty:如果修改后的提交为空则扔掉不要,实际可能虽然文件被删除了,但是还剩下个空的提交
  * --tag-name-filter cat:来简单地更新标签
  * --all:针对所有的分支,这个是为了让分隔开git filter-branch 和 --all
* git for-each-ref:输出指定位置所有reflog条目
  * --format:指定带有特定字符的Object
* git update-ref:update reflog条目
* git reflog expire:删除掉--expire时间早的reflog条目
* git gc --prune=now:对指定日期之前的未被关联的松散对象进行清理
* git push --force []:非真正的提交
  * --verbose:详细输出运行log
  * --dry-run:做真的push到远程仓库以外的所有工作
* git push --force:真正的提交

```shell
# 找出大文件并过滤输出
git rev-list --objects --all | grep "$(git verify-pack -v .git/objects/pack/*.idx | sort -k 3 -n | tail -5 | awk '{print$1}')";
# filename可以是单个文件,也可以是整个文件夹,如notes/*
git filter-branch -f --index-filter 'git rm --cached --ignore-unmatch <filename>' --prune-empty --tag-name-filter cat -- --all;
# 更新清理优化
rm -Rf .git/refs/original/
rm -Rf .git/logs/
git gc
git prune
git push --force
```

* 在回退版本的时候即使回退到该大文件A存在的历史版本,依然无法获取A文件,这个文件被永久被仓库以及仓库历史中删除



# 多GIT仓库

* 若有多个远程仓库需要提交,可以使用git remote命令添加多个地址

  ```shell
  # other_origin是另外一个远程仓库的名称,可自定义,默认是origin,已经被默认的仓库地址占据
  # 拉下来默认是master分支,也可以指定分支,使用-b参数,后面的地址是另外一个仓库的地址
  git remote add other_origin https://gitreposiory/gitname/repositoryname.git
  # 从默认远程仓库拉代码
  git fetch origin master
  # 从另外一个仓库拉代码
  git fetch other_origin master
  # 默认仓库提交代码
  git push origin master
  # 另外一个仓库提交代码
  git push other_origin master
  ```



# Fork

* 从其他仓库fork的代码,原仓库更新后如何和本地仓库保持同步

  ```shell
  # 检出自己在github上fork别人的分支到目录下
  git clone https://github.com/_your/_project.git
  # 进到_project目录下,然后增加原远程分支(fork的分支),名为fork_origin(名字任意)到本地
  git remote add fork_origin https://github.com/_original/_project.git
  # 运行命令:`git remote -v`,会发现多出来了一个fork_origin的远程分支
  git remote -v
  # 然后把远程原始分支fork_origin的代码拉到本地
  git fetch fork_origin
  # 合并对方远程原始分支fork_origin的代码
  git merge fork_origin/master
  # 最后把最新的代码推送到自己的github上
  git push origin master
  # 如果需要给update_stream发送Pull Request,打开https://github.com/_your/_project.git,点击Pull Request->点击New Pull Request->输入Title和功能说明->点击Send pull request
  ```



# 本地备份

* git clone --bare src/.git dest:在dest目录中执行,将src项目备份到当前目录中
* git clone --bare file///src/.git dest:同上,但是会有压缩,进度条等.更智能,通常使用该方式



# 其他

- `git add -p`: 交互式暂存
- `git blame`: 查看最后修改某行的人
- `git bisect`: 通过二分查找搜索历史记录
- `.gitignore`: 指定不追踪的文件



# SSH

* 打开git bash,直接输入ssh-keygen -t rsa -C "email地址",回车
  * 之后会输入key的名称,不输则默认为id
  * 输入密码,不输则为空
* 若是windows系统,会在C/用户/用户名/下生成.ssh文件夹,里面会有id_rsa,id_rsa.pub
* 用文本编辑器打开id_rsa.pub,复制里面的内容到github或gitee或gitlab的设置的ssh里面.之后本地提交修改到远程服务器可以不使用用户名和密码,直接通过ssh认证,更加方便安全
* 添加了ssh之后,如果再pull,最好使用ssh方式,而且现在github已经不再支持使用密码模式提交修改



# Gitlab

## 安装

### yum安装

* yum install git

### 压缩包安装



## 备份

1. 备份时需要保持gitlab处于正常运行状态,直接执行gitlab-rake gitlab:backup:create进行备份

2. 备份默认会放在/var/opt/gitlab/backups下,名称如1591836711_2020_06_11_10.8.7_gitlab_backup.tar,这个压缩包是完整的仓库

3. 可通过修改/etc/gitlab/gitlab.rb配置文件来修改默认存放备份目录

   ```shell
   # 设置自定义的备份目录
   gitlab_rails['backup_path'] = "/var/opt/gitlab/backups"
   # 设置完成之后需要重启gitlab配置
   gitlab-ctl reconfigure
   # 设置备份过期时间,以秒为单位
   gitlab_rails['backup_keep_time'] = 604800
   ```

4. 自动进行仓库备份

   ```shell
   # 编辑定时任务
   crontab -e
   # 输入命令:分钟,小时,天,月,周 执行命令
   0 2 * * * /opt/gitlab/bin/gitlab-rake gitlab:backup:create
   ```

   

## 迁移恢复

1. 在新服务器上安装相同版本的gitlab

2. 将备份生成的文件发送到新服务器的相同目录下

3. 停止gitlab

   ```shell
   # 停止相关数据连接服务
   gitlab-ctl stop unicron
   gitlab-ctl stop sidekiq
   # 修改备份文件权限
   chmod 777 1591836711_2020_06_11_10.8.7_gitlab_backup.tar
   # 备份,按2次yes完成
   gitlab-rake gitlab:backup:restore BACKUP=1591836711_2020_06_11_10.8.7
   # 重启gitlab
   gitlab-ctl start
   ```

   

## 升级

1. 停止gitlab并备份

   ```shell
   gitlab-ctl stop
   gitlab-rake gitlab:backup:create
   ```

2. 下载最新安装包,若安装时出现Error executing action `run` on resource 'ruby_block[directory resource: /var/opt/gitlab/git-data/repositories]',解决如下chmod 2770 /var/opt/gitlab/git-data/repositories

3. 重启gitlab

   ```shell
   gitlab-ctl reconfigure
   gitlab-ctl restart
   ```



## 其他问题

* 当push成功之后发现web管理界面没有改变,是需要清理缓存的原因

  ```shell
  gitlab-rake cache:clear RAILS_ENV=production
  # 若抛异常Gem::LoadError: You have already activated rake 10.5.0, but your Gemfile requires rake 12.0.0. Prepending `bundle exec` to your command may solve this.可执行以下操作
  bundle exec ‘rake cache:clear RAILS_ENV=production’
  ```

  