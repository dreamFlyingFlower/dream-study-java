# Quartz



# 概述

1. springboot整合quartz无法自动创建表,需要 手动创建quartz表,如果不实现持久化,可以不创建表
2. quartz持久化的数据库文件在jar包中的src\org\quartz\impl\jdbcjobstore下,每种数据库有不同
3. JobStore:Scheduler在运行时用来存储相关的信息,JDBCJobStore和JobStoreTX都使用关系数据库来存储schedule相关信息,JobStoreTX在每次执行任务后都使用commit或rollback来提交更改
4. Scheduler:与调度程序交互的主要API
5. Job:由希望由调度程序执行的组件实现的接口
6. JobDetail:用于定义作业的实例
7. Trigger:触发器,定义执行给定作业的计划的组件
8. JobBuilder:用于定义/构建JobDetail实例,用于定义作业的实例
9. TriggerBuilder:用于定义/构建触发器实例



# 相关表

1. qrtz_blob_triggers:以Blob 类型存储的触发器
2. qrtz_calendars:存放日历信息,quartz可配置一个日历来指定一个时间范围
3. qrtz_cron_triggers:存放cron类型的触发器
4. qrtz_fired_triggers:存放已触发的触发器
5. qrtz_job_details:存放一个jobDetail信息
6. qrtz_job_listeners:job监听器
7. qrtz_locks:存储程序的悲观锁的信息(假如使用了悲观锁)
8. qrtz_paused_trigger_graps:存放暂停掉的触发器
9. qrtz_scheduler_state:调度器状态
10. qrtz_simple_triggers:简单触发器的信息
11. qrtz_trigger_listeners:触发器监听器
12. qrtz_triggers:触发器的基本信息



# Misfire策略

> 该策略是由于系统奔溃或者任务时间过长等原因导致trigger在应该触发的时间点没有触发,并且超过了misfireThreshold设置的时间(默认1m,没有超过就立即执行)就算misfire了,此时就该设置如何应对这种异常



## SimpleTirgger

* MISFIRE_INSTRUCTION_FIRE_NOW:立刻执行,对于不会重复执行的任务,这是默认的处理策略
* MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT:在下一个激活点执行,且超时期内错过的执行机会作废
* MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_COUNT:立即执行,且超时期内错过的执行机会作废
* MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT:在下一个激活点执行,并重复到指定的次数
* MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_COUNT:立即执行,并重复到指定的次数
* MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:忽略所有的超时状态,按照触发器的策略执行



## CronTrigger

* MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:立刻执行一次,然后就按照正常的计划执行,默认策略
* MISFIRE_INSTRUCTION_DO_NOTHING:目前不执行,然后按照正常的计划执行,这意味着如果下次执行时间超过了end time,就没有执行机会了



# 主要类



## Scheduler

1. 对Trigger和Job进行管理,Trigger和JobDetail可以注册到Scheduler中,两者在Scheduler中都拥有自己的唯一的组和名称用来进行彼此的区分
2. Scheduler可以通过组名或者名称来对Trigger和JobDetail来进行管理,一个Trigger只能对应一个Job,但是一个Job可以对应多个Trigger
3. 每个Scheduler都包含一个SchedulerContext,用来保存Scheduler的上下文,Job和Trigger都可以获取SchedulerContext中的信息
4. Scheduler包含两个重要的组件:JobStore和ThreadPool
5. JobStore用来存储运行时信息,包括Trigger,Schduler,JobDetail,业务锁等,它有多种实现RAMJob(内存实现),JobStoreTX(JDBC,事务由Quartz管理)等
6. ThreadPool就是线程池,Quartz有自己的线程池实现,所有任务的都会由线程池执行
7. Scheduler是由SchdulerFactory创建,它有两个实现:DirectSchedulerFactory和 StdSchdulerFactory
8. DirectSchedulerFactory可以用来在代码里定制你自己的Schduler参数
9. StdSchdulerFactory是直接读取classpath下的quartz.properties(不存在就都使用默认值)配置来实例化Schduler.通常来讲,使用StdSchdulerFactory也就足够了



## Trigger

* Trigger定义Job的执行规则,主要有四种触发器,其中SimpleTrigger和CronTrigger触发器用的最多
* 所有Trigger都包含了StartTime和endTIme这两个属性,用来指定Trigger被触发的时间区间
* 所有Trigger都可以设置MisFire策略,该策略是对于由于系统奔溃或任务时间过长等原因导致Trigger在应该触发的时间没有触发,且超过misfireThreshold的值(默认1分钟,未超过就立即执行)时就算misfire
* 激活失败指令(Misfire Instructions)指定了MisFire发生时调度器应当如何处理
* 所有类型的触发器都有一个默认的指令-Trigger.MISFIRE_INSTRUCTION_SMART_POLICY,但是该策略对于不同类型的触发器其具体行为是不同的
  * SimpleTrigger,该策略将根据触发器实例的状态和配置来决定其行为
    * 如果Repeat Count=0:只执行一次,instruction = MISFIRE_INSTRUCTION_FIRE_NOW
    * 如果Repeat Count=REPEAT_INDEFINITELY:无限次执行,instruction = MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT
    * 如果Repeat Count>0:执行多次(有限),instruction =MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT
  * CronTrigger
    * MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:默认策略,立刻执行一次,然后就按照正常的计划执行
    * MISFIRE_INSTRUCTION_DO_NOTHING:目前不执行,然后就按照正常的计划执行.如果下次执行时间超过了end time,就没有执行机会了



