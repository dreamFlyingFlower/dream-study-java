# BPMN 2.0



# 概述





# 定义流程



* BPMN 2.0根节点是`definitions`节点.这个元素中,可以定义多个流程定义,不过通常每个文件只包含一个流程定义,可以简化开发过程中的维护难度
* `definitions`元素最少也要包含`xmlns` 和 `targetNamespace`的声明.`targetNamespace`可以是任意值,它用来对流程实例进行分类

```xml
<definitions
             xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:activiti="http://activiti.org/bpmn"
             targetNamespace="Examples">

    <process id="myProcess" name="My First Process">
        ..
    </process>
</definitions>
```

* `process`元素有两个属性
  * **id**:**必须**,对应`ProcessDefinition`对象的**key**属性.`id`可以用来启动流程定义的流程实例, 通过`RuntimeService`的`startProcessInstanceByKey()`.和`startProcessInstanceById()`不同,这个方法使用Activiti在发布时自动生成的id,可以通过`processDefinition.getId()`获得这个值.生成的id的格式为**'key:version'**,最大长度限制为64,如果启动时抛出了`ActivitiException`,说明生成的id太长了, 需要限制流程的*key*的长度
  * **name**:这个属性是**可选的**,对应`ProcessDefinition`的*name*属性.引擎自己不会使用这个属性,它可以用来在用户接口显示便于阅读的名称



# 用例

用例很直接:我们有一个公司,就叫BPMCorp.在BPMCopr中,每个月都要给公司领导一个金融报表.由会计部门负责.当报表完成时,一个上级领导需要审批文档, 然后才能发给所有领导



## 流程图

上面描述的业务流程可以用Activiti Designer进行可视化设计

![](004.png)

空开始事件(左侧圆圈),后面是两个用户任务:**制作月度财报**和**验证月度财报**,最后是空结束事件(右侧粗线圆)



## XML内容

业务流程的XML内容(*FinancialReportProcess.bpmn20.xml*)如下所示,很容易找到流程的主要元素

- (空)开始事件是流程的*入口*
- 用户任务是流程中与操作者相关的任务声明.第一个任务分配给*accountancy*组,第二个任务分配给*management*组
- 当流程达到空结束事件就会结束
- 这些元素都使用连线链接,连线拥有`source`和 `target`属性,定义了连线的*方向*

```xml
<definitions id="definitions"
             targetNamespace="http://activiti.org/bpmn20"
             xmlns:activiti="http://activiti.org/bpmn"
             xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

    <process id="financialReport" name="Monthly financial report reminder process">
        <startEvent id="theStart" />
        <sequenceFlow id='flow1' sourceRef='theStart' targetRef='writeReportTask' />
        <userTask id="writeReportTask" name="Write monthly financial report" >
            <documentation>
                Write monthly financial report for publication to shareholders.
            </documentation>
            <potentialOwner>
                <resourceAssignmentExpression>
                    <formalExpression>accountancy</formalExpression>
                </resourceAssignmentExpression>
            </potentialOwner>
        </userTask>
        <sequenceFlow id='flow2' sourceRef='writeReportTask' targetRef='verifyReportTask' />
        <userTask id="verifyReportTask" name="Verify monthly financial report" >
            <documentation>
           Verify monthly financial report composed by the accountancy department.
           This financial report is going to be sent to all the company shareholders.
            </documentation>
            <potentialOwner>
                <resourceAssignmentExpression>
                    <formalExpression>management</formalExpression>
                </resourceAssignmentExpression>
            </potentialOwner>
        </userTask>
        <sequenceFlow id='flow3' sourceRef='verifyReportTask' targetRef='theEnd' />
        <endEvent id="theEnd" />
    </process>
</definitions>
```



## 启动流程实例

* 创建好了业务流程的**流程定义**,有了这个流程定义,就可以创建**流程实例**了.这时,一个流程实例对应了特定月度财报的创建和审批.所有流程实例都共享同一个流程定义
* 为了使用流程定义创建流程实例,首先要**发布**业务流程,这意味着两方面:
  * 流程定义会保存到持久化的数据存储里,是为Activiti引擎特别配置.所以部署好业务流程就能确认引擎重启后还能找到流程定义
  * BPMN 2.0流程文件会解析成内存对象模型,可以通过Activiti API操作
* 有很多种方式可以进行发布,一种方式是通过下面的API

```java
Deployment deployment = repositoryService.createDeployment()
    .addClasspathResource("FinancialReportProcess.bpmn20.xml")
    .deploy();
```

* 现在可以启动一个新流程实例,使用定义在流程定义里的`id`(对应XML文件中的process元素).注意这里的`id`对于Activiti来说, 应该叫做**key**

```java
ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");
```

* 这会创建一个流程实例,首先进入开始事件
* 开始事件之后,它会沿着所有的外出连线执行,到达第一个任务(制作月度财报)
* Activiti会把一个任务保存到数据库里.这时,分配到这个任务的用户或群组会被解析,也会保存到数据库里
* Activiti引擎会继续执行流程的环节,除非遇到一个*等待状态*,比如用户任务
* 在等待状态下,当前的流程实例的状态会保存到数据库中,直到用户决定完成任务才能改变这个状态.这时,引擎继续执行,直到遇到下一个等待状态,或流程结束
* 如果中间引擎重启或崩溃,流程状态也会安全的保存在数据库里
* 任务创建之后,`startProcessInstanceByKey`会在到达用户任务 这个*等待状态*之后才会返回.这时,任务分配给了一个组, 这意味着这个组是执行这个任务的**候选**组

