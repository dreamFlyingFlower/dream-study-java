# Git



# 常用命令

* git init:将一个目录初始化为git仓库,必须是空目录

* git clone [] url:将git远程仓库中的内容拉去到本地

  * -b branchname:拉取指定分支到本地，branchname为要拉取的分支名称
  * --shallow: 克隆仓库,但是不包括版本历史信息

* git remote []:查看本地库对应的远程仓库名称,默认都是origin,是一个标签
  * -v:查看拉取和提交的远程仓库名称和地址
  
  * add name url:添加一个新的源地址,比如github和gitee2个源.name和origin类似,可自定义,不可重复.url是新的源的远程地址
  
    ```shell
    # 当新添加了一个源地址后,首次拉取代码需要带上参数
    git pull name master --allow-unrelated-histories
    ```
  
  * rm name:删除一个源,若只有一个源的时候,不可删除.origin不可删除
  
* git status []:查看本地仓库和远程仓库的差异

  * -s:查看详情,会出现2个M,后面跟着文件名.第一列M表示版本库和处理中间状态有差异;第二M表示工作区和当前文件有差异.有时候只会有1列

* git log [-n] [filename]:查看历史版本信息,会包含版本号,一长串的字符
  * -n:最近的n个版本
  * filename:指定文件的信息,文件需要带上路径
  
* git pull []:从远程仓库拉取最新的代码到本地仓库中

  * version:从远程仓库中拉去指定版本的,该版本号可从git log中获取

* git add -A:将修改的所有文件都添加到本地的预提交程序中

* git add filename:将某个文件添加到本地的预提交程序中

* git commit [] :将git add中预提交的文件提交到本地仓库

  * -am comment:提交所有同时提交注释,comment为注释,必须填写,a功能和git add -A相同
  * --amend:编辑提交的内容或信息

* git push [] [origin master]:将本地仓库中的修改同步到远程仓库中,默认提交到origin master;若有多个git远程仓库,origin要自定义命名,且和origin不一样,此时必须加上自定义名称和分支,如git push github master.若只是分支不一样,可以是git push origin aa,aa为分支名

  * -f:强制推送到远程仓库

* git checkout <file>:丢弃修改,将文件恢复到初始状态,已经被add进暂存区的文件不会被恢复

* git rebase:将分支进行合并

* git merge aa:将aa分支中的代码合并到当前分支,注意可能需要解决冲突



# git reset

* git reset HEAD <file>:将暂存区的文件恢复到上一个版本.即将已经add的文件从暂存区退回到工作区.和checkout不同的是:reset恢复的是已经add到暂存区的,checkout恢复的是没有add到暂存区的
* git reset --hard [HEAD^]:将本地仓库中的数据回滚到上个版本
* git reset --hard HEAD^2:将本地仓库中的数据回滚到上2个版本
* git reset --hard version:将本地仓库中的数据回滚到指定版本
* git reset --hard version filename:将指定文件回滚到指定版本



# git bransh

* `git branch []`:查看本地仓库当前分支
  * -a:查看所有分支
  * -v:查看版本以及注释
* `git branch aa`:根据当前分支创建一个新的分支aa,aa的所有代码和当前分支一样
* `git branch -d aa`:删除分支aa
* `git checkout -b aa`:根据当前分支创建一个新的分支aa,并切换到aa分支上
* `git checkout aa`:切换到aa分支上
* `git pull origin aa`:从aa分支上拉取最新代码,需要显示的指定用户名和分支名
* `git push origin aa[:master]`:将新建的本地分支提交到远程仓库.若远程仓库不存在aa分支,则会自动创建,也可以提交到指定的分支上.需要显示的指定用户名和分支名
* `git merge [revision]`: 合并到当前分支
* `git mergetool`: 使用工具来处理合并冲突



# git log

* `git log`: 显示历史日志
* `git log --all --graph --decorate`: 可视化历史记录(有向无环图)



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



# git config

* git config --global core.autocrlf []:windows,mac的换行符不一样,win是CRLF,mac是LF,不同开发者使用系统不一样会导致在比对时因为换行符的问题而出现差异
  * true:提交时转换为LF,检出时转换为CRLF
  * false:提交检出均不转换
  * input:提交时转换为LF,检出时不转换
* git config --global core.safecrlf []:全局设置git对换行符的行为权限
  * true:拒绝提交包含混合换行符的文件
  * false:允许提交包含混合换行符的文件
  * warn:提交包含混合换行符的文件时给出警告
* git update-index --assue-unchanged config.conf:设置config.conf文件忽略更新,不提交,但是也不从远程仓库删除
* git update-index --no-assume-unchanged config.conf:取消config.conf的忽略更新
* git config core.ignorecase false:设置忽略大小写配置,可检测到文件名大小写变更
* git config --global core.compression -1:默认zlib压缩方式,0不压缩
* git config --global http.postBuffer 524288000:配置git缓存大小500M或更大,需要拉取的文件比较大时使用
* git config --global http.lowSpeedLimit 0:配置git最低速度,git拉取速度较低时使用
* git config --global http.lowSpeedTime 99999:配置git最低速度可持续时间,单位秒,git拉取速度较低时使用



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



# 其他

- `git add -p`: 交互式暂存
- `git blame`: 查看最后修改某行的人
- `git bisect`: 通过二分查找搜索历史记录
- `.gitignore`: 指定不追踪的文件



# Gitlab

## 一 安装

### yum安装

* yum install git

### 压缩包安装



## 二 备份

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

   

## 三 迁移恢复

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

   

## 四 升级

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



## 五 其他问题

* 当push成功之后发现web管理界面没有改变,是需要清理缓存的原因

  ```shell
  gitlab-rake cache:clear RAILS_ENV=production
  # 若抛异常Gem::LoadError: You have already activated rake 10.5.0, but your Gemfile requires rake 12.0.0. Prepending `bundle exec` to your command may solve this.可执行以下操作
  bundle exec ‘rake cache:clear RAILS_ENV=production’
  ```

  