### SimpleTrigger

* 从某一个时间开始,以一定的时间间隔来执行任务.它主要有两个属性
  * repeatInterval:重复的时间间隔
  * repeatCount重复的次数,实际上执行的次数是n+1,因为在startTime的时候会执行一次
* 常见策略
  * MISFIRE_INSTRUCTION_FIRE_NOW:立刻执行,对于不会重复执行的任务,这是默认的处理策略
  * MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT:在下一个激活点执行,且超时期内错过的执行机会作废

  * MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_COUNT:立即执行,且超时期内错过的执行机会作废

  * MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT:在下一个激活点执行,并重复到指定的次数

  * MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_COUNT:立即执行,并重复到指定的次数

  * MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:忽略所有的超时状态,按照触发器的策略执行




### CronTrigger

* 适合于复杂的任务,使用cron表达式来定义执行规则



### CalendarIntervalTrigger

* 类似于SimpleTrigger,指定从某一个时间开始,以一定的时间间隔执行的任务.但是CalendarIntervalTrigger执行任务的时间间隔比SimpleTrigger要丰富,它支持的间隔单位有秒,分钟,小时,天,月,年,星期.相较于SimpleTrigger有两个优势:
  * 更方便,比如每隔1小时执行,你不用自己去计算1小时等于多少毫秒
  * 支持不是固定长度的间隔,比如间隔为月和年.但劣势是精度只能到秒.它的主要两个属性:
    * interval:执行间隔
    * intervalUnit:执行间隔的单位(秒,分钟,小时,天,月,年,星期)



### DailyTimeIntervalTrigger

* 指定每天的某个时间段内,以一定的时间间隔执行任务,并且它可以支持指定星期.它适合的任务类似于:指定每天9:00至18:00,每隔70秒执行一次,并且只要周一至周五执行.属性如下
  * startTimeOfDay:每天开始时间
  * endTimeOfDay:每天结束时间
  * daysOfWeek:需要执行的星期
  * interval:执行间隔
  * intervalUnit:执行间隔的单位(秒,分钟,小时,天,月,年,星期)
  * repeatCount:重复次数



## Job

1. Job是一个任务接口,开发者定义自己的任务须实现该接口重写execute(JobExecutionContext context)方法
2. JobExecutionContext中提供了调度上下文的各种信息
3. Job中的任务有可能并发执行,例如任务的执行时间过长,而每次触发的时间间隔太短,则会导致任务会被并发执行如果是并发执行,就需要一个数据库锁去避免一个数据被多次处理
4. 在execute()方法上添加注解@DisallowConcurrentExecution解决这个问题



## JobDetail

1. Quartz在每次执行Job时,都重新创建一个Job实例,所以它不直接接受一个Job的实例
2. 它接收一个Job实现类,以便运行时通过newInstance()的反射机制实例化Job
3. 因此需要通过一个类来描述Job的实现类及其它相关的静态信息,如Job名字,描述,关联监听器等信息
4. JobDetail承担了这一角色,所以说JobDetail是任务的定义,而Job是任务的执行逻辑



## Calendar

1. Calendar:org.quartz.Calendar和java.util.Calendar不同,它是一些日历特定时间点的集合
2. 可以简单地将org.quartz.Calendar看作java.util.Calendar的集合
3. java.util.Calendar代表一个日历时间点
4. 一个Trigger可以和多个Calendar关联,以便排除或包含某些时间点
5. 主要有以下Calendar
   1. HolidayCalendar指定特定的日期,比如20140613精度到天
   2. DailyCalendar指定每天的时间段(rangeStartingTime,rangeEndingTime),格式是HH:MM[:SS[:mmm]]也就是最大精度可以到毫秒
   3. WeeklyCalendar指定每星期的星期几,可选值比如为java.util.Calendar.SUNDAY精度是天
   4. MonthlyCalendar指定每月的几号可选值为1-31精度是天
   5. AnnualCalendar 指定每年的哪一天使用方式如上例精度是天
   6. CronCalendar指定Cron表达式精度取决于Cron表达式,也就是最大精度可以到秒



## JobDataMap

用来保存JobDetail运行时的信息,JobDataMap的使用:

> usingJobData("key","value")或者getJobDataMap("key","value")



## SimpleScheduleBuilder

1. 使用quartz已经创建好的cron表达式来执行定时任务,适用于简单的定时任务
2. SimpleTrigger中的misfire方式适用于该builder



## CronScheduleBuilder

1. 自定义cron表达式,适用于复杂的定时任务

2. CronTrigger中的misfire方式适用于该builder

   ```java
   CronScheduleBuilder.cronSchedule("自定义cron表达式,可以到年")
       // misfire策略,默认是立即执行一次,之后按计划执行
       .withMisfireHandlingInstructionFireAndProceed()
       // 目前不执行,之后按照计划执行
       // .withMisfireHandlingInstructionDoNothing()
       // 忽略这种异常
       // .withMisfireHandlingInstructionIgnoreMisfires();
   ```