```java
public static void main(String[] args) {
    // Create Activiti process engine
    ProcessEngine processEngine = ProcessEngineConfiguration
        .createStandaloneProcessEngineConfiguration()
        .buildProcessEngine();
    // Get Activiti services
    RepositoryService repositoryService = processEngine.getRepositoryService();
    RuntimeService runtimeService = processEngine.getRuntimeService();
    // Deploy the process definition
    repositoryService.createDeployment()
        .addClasspathResource("FinancialReportProcess.bpmn20.xml")
        .deploy();
    // Start a process instance
    runtimeService.startProcessInstanceByKey("financialReport");
}
```



## 任务列表

可以通过`TaskService`来获得任务

```java
List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("kermit").list();
```

注意传入的用户必须是*accountancy*组的一个成员,要和流程定义中向对应

```xml
<potentialOwner>
    <resourceAssignmentExpression>
        <formalExpression>accountancy</formalExpression>
    </resourceAssignmentExpression>
</potentialOwner>
```

也可以使用群组名称,通过任务查询API来获得相关的结果

```java
TaskService taskService = processEngine.getTaskService();
List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
```

因为`ProcessEngine`使用了与demo相同的数据,可以登录到[Activiti Explorer](http://localhost:8080/activiti-explorer/) ,然后可以启动业务流程, 选择*Processes*页,在**月度财报**的**操作**列 点击**启动流程**

![](005.png)

流程会执行到第一个用户任务,因为是以kermit登录,在启动流程实例之后,就可以看到有了一个新的待领任务.选择*任务*页来查看这条新任务.即使流程被其他人启动,任务还是会被会计组里的所有人作为一个候选任务看到

![](006.png)



## 领取任务

* 现在一个会计要**认领这个任务**.认领以后,这个用户就会成为任务的**执行人**,任务会从会计组的其他成员的任务列表中消失

```java
taskService.claim(task.getId(), "fozzie");
```

* 任务会进入**认领任务人的个人任务列表**中

```java
List<Task> tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
```

* 在Activiti Explorer UI中,点击*认领*按钮,会执行相同的操作.任务会移动到登录用户的个人任务列表,任务的执行人已经变成当前登陆的用户

![](007.png)





## 完成任务

* 现在会计可以开始进行财报的工作了,报告完成后,他可以**完成任务**,意味着任务所需的所有工作都完成了

```java
taskService.complete(task.getId());
```

* 对于Activiti引擎,需要一个外部信息让流程实例继续执行.任务会把自己从运行库中删除,流程会沿着单独一个外出连线执行,移动到第二个任务.与第一个任务相同的机制会用到第二个任务上,不同的是任务是分配给 *management*组
* 在demo中,完成任务是通过点击任务列表中的*完成*按钮.因为Fozzie不是会计,我们先从Activiti Explorer注销 然后使用*kermit*登陆(他是经理).第二个任务会进入未分配任务列表



## 结束流程

* 完成第二个任务会让流程执行到结束事件,结束流程实例.流程实例和所有相关数据都会从数据库中删除

* 登录Activiti Explorer就可以进行验证,可以看到保存流程运行数据的表中已经没有数据了

![](008.png)

* 使用`historyService`判断流程已经结束

```java
HistoryService historyService = processEngine.getHistoryService();
HistoricProcessInstance historicProcessInstance =historyService
    .createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
System.out.println("Process instance end time:"+historicProcessInstance.getEndTime());
```



## 完整代码

```java
public class TenMinuteTutorial {

    public static void main(String[] args) {
        // Create Activiti process engine
        ProcessEngine processEngine = ProcessEngineConfiguration
            .createStandaloneProcessEngineConfiguration()
            .buildProcessEngine();
        // Get Activiti services
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // Deploy the process definition
        repositoryService.createDeployment()
            .addClasspathResource("FinancialReportProcess.bpmn20.xml")
            .deploy();
        // Start a process instance
        String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
        // Get the first task
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        for (Task task : tasks) {
            System.out.println("Following task is available for accountancy group: " + task.getName());
            // claim it
            taskService.claim(task.getId(), "fozzie");
        }
        // Verify Fozzie can now retrieve the task
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        for (Task task : tasks) {
            System.out.println("Task for fozzie: " + task.getName());

            // Complete the task
            taskService.complete(task.getId());
        }
        System.out.println("Number of tasks for fozzie: "+ taskService.createTaskQuery().taskAssignee("fozzie").count());
        // Retrieve and claim the second task
        tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for (Task task : tasks) {
            System.out.println("Following task is available for accountancy group: " + task.getName());
            taskService.claim(task.getId(), "kermit");
        }
        // Completing the second task ends the process
        for (Task task : tasks) {
            taskService.complete(task.getId());
        }
        // verify that the process is actually finished
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance =
historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();
        System.out.println("Process instance end time: "+historicProcessInstance.getEndTime());
    }
}
```

