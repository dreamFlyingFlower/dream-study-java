# Lua



# 概述

* Lua是一个高效、简洁、轻量级、可扩展的脚本语言,可以很方便的嵌入到其它语言中使用



# 数据类型

* Lua是一个动态类型的语言,一个变量可以存储任何类型的值
* 空:nil,也就是还没有赋值
* 字符串:用单引号 或者 双引号 引起来
* 数字:包含整数和浮点型
* 布尔:boolean
* 表:表是Lua唯一的数据结构,既可以当数组,也可以做Map,或被视为对象
* 函数:封装某个或某些功能
* userData:用来将任意 C 数据保存在 Lua 变量中,这样的操作只能通过 C 语言API
* Thread:用来区别独立的执行线程,它被用来实现 coroutine (协同例程)
* 在Lua中,只有nil和false才是假,其它类型的值均被认为是真



# 变量

* 区分大小写



## 全局变量

* 无需声明即可直接使用,默认值是nil



## 局部变量

* 声明方法为:local 变量名
* 局部变量的作用域为从声明开始到所在层的语句块结尾



# 注释

* 单行:--
* 多行:--[[ 开始,到 ]] 结束



# 操作符

* 数学操作符:+、-、*、/、%、- 取反、^ 幂运算;如果操作数是字符串,会自动转换成数字进行操作
* 比较操作符:==、~=、〉、>=、<、<=；比较操作符不会转换类型,如果类型不同进行比较,会返回false;可以手动使用tonumber或者tostring进行转换
* 逻辑操作符:and、or、not
* 连接操作符:..；用来连接两个字符串
* 取长度操作符:#,例如:print(#’helloworld’)
* 操作符的优先级跟其它编程语言是类似的



# If

```lua
if 条件 then
    -- dosomething
elseif 条件 then
    -- dosomething
else
    -- dosomething
end
```



# For

```lua
-- 步长可省略,默认为1,用break跳出循环
for 变量=初值,终值,步长 do
    -- dosomething
end
-- 增强for循环
for 变量1,变量2…,变量N in 迭带器 do
    -- dosomething
end
```



# While

```lua
-- 用break跳出循环
while 条件 do
    -- dosomething
end
```



# Repeat

```lua
-- 用break跳出循环
repeat
    -- dosomething
until 条件
```



# 表

* 可以当作数组或者Map来理解

  ```lua
  a = {}; -- 报一个空表赋值给a
  a[key]=value; -- 把value赋值给表a中的字段key
  a={ key1=‘value1’, key2=‘value2’ };
  a.key1; -- 引用的时候,可以使用.操作符
  a[1]; -- 如果用索引来引用,跟数组是一样的,Lua的索引是从1开始
  -- 向a中添加方法,注意中间是冒号
  function a:test()
  	-- self:相当于this
  end
  -- 可以使用增强for循环来遍历数组
  for k,v in ipairs(a) do
      print(k)
      print(v)
  end
  -- ipairs是Lua的内置函数,实现类似迭带器的功能,从索引1开始递增遍历到最后一个不为nil的整数索引.类似的还有一个pairs,用来遍历非数组的表值,它会遍历所有值不为nil的索引
  -- 也可以使用for循环来按照索引遍历数组
  for i=1,#a do
  end
  ```

  

# 函数

```lua
function(参数列表)
	-- dosomething
end
```

* 就算没有参数,括号也不能省略
* 形参实参个数不用完全对应,如果想要得到所有的实参,可以把最后一个形参设置成…
* 函数内返回使用return



# 面向对象

* Lua脚本的面向对象类似于JavaScript的面向对象,都是模拟的

  ```lua
  -- 直接创建对象
  local user={userId='user1',userName='sishuok'}
  -- 加新属性
  user.age = 12
  -- 添加方法,里面的self就相当于this
  function user:show(a)
      redis.log(redis.LOG_NOTICE,'a='..a..',age='..self['age'])
  end
  -- 调用方法
  user:show('abc')
  -- 子类继承,__index在这里起的作用就类似于JS中的Prototype
  local child={address='bj'}
  setmetatable(child,{__index=user})
  -- 子类调用父类方法
  child:show('child')
  -- 覆盖父类方法
  function child:show(a)
      redis.log(redis.LOG_NOTICE,'child='..a..',age='..self['age']..)
  end
  ```



# 模块化



* 直接使用require("model_name")来载入别的lua文件,文件后缀是.lua,载入时就会直接执行那个文件
* 载入同样的lua文件时,只有第一次的时候会去执行,后面的相同的都不执行
* 如果要让每一次文件都执行,可使用dofile("model_name")函数
* 如果要载入后不执行,等需要的时候执行,可使用 loadfile("model_name")函数,这种是把loadfile的结果赋值给一个变量,比如:local abc = loadfile("model_name") 后面需要运行时: abc()

