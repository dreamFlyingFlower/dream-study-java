# ActivitiRestAPI



# 概述



* 在Activiti7版本之前,activiti-rest.war包含了内置的REST API,将该JAR部署到Tomcat即可访问,访问的url为ip:port/activiti-app/service(版本6),各个版本不一样

* REST API使用JSON格式,它是基于[Restlet](http://www.restlet.org)开发的

* 默认所有REST资源都需要进行登录认证后才能使用.可以在请求头中添加`Authorization:Basic ...`,或在url中包含用户名密码,如:`http://username:password@localhost...`

* 可以将Basic认证与HTTPS一起使用

* 可以认证后删除对应资源,或添加额外的授权给一个认证的用户.可以实现RestAuthenticator接口

  * requestRequiresAuthentication(Request request):在请求认证检查之前调用(通过头部传递合法的账号和密码).如果返回true,这个方法就需要认证才能访问.如果返回false,无论请求是否认证都可以访问.如果返回false,就不会为这个方法调用`isRequestAuthorized`
  * isRequestAuthorized(Request request):在用户已经通过Activiti账号管理认证后,但是在请求实际之前调用.可以用来检查认证用户是否可以访问对应请求.如果返回true,会允许请求执行.如果返回true,请求不会执行,客户端会收到对应的错误
  * 自定义的RestAuthenticator需要设置到RestletServlet的ActivitiRestServicesApplication中.最简单的方法是创建`ActivitiRestServicesApplication`的子类,并在servlet-mapping中设置自定义的类名

  ```xml
  <!-- Restlet adapter -->
  <servlet>
      <servlet-name>RestletServlet</servlet-name>
      <servlet-class>org.restlet.ext.servlet.ServerServlet</servlet-class>
      <init-param>
          <!-- Application class name -->
          <param-name>org.restlet.application</param-name>
          <param-value>com.my.company.CustomActivitiRestServicesApplication</param-value>
      </init-param>
  </servlet>
  ```



# 通用参数

| 参数  | 默认值             | 描述                                           |
| ----- | ------------------ | ---------------------------------------------- |
| sort  | 根据查询实现而不同 | 查询的名称，对于不同的查询实现，默认值也不同。 |
| order | asc                | 排序的方式，可以为'asc'或'desc'。              |
| start | 0                  | 分页查询开始的值，默认从0开始。                |
| size  | 10                 | 分页查询每页显示的记录数。默认为10。           |



# 部署



## 部署列表

```http
GET repository/deployments
```



| 参数  | 是否必须   | 值  | 描述     |
| ---- | ---------------- | ---- | ------------------------------ |
| name| 否       | String| 只返回指定名称的部署         |
| nameLike| 否       | String| 只返回名称与指定值相似的部署 |
| category| 否       | String| 只返回指定分类的部署         |
| categoryNotEquals| 否       | String| 只返回与指定分类不同的部署   |
| sort| 否       | id(默认),name或deploytime | 排序,与order一起使用 |
|  |          |                                    |                                |

```json
{
    "data": [
        {
            "id": "10",
            "name": "activiti-examples.bar",
            "deploymentTime": "2010-10-13T14:54:26.750+02:00",
            "category": "examples",
            "url": "http://localhost:8081/service/repository/deployments/10"
        }
    ],
    "total": 1,
    "start": 0,
    "sort": "id",
    "order": "asc",
    "size": 1
}
```



## 获得一个部署

```http
GET repository/deployments/{deploymentId}
```



| 参数         | 是否必须 | 值     | 描述         |
| ------------ | -------- | ------ | ------------ |
| deploymentId | 是       | String | 获取部署的id |

  

```json
{
    "id": "10",
    "name": "activiti-examples.bar",
    "deploymentTime": "2010-10-13T14:54:26.750+02:00",
    "category": "examples",
    "url": "http://localhost:8081/service/repository/deployments/10"
}
```



## 创建新部署

```http
POST repository/deployments 
```

请求体包含的数据类型应该是*multipart/form-data*.请求里应该只包含一个文件,其他额外的任务都会被忽略.部署的名称就是文件域的名称.如果需要在一个部署中包含多个资源,把这些文件压缩成zip包,并要确认文件名是以`.bar`或`.zip`结尾

 

**成功响应体:** 

```json
{
    "id": "10",
    "name": "activiti-examples.bar",
    "deploymentTime": "2010-10-13T14:54:26.750+02:00",
    "category": null,
    "url": "http://localhost:8081/service/repository/deployments/10"
}
```



## 删除部署

```http
DELETE repository/deployments/{deploymentId}
```



| 参数         | 是否必须 | 值     | 描述         |
| ------------ | -------- | ------ | ------------ |
| deploymentId | 是       | String | 删除的部署id |



## 列出部署内的资源

```http
GET repository/deployments/{deploymentId}/resources
```



| 参数         | 是否必须 | 值     | 描述             |
| ------------ | -------- | ------ | ---------------- |
| deploymentId | 是       | String | 获取资源的部署id |

 

```json
[
    {
        "id": "diagrams/my-process.bpmn20.xml",
        "url": "http://localhost:8081/activiti-rest/service/repository/deployments/10/resources/diagrams%2Fmy-process.bpmn20.xml",
        "dataUrl": "http://localhost:8081/activiti-rest/service/repository/deployments/10/resourcedata/diagrams%2Fmy-process.bpmn20.xml",
        "mediaType": "text/xml",
        "type": "processDefinition"
    },
    {
        "id": "image.png",
        "url": "http://localhost:8081/activiti-rest/service/repository/deployments/10/resources/image.png",
        "dataUrl": "http://localhost:8081/activiti-rest/service/repository/deployments/10/resourcedata/image.png",
        "mediaType": "image/png",
        "type": "resource"
    }
]
```

- `mediaType`:包含资源的media-type,这是使用`MediaTypeResolver`处理的,默认已经支持了一些有限的mime-type映射
- `type`:资源类型
  - `resource`:原始资源
  - `processDefinition`:包含一个或多个流程定义的资源,它会被发布器处理
  - `processImage`:展示一个已发布流程定义的图形布局
- dataUrl:包含了用来获取二进制资源的真实URL



## 获取部署资源

```http
GET repository/deployments/{deploymentId}/resources/{resourceId}
```



| 参数         | 是否必须 | 值     | 描述                                               |
| ------------ | -------- | ------ | -------------------------------------------------- |
| deploymentId | 是       | String | 部署ID是请求资源的一部分                           |
| resourceId   | 是       | String | 获取资源的ID.确保对资源ID进行编码的情况下,包含斜杠 |



```json
{
    "id": "diagrams/my-process.bpmn20.xml",
    "url": "http://localhost:8081/activiti-rest/service/repository/deployments/10/resources/diagrams%2Fmy-process.bpmn20.xml",
    "dataUrl": "http://localhost:8081/activiti-rest/service/repository/deployments/10/resourcedata/diagrams%2Fmy-process.bpmn20.xml",
    "mediaType": "text/xml",
    "type": "processDefinition"
}
```



## 获取部署资源的内容

```http
GET repository/deployments/{deploymentId}/resourcedata/{resourceId}
```



| 参数         | 是否必须 | 值     | 描述                                               |
| ------------ | -------- | ------ | -------------------------------------------------- |
| deploymentId | 是       | String | 部署ID是请求资源的一部分                           |
| resourceId   | 是       | String | 获取资源的ID.确保对资源ID进行编码的情况下,包含斜杠 |



根据请求的资源响应体将包含二进制的资源内容,响应体的content-type的'mimeType'属性将会和资源的返回类型相同.同样,响应头设置content-disposition,允许浏览器下载该文件而不是去显示它



# 流程定义



## 流程定义列表

```http
GET repository/process-definitions
```



| 参数              | 是否必须 | 值      | 描述                                                         |
| :---------------- | -------- | ------- | ------------------------------------------------------------ |
| version           | 否       | integer | 只返回给定版本的流程定义                                     |
| name              | 否       | String  | 只返回给定名称的流程定义                                     |
| nameLike          | 否       | String  | 只返回与给定名称匹配的流程定义                               |
| key               | 否       | String  | 只返回给定key的流程定义                                      |
| keyLike           | 否       | String  | 只返回与给定key匹配的流程定义                                |
| resourceName      | 否       | String  | 只返回给定资源名称的流程定义                                 |
| resourceNameLike  | 否       | String  | 只返回与给定资源名称匹配的流程定义                           |
| category          | 否       | String  | 只返回给定分类的流程定义                                     |
| categoryLike      | 否       | String  | 只返回与给定分类匹配的流程定义                               |
| categoryNotEquals | 否       | String  | 只返回非给定分类的流程定义                                   |
| deploymentId      | 否       | String  | 只返回包含在与给定id一致的部署中的流程定义                   |
| startableByUser   | 否       | String  | 只返回给定用户可以启动的流程定义                             |
| latest            | 否       | Boolean | 只返回最新的流程定义版本.只能与'key'或'keyLike'参数一起使用,如果使用了其他参数会返回400的响应 |
| suspended         | 否       | Boolean | `true`,返回挂起的流程定义.`false`,返回活动的流程定义         |
| sort              | 否       | String  | 默认name,id,key,category,deploymentId,version,可以与order一起使用 |
|                   |          |         |                                                              |

```json
{
    "data": [
        {
            "id" : "oneTaskProcess:1:4",
            "url" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
            "version" : 1,
            "key" : "oneTaskProcess",
            "category" : "Examples",
            "suspended" : false,
            "name" : "The One Task Process",
            "description" : "This is a process for testing purposes",
            "deploymentId" : "2",
            "deploymentUrl" : "http://localhost:8081/repository/deployments/2",
            "graphicalNotationDefined" : true,
            "resource" : "http://localhost:8182/repository/deployments/2/resources/testProcess.xml",
            "diagramResource" : "http://localhost:8182/repository/deployments/2/resources/testProcess.png",
            "startFormDefined" : false
        }
    ],
    "total": 1,
    "start": 0,
    "sort": "name",
    "order": "asc",
    "size": 1
}
```

- `graphicalNotationDefined`:流程定义包含图形信息,BPMN,DI
- `resource`:包含实际部署的BPMN2.0 xml
- `diagramResource`:包含流程的图形内容,如果没有图形就返回null



## 获得一个流程定义

```http
GET repository/process-definitions/{processDefinitionId}
```



| 参数                | 是否必须 | 值     | 描述                   |
| ------------------- | -------- | ------ | ---------------------- |
| processDefinitionId | 是       | String | 希望获取的流程定义的id |



```json
{
    "id" : "oneTaskProcess:1:4",
    "url" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
    "version" : 1,
    "key" : "oneTaskProcess",
    "category" : "Examples",
    "suspended" : false,
    "name" : "The One Task Process",
    "description" : "This is a process for testing purposes",
    "deploymentId" : "2",
    "deploymentUrl" : "http://localhost:8081/repository/deployments/2",
    "graphicalNotationDefined" : true,
    "resource" : "http://localhost:8182/repository/deployments/2/resources/testProcess.xml",
    "diagramResource" : "http://localhost:8182/repository/deployments/2/resources/testProcess.png",
    "startFormDefined" : false
}
```

- `graphicalNotationDefined`:表示流程定义包含图形信息,BPMN,DI
- `resource`:包含实际部署的BPMN2.0 xml
- `diagramResource`:包含流程的图形内容,如果没有图形就返回null



## 更新流程定义的分类

```http
PUT repository/process-definitions/{processDefinitionId}
```



```json
{
    "category" : "updatedcategory"
}
```



## 获得一个流程定义的资源内容

```http
GET repository/process-definitions/{processDefinitionId}/resourcedata
```



| 参数                | 是否必须 | 值     | 描述                           |
| ------------------- | -------- | ------ | ------------------------------ |
| processDefinitionId | 是       | String | 期望获得资源数据的流程定义的id |

 

## 获得流程定义的BPMN模型

```http
GET repository/process-definitions/{processDefinitionId}/model
```



| 参数                | 是否必须 | 值     | 描述                       |
| ------------------- | -------- | ------ | -------------------------- |
| processDefinitionId | 是       | String | 期望获得模型的流程定义的id |

 

```json
{
    "processes":[
        {
            "id":"oneTaskProcess",
            "xmlRowNumber":7,
            "xmlColumnNumber":60,
            "extensionElements":{

            },
            "name":"The One Task Process",
            "executable":true,
            "documentation":"One task process description",

            ...
        }
            ]
        }
```



## 暂停流程定义

```http
PUT repository/process-definitions/{processDefinitionId}
```



```json
{
  "action" : "suspend",
  "includeProcessInstances" : "false",
  "date" : "2013-04-15T00:42:12Z"
}
```



| 参数                    | 是否必须 | 描述                                                         |
| ----------------------- | -------- | ------------------------------------------------------------ |
| action                  | 是       | 执行的动作.`activate` 或 `suspend`                           |
| includeProcessInstances | 否       | 是否把正在运行的流程暂停或激活.如果忽略,就不改变流程实例的状态 |
| date                    | 否       | 执行暂停或激活的日期.如果忽略,会立即执行暂停或激活           |



## 激活流程定义

```http
PUT repository/process-definitions/{processDefinitionId}
```



```json
{
  "action" : "activate",
  "includeProcessInstances" : "true",
  "date" : "2013-04-15T00:42:12Z"
}
```



## 获得流程定义的所有候选启动者

```http
GET repository/process-definitions/{processDefinitionId}/identitylinks
```



| 参数                | 是否必须 | 值     | 描述                             |
| ------------------- | -------- | ------ | -------------------------------- |
| processDefinitionId | 是       | String | 期望获得IdentityLink的流程定义id |

 

```json
[
    {
        "url":"http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4/identitylinks/groups/admin",
        "user":null,
        "group":"admin",
        "type":"candidate"
    },
    {
        "url":"http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4/identitylinks/users/kermit",
        "user":"kermit",
        "group":null,
        "type":"candidate"
    }
]
```



## 为流程定义添加一个候选启动者

```http
POST repository/process-definitions/{processDefinitionId}/identitylinks
```



| 参数                | 是否必填 | 数据   | 描述         |
| ------------------- | -------- | ------ | ------------ |
| processDefinitionId | 是       | String | 流程定义的id |

 

**请求体-用户:** 

```json
{
  "userId" : "kermit"
}
```

**请求体-组:** 

```json
{
  "groupId" : "sales"
}
```



```json
{
    "url":"http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4/identitylinks/users/kermit",
    "user":"kermit",
    "group":null,
    "type":"candidate"
}
```



## 删除流程定义的候选启动者

```http
DELETE repository/process-definitions/{processDefinitionId}/identitylinks/{family}/{identityId}
```



| 参数                | 是否必填 | 数据   | 描述                                        |
| ------------------- | -------- | ------ | ------------------------------------------- |
| processDefinitionId | 是       | String | 流程定义的id                                |
| family              | 是       | String | `users` 或 `groups`,依赖IdentityLink的类型  |
| identityId          | 是       | String | 需要删除的候选创建者的身份的userId或groupId |



```json
{
    "url":"http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4/identitylinks/users/kermit",
    "user":"kermit",
    "group":null,
    "type":"candidate"
}
```



## 获得流程定义的一个候选启动者

```http
GET repository/process-definitions/{processDefinitionId}/identitylinks/{family}/{identityId}
```



| 参数                | 是否必填 | 数据   | 描述                                        |
| ------------------- | -------- | ------ | ------------------------------------------- |
| processDefinitionId | 是       | String | 流程定义的id                                |
| family              | 是       | String | `users` 或 `groups`,依赖IdentityLink的类型  |
| identityId          | 是       | String | 用来获得候选启动者的身份的userId 或 groupId |



```json
{
    "url":"http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4/identitylinks/users/kermit",
    "user":"kermit",
    "group":null,
    "type":"candidate"
}
```



# 模型



## 获得模型列表

```http
GET repository/models
```


| 参数              | 必须 | 值      | 描述                                                       |
| ----------------- | ---- | ------- | ---------------------------------------------------------- |
| id                | 否   | String  | 指返回指定id的模型                                         |
| category          | 否   | String  | 只返回指定分类的模型                                       |
| categoryLike      | 否   | String  | 只返回与给定分类匹配的模型,使用`%`作为通配符               |
| categoryNotEquals | 否   | String  | 只返回非指定分类的模型                                     |
| name              | 否   | String  | 只返回指定名称的模型                                       |
| nameLike          | 否   | String  | 只返回与指定名称匹配的模型,使用`%`作为通配符               |
| key               | 否   | String  | 只返回指定key的模型                                        |
| deploymentId      | 否   | String  | 只返回包含在指定部署包中的模型                             |
| version           | 否   | Integer | 只返回指定版本的模型                                       |
| latestVersion     | 否   | Boolean | `true`返回最新版本,最好与`key`一起使用.`false`返回所有版本 |
| deployed          | 否   | Boolean | `true`返回已部署.`false`返回未部署的(deploymentId为null)   |
| sort              | 否   | String  | 默认id,category,createTime,key,lastUpdateTime,name,version |
|                   |      |         |                                                            |



```json
{
    "data":[
        {
            "name":"Model name",
            "key":"Model key",
            "category":"Model category",
            "version":2,
            "metaInfo":"Model metainfo",
            "deploymentId":"7",
            "id":"10",
            "url":"http://localhost:8182/repository/models/10",
            "createTime":"2013-06-12T14:31:08.612+0000",
            "lastUpdateTime":"2013-06-12T14:31:08.612+0000",
            "deploymentUrl":"http://localhost:8182/repository/deployments/7"
        },

        ...

    ],
        "total":2,
        "start":0,
        "sort":"id",
        "order":"asc",
        "size":2
        }
```



## 获得一个模型

```http
GET repository/models/{modelId}
```



| 参数    | 是否必须 | 值     | 描述             |
| ------- | -------- | ------ | ---------------- |
| modelId | 是       | String | 希望获得的模型id |

 

```json
{
    "id":"5",
    "url":"http://localhost:8182/repository/models/5",
    "name":"Model name",
    "key":"Model key",
    "category":"Model category",
    "version":2,
    "metaInfo":"Model metainfo",
    "deploymentId":"2",
    "deploymentUrl":"http://localhost:8182/repository/deployments/2",
    "createTime":"2013-06-12T12:31:19.861+0000",
    "lastUpdateTime":"2013-06-12T12:31:19.861+0000"
}
```



## 更新模型

```http
PUT repository/models/{modelId}
```



```json
{
    "name":"Model name",
    "key":"Model key",
    "category":"Model category",
    "version":2,
    "metaInfo":"Model metainfo",
    "deploymentId":"2"
}
```



```json
{
   "id":"5",
   "url":"http://localhost:8182/repository/models/5",
   "name":"Model name",
   "key":"Model key",
   "category":"Model category",
   "version":2,
   "metaInfo":"Model metainfo",
   "deploymentId":"2",
   "deploymentUrl":"http://localhost:8182/repository/deployments/2",
   "createTime":"2013-06-12T12:31:19.861+0000",
   "lastUpdateTime":"2013-06-12T12:31:19.861+0000"
}
```



## 新建模型

```http
POST repository/models
```



```json
{
   "name":"Model name",
   "key":"Model key",
   "category":"Model category",
   "version":1,
   "metaInfo":"Model metainfo",
   "deploymentId":"2"
}
```



```json
{
   "id":"5",
   "url":"http://localhost:8182/repository/models/5",
   "name":"Model name",
   "key":"Model key",
   "category":"Model category",
   "version":1,
   "metaInfo":"Model metainfo",
   "deploymentId":"2",
   "deploymentUrl":"http://localhost:8182/repository/deployments/2",
   "createTime":"2013-06-12T12:31:19.861+0000",
   "lastUpdateTime":"2013-06-12T12:31:19.861+0000"
}
```



## 删除模型

```http
DELETE repository/models/{modelId}
```



## 获得模型的可编译源码

```http
GET repository/models/{modelId}/source
```



**成功响应体:** 响应体包含了模型的原始可编译源码,无论源码的内容是什么,响应的content-type都设置为`application/octet-stream`



## 设置模型的可编辑源码

```http
PUT repository/models/{modelId}/source
```

**请求体:** 请求应该是`multipart/form-data`类型,应该只有一个文件区域,包含源码的二进制内容

**成功响应体:** 响应体包含了模型的原始可编译源码,无论源码的内容是什么,响应的content-type都设置为`application/octet-stream`



## 获得模型的附加可编辑源码

```http
GET repository/models/{modelId}/source-extra
```

 

**成功响应体:**响应体包含了模型的原始可编译源码.无论附加源码的内容是什么,响应的content-type都设置为`application/octet-stream`



## 设置模型的附加可编辑源码

```http
PUT repository/models/{modelId}/source-extra
```

**请求体:** 请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容

**成功响应体:**响应体包含了模型的原始可编译源码.无论附加源码的内容是什么,响应的content-type都设置为`application/octet-stream`



# 流程实例



## 获得流程实例

```http
GET runtime/process-instances/{processInstanceId}
```



```json
{
    "id":"7",
    "url":"http://localhost:8182/runtime/process-instances/7",
    "businessKey":"myBusinessKey",
    "suspended":false,
    "processDefinitionUrl":"http://localhost:8182/repository/process-definitions/processOne%3A1%3A4",
    "activityId":"processTask"
}
```



## 删除流程实例

```http
DELETE runtime/process-instances/{processInstanceId}
```



| 参数              | 是否必须 | 值     | 描述                 |
| ----------------- | -------- | ------ | -------------------- |
| processInstanceId | 是       | String | 希望删除的流程实例id |

 

## 激活或挂起流程实例

```http
PUT runtime/process-instances/{processInstanceId}
```



| 参数              | 是否必须 | 值     | 描述                       |
| ----------------- | -------- | ------ | -------------------------- |
| processInstanceId | 是       | String | 希望激活或挂起的流程实例id |

 

**请求响应体-挂起:** 

```json
{
   "action":"suspend"
}
```

**请求响应体-激活:** 

```json
{
   "action":"activate"
}
```

 

## 启动流程实例

```http
POST runtime/process-instances
```



**请求体(使用流程定义id启动):**

```json
{
   "processDefinitionId":"oneTaskProcess:1:158",
   "businessKey":"myBusinessKey",
   "variables": [
      {
        "name":"myVar",
        "value":"This is a variable",
      },
 
      ...
   ]
}
```



**请求体(使用流程定义key启动):**

```json
{
   "processDefinitionKey":"oneTaskProcess",
   "businessKey":"myBusinessKey",
   "variables": [
      {
        "name":"myVar",
        "value":"This is a variable",
      },
 
      ...
   ]
}
```



**请求体(使用message启动):**

```json
{
   "processDefinitionKey":"newOrderMessage",
   "businessKey":"myBusinessKey",
   "variables": [
      {
        "name":"myVar",
        "value":"This is a variable",
      },
 
      ...
   ]
}
```

* 请求体中只能使用`processDefinitionId`,`processDefinitionKey`或`message`三者之一.`businessKey`和`variables`可选

 

```json
{
   "id":"7",
   "url":"http://localhost:8182/runtime/process-instances/7",
   "businessKey":"myBusinessKey",
   "suspended":false,
   "processDefinitionUrl":"http://localhost:8182/repository/process-definitions/processOne%3A1%3A4",
   "activityId":"processTask"
}
```



## 显示流程实例列表

```http
GET runtime/process-instances
```



| 参数                    | 是否必须 | 值      | 描述                                                         |
| ----------------------- | -------- | ------- | ------------------------------------------------------------ |
| id                      | 否       | String  | 返回指定id的流程实例                                         |
| processDefinitionKey    | 否       | String  | 返回指定流程定义key的流程实例                                |
| processDefinitionId     | 否       | String  | 返回指定流程定义id的流程实例                                 |
| businessKey             | 否       | String  | 返回指定businessKey的流程实例                                |
| involvedUser            | 否       | String  | 返回指定用户参与过的流程实例                                 |
| suspended               | 否       | Boolean | `true`返回挂起的流程实例.`false`返回未挂起的流程实例         |
| superProcessInstanceId  | 否       | String  | 返回指定上级流程实例id的流程实例(对应call-activity)          |
| subProcessInstanceId    | 否       | String  | 返回指定子流程id的流程实例(对应call-activity)                |
| excludeSubprocesses     | 否       | Boolean | 返回非子流程的流程实例                                       |
| includeProcessVariables | 否       | Boolean | 表示结果中包含流程变量                                       |
| sort                    | 否       | String  | 排序字段,应该为`id`,`processDefinitionId` 或 `processDefinitionKey`三者之一 |
|                         |          |         |                                                              |

```json
{
   "data":[
      {
         "id":"7",
         "url":"http://localhost:8182/runtime/process-instances/7",
         "businessKey":"myBusinessKey",
         "suspended":false,
         "processDefinitionUrl":"http://localhost:8182/repository/process-definitions/processOne%3A1%3A4",
         "activityId":"processTask"
      },
 
      ...
   ],
   "total":2,
   "start":0,
   "sort":"id",
   "order":"asc",
   "size":2
}
```



## 查询流程实例

```http
POST query/process-instances
```



```json
{
  "processDefinitionKey":"oneTaskProcess",
  "variables":
  [
    {
        "name" : "myVariable",
        "value" : 1234,
        "operation" : "equals",
        "type" : "long"
    },
    ...
  ],
  ...
}
```

* 请求体可以包含所有用于显示流程实例列表中的查询参数.除此之外,查询条件中也可以使用变量列表



```json
{
   "data":[
      {
         "id":"7",
         "url":"http://localhost:8182/runtime/process-instances/7",
         "businessKey":"myBusinessKey",
         "suspended":false,
         "processDefinitionUrl":"http://localhost:8182/repository/process-definitions/processOne%3A1%3A4",
         "activityId":"processTask"
      },
 
      ...
   ],
   "total":2,
   "start":0,
   "sort":"id",
   "order":"asc",
   "size":2
}
```



## 获得流程实例的流程图

```http
GET runtime/process-instances/{processInstanceId}
```



| 参数              | 是否必须 | 值     | 描述                       |
| ----------------- | -------- | ------ | -------------------------- |
| processInstanceId | 是       | String | 希望获得流程图的流程实例id |

 

```json
{
   "id":"7",
   "url":"http://localhost:8182/runtime/process-instances/7",
   "businessKey":"myBusinessKey",
   "suspended":false,
   "processDefinitionUrl":"http://localhost:8182/repository/process-definitions/processOne%3A1%3A4",
   "activityId":"processTask"
}
```



## 获得流程实例的参与者

```http
GET runtime/process-instances/{processInstanceId}/identitylinks
```



| 参数              | 是否必须 | 值     | 描述             |
| ----------------- | -------- | ------ | ---------------- |
| processInstanceId | 是       | String | 关联的流程实例id |

 

```json
[
   {
      "url":"http://localhost:8182/runtime/process-instances/5/identitylinks/users/john/customType",
      "user":"john",
      "group":null,
      "type":"customType"
   },
   {
      "url":"http://localhost:8182/runtime/process-instances/5/identitylinks/users/paul/candidate",
      "user":"paul",
      "group":null,
      "type":"candidate"
   }
]
```

* 注意`groupId`总是null,因为只有用户才能实际参与到流程实例中



## 为流程实例添加一个参与者

```http
POST runtime/process-instances/{processInstanceId}/identitylinks
```



| 参数              | 是否必须 | 值     | 描述               |
| ----------------- | -------- | ------ | ------------------ |
| processInstanceId | 是       | String | 关联的流程实例id   |
| userId            | 是       | String | 用户编号           |
| type              | 是       | String | 类型,如participant |



```json
{
   "url":"http://localhost:8182/runtime/process-instances/5/identitylinks/users/john/customType",
   "user":"john",
   "group":null,
   "type":"customType"
}
```

* 注意`groupId`总是null,因为只有用户才能实际参与到流程实例中



## 删除一个流程实例的参与者

```http
DELETE runtime/process-instances/{processInstanceId}/identitylinks/users/{userId}/{type}
```



| 参数              | 是否必须 | 值     | 描述               |
| ----------------- | -------- | ------ | ------------------ |
| processInstanceId | 是       | String | 流程实例id         |
| userId            | 是       | String | 要删除关联的用户id |
| type              | 是       | String | 删除的关联类型     |

 

```json
{
   "url":"http://localhost:8182/runtime/process-instances/5/identitylinks/users/john/customType",
   "user":"john",
   "group":null,
   "type":"customType"
}
```

* 注意`groupId`总是null,因为只有用户才能实际参与到流程实例中



## 列出流程实例的变量

```http
GET runtime/process-instances/{processInstanceId}/variables
```



| 参数              | 是否必须 | 值     | 描述                 |
| ----------------- | -------- | ------ | -------------------- |
| processInstanceId | 是       | String | 变量对应的流程实例id |

 

```json
[
   {
      "name":"intProcVar",
      "type":"integer",
      "value":123,
      "scope":"local"
   },
   {
      "name":"byteArrayProcVar",
      "type":"binary",
      "value":null,
      "valueUrl":"http://localhost:8182/runtime/process-instances/5/variables/byteArrayProcVar/data",
      "scope":"local"
   },
 
   ...
]
```

* 当变量为二进制或序列化类型时,`valueUrl`给出了获得原始数据的URL.如果是普通变量,变量值就会直接包含在响应中.注意只会返回`local`作用域的变量,因为流程实例变量没有`global`作用域



## 获得流程实例的一个变量

```http
GET runtime/process-instances/{processInstanceId}/variables/{variableName}
```



| 参数              | 是否必须 | 值     | 描述                 |
| ----------------- | -------- | ------ | -------------------- |
| processInstanceId | 是       | String | 变量对应的流程实例id |
| variableName      | 是       | String | 获取变量的名称       |



```json
{
    "name":"intProcVar",
    "type":"integer",
    "value":123,
    "scope":"local"
}
```

* 当变量为二进制或序列化类型时,`valueUrl`给出了获得原始数据的URL.如果是普通变量,变量值就会直接包含在响应中. 注意只会返回`local`作用域的变量,因为流程实例变量没有`global`作用域



## 创建或更新流程实例变量

```http
POST runtime/process-instances/{processInstanceId}/variables
PUT runtime/process-instances/{processInstanceId}/variables
```

* 使用`POST`时,会创建所有传递的变量.如果流程实例中已经存在了其中一个变量,就会返回一个错误(409 - CONFLICT).使用`PUT`时, 流程实例中不存在的变量会被创建,已存在的变量会被更新,不会有任何错误



| 参数              | 是否必须 | 值     | 描述                 |
| ----------------- | -------- | ------ | -------------------- |
| processInstanceId | 是       | String | 变量对应的流程实例id |

```json
[
   {
      "name":"intProcVar"
      "type":"integer"
      "value":123
   },
 
   ...
]
```

* 请求体的数组中可以包含任意多个变量.关于变量格式的更多信息可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables).注意此处忽略作用域,流程实例只能设置`local`作用域



```json
[
   {
      "name":"intProcVar",
      "type":"integer",
      "value":123,
      "scope":"local"
   },
 
   ...
 
]
```



## 更新一个流程实例变量

```http
PUT runtime/process-instances/{processInstanceId}/variables/{variableName}
```



| 参数              | 是否必须 | 值     | 描述                 |
| ----------------- | -------- | ------ | -------------------- |
| processInstanceId | 是       | String | 变量对应的流程实例id |
| variableName      | 是       | String | 希望获得的变量名称   |

```
 {
    "name":"intProcVar"
    "type":"integer"
    "value":123
 }
```

* 请求体的数组中可以包含任意多个变量.关于变量格式的更多信息可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables).注意此处忽略作用域,流程实例只能设置`local`作用域

 

```json
   {
      "name":"intProcVar",
      "type":"integer",
      "value":123,
      "scope":"local"
   }
```

* 当变量为二进制或序列化类型时,`valueUrl`给出了获得原始数据的URL.如果是普通变量,变量值就会直接包含在响应中. 注意只会返回`local`作用域的变量,因为流程实例变量没有`global`作用域



## 创建一个新的二进制流程变量

```http
POST runtime/process-instances/{processInstanceId}/variables
```



| 参数              | 是否必须 | 值     | 描述                       |
| ----------------- | -------- | ------ | -------------------------- |
| processInstanceId | 是       | String | 创建新变量对应的流程实例id |

**请求体:**请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域

- `name`:必须的变量名称
- `type`:创建的变量类型.如果忽略,会假设使用`binary`,请求的二进制数据会当做二进制数组保存起来



```json
{
    "name" : "binaryVariable",
    "scope" : "local",
    "type" : "binary",
    "value" : null,
    "valueUrl" : "http://.../runtime/process-instances/123/variables/binaryVariable/data"
}
```

 

## 更新一个二进制的流程实例变量

```http
PUT runtime/process-instances/{processInstanceId}/variables
```



| 参数              | 是否必须 | 值     | 描述                       |
| ----------------- | -------- | ------ | -------------------------- |
| processInstanceId | 是       | String | 创建新变量对应的流程实例id |

**请求体:**请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域:

- `name`:必须的变量名称
- `type`:创建的变量类型.如果忽略,会假设使用`binary`,请求的二进制数据会当做二进制数组保存起来



```json
{
    "name" : "binaryVariable",
    "scope" : "local",
    "type" : "binary",
    "value" : null,
    "valueUrl" : "http://.../runtime/process-instances/123/variables/binaryVariable/data"
}
```

 

# 分支



## 获取一个分支

```http
GET runtime/executions/{executionId}
```



| 参数        | 是否必须 | 值     | 描述         |
| ----------- | -------- | ------ | ------------ |
| executionId | 是       | String | 获取分支的id |

 

```json
{
   "id":"5",
   "url":"http://localhost:8182/runtime/executions/5",
   "parentId":null,
   "parentUrl":null,
   "processInstanceId":"5",
   "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
   "suspended":false,
   "activityId":null
}
```



## 对分支执行操作

```http
PUT runtime/executions/{executionId}
```



| 参数        | 是否必须 | 值     | 描述                 |
| ----------- | -------- | ------ | -------------------- |
| executionId | 是       | String | 希望执行操作的分支id |

**请求体(继续执行分支):** 

```json
{
  "action":"signal"
}
```

**请求体(分支接收了信号事件):** 

```json
{
  "action":"signalEventReceived",
  "signalName":"mySignal"
  "variables": [ ... ]
}
```

* 提醒分支接收了一个信号事件,要使用一个`signalName`参数.还可以传递`variables`参数,它会在执行操作之前设置到分支中

**请求体(分支接收了消息事件):** 

```json
{
  "action":"messageEventReceived",
  "messageName":"myMessage"
  "variables": [ ... ]
}
```

* 提醒分支接收了一个消息事件,要使用一个`messageName`参数.还可以传递`variables`参数,它会在执行操作之前设置到分支中

 

**成功响应体(当操作没有导致分支结束的情况):**

```json
{
   "id":"5",
   "url":"http://localhost:8182/runtime/executions/5",
   "parentId":null,
   "parentUrl":null,
   "processInstanceId":"5",
   "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
   "suspended":false,
   "activityId":null
}
```



## 获得一个分支的所有活动节点

```http
GET runtime/executions/{executionId}/activities
```

* 返回分支以及子分支当前所有活动的节点(递归所有下级)



| 参数        | 是否必须 | 值     | 描述                   |
| ----------- | -------- | ------ | ---------------------- |
| executionId | 是       | String | 获取节点对应的分支id。 |

 

```json
[
  "userTaskForManager",
  "receiveTask"
]
```



## 获取分支列表

```http
GET repository/executions
```



| 参数                         | 必须 | 值     | 描述                                                         |
| ---------------------------- | ---- | ------ | ------------------------------------------------------------ |
| id                           | 否   | String | 只返回指定id的分支                                           |
| processDefinitionKey         | 否   | String | 只返回指定流程定义key的分支                                  |
| processDefinitionId          | 否   | String | 只返回指定流程定义id的分支                                   |
| processInstanceId            | 否   | String | 只返回作为指定流程实例id一部分的分支                         |
| messageEventSubscriptionName | 否   | String | 只返回订阅了指定名称消息的分支                               |
| signalEventSubscriptionName  | 否   | String | 只返回订阅了指定名称信号的分支                               |
| parentId                     | 否   | String | 只返回指定分支直接下级的分支                                 |
| sort                         | 否   | String | 排序,应该和`processInstanceId`(默认), `processDefinitionId` 或 `processDefinitionKey`之一一起使用. |
|                              |      |        |                                                              |

```json
{
   "data":[
      {
         "id":"5",
         "url":"http://localhost:8182/runtime/executions/5",
         "parentId":null,
         "parentUrl":null,
         "processInstanceId":"5",
         "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
         "suspended":false,
         "activityId":null
      },
      {
         "id":"7",
         "url":"http://localhost:8182/runtime/executions/7",
         "parentId":"5",
         "parentUrl":"http://localhost:8182/runtime/executions/5",
         "processInstanceId":"5",
         "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
         "suspended":false,
         "activityId":"processTask"
      }
   ],
   "total":2,
   "start":0,
   "sort":"processInstanceId",
   "order":"asc",
   "size":2
}
```



## 查询分支

```http
POST query/executions
```



```json
{
  "processDefinitionKey":"oneTaskProcess",
  "variables":
  [
    {
        "name" : "myVariable",
        "value" : 1234,
        "operation" : "equals",
        "type" : "long"
    },
    ...
  ],
  "processInstanceVariables":
  [
    {
        "name" : "processVariable",
        "value" : "some string",
        "operation" : "equals",
        "type" : "string"
    },
    ...
  ],
  ...
}
```

* 请求体可以包含在[获取分支列表](http://www.mossle.com/docs/activiti/#restExecutionsGet)中可以使用的查询条件.除此之外,也可以在查询中提供`variables`和`processInstanceVariables`列表,关于变量的格式可以参考[此处](http://www.mossle.com/docs/activiti/#restQueryVariable). 

 

```json
{
   "data":[
      {
         "id":"5",
         "url":"http://localhost:8182/runtime/executions/5",
         "parentId":null,
         "parentUrl":null,
         "processInstanceId":"5",
         "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
         "suspended":false,
         "activityId":null
      },
      {
         "id":"7",
         "url":"http://localhost:8182/runtime/executions/7",
         "parentId":"5",
         "parentUrl":"http://localhost:8182/runtime/executions/5",
         "processInstanceId":"5",
         "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
         "suspended":false,
         "activityId":"processTask"
      }
   ],
   "total":2,
   "start":0,
   "sort":"processInstanceId",
   "order":"asc",
   "size":2
}
```



## 获取分支的变量列表

```
GET runtime/executions/{executionId}/variables?scope={scope}
```



| 参数        | 是否必须 | 值     | 描述                                                         |
| ----------- | -------- | ------ | ------------------------------------------------------------ |
| executionId | 是       | String | 变量对应的分支id                                             |
| scope       | 否       | String | `local`或`global`.若忽略,会返回local和global作用域下的所有变量 |



```
[
   {
      "name":"intProcVar",
      "type":"integer",
      "value":123,
      "scope":"global"
   },
   {
      "name":"byteArrayProcVar",
      "type":"binary",
      "value":null,
      "valueUrl":"http://localhost:8182/runtime/process-instances/5/variables/byteArrayProcVar/data",
      "scope":"local"
   },
 
   ...
]
```

* 当变量为二进制或序列化类型时,`valueUrl`给出了获得原始数据的URL.如果是普通变量,变量值就会直接包含在响应中



## 获得分支的一个变量

```
GET runtime/executions/{executionId}/variables/{variableName}?scope={scope}
```



| 参数         | 必须 | 值     | 描述                                                         |
| ------------ | ---- | ------ | ------------------------------------------------------------ |
| executionId  | 是   | String | 变量对应的分支id                                             |
| variableName | 是   | String | 获取的变量名称                                               |
| scope        | 否   | String | `local` 或 `global`.若忽略,返回local变量(如果存在).如果不存在局部变量,返回global变量(如果存在) |



```
   {
      "name":"intProcVar",
      "type":"integer",
      "value":123,
      "scope":"local"
   }
```

* 当变量为二进制或序列化类型时,`valueUrl`给出了获得原始数据的URL.如果是普通变量,变量值就会直接包含在响应中



## 新建或更新分支变量

```
POST runtime/executions/{executionId}/variables
PUT runtime/executions/{executionId}/variables
```

* 使用`POST`时,会创建所有传递的变量.如果流程实例中已经存在了其中一个变量,就会返回一个错误(409 - CONFLICT).使用`PUT`时, 流程实例中不存在的变量会被创建,已存在的变量会被更新,不会有任何错误



| 参数        | 是否必须 | 值     | 描述             |
| ----------- | -------- | ------ | ---------------- |
| executionId | 是       | String | 变量对应的分支id |

```
[
   {
      "name":"intProcVar"
      "type":"integer"
      "value":123,
      "scope":"local"
   },
 
   ...
]
```

* **注意你只能提供作用域相同的变量.如果请求体数组中包含了不同作用域的变量,请求会返回一个错误(****400 - BAD REQUEST****).**请求体数据中可以传递任意个数的变量.关于变量格式的详细信息可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables).注意,如果忽略了作用域,只有`local`作用域的比那两可以设置到流程实例中.



```
[
   {
      "name":"intProcVar",
      "type":"integer",
      "value":123,
      "scope":"local"
   },
 
   ...
 
]
```



## 更新分支变量

```
PUT runtime/executions/{executionId}/variables/{variableName}
```



| 参数         | 是否必须 | 值     | 描述                         |
| ------------ | -------- | ------ | ---------------------------- |
| executionId  | 是       | String | 希望更新的变量对应的分支id。 |
| variableName | 是       | String | 希望更新的变量名称。         |

```
 {
    "name":"intProcVar"
    "type":"integer"
    "value":123,
    "scope":"global"
 }
```

* 关于变量格式的详细信息可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables). 

 

```
   {
      "name":"intProcVar",
      "type":"integer",
      "value":123,
      "scope":"global"
   }
```

* 当变量为二进制或序列化类型时,`valueUrl`给出了获得原始数据的URL.如果是普通变量,变量值就会直接包含在响应中



## 创建一个二进制变量

```
POST runtime/executions/{executionId}/variables
```



| 参数        | 是否必须 | 值     | 描述                         |
| ----------- | -------- | ------ | ---------------------------- |
| executionId | 是       | String | 希望创建的新变量对应的分支id |

 

**请求体:** 请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域

- `name`:必须的变量名称
- `type`:创建的变量类型.如果忽略,会假设使用`binary`,请求的二进制数据会当做二进制数组保存起来



```
{
  "name" : "binaryVariable",
  "scope" : "local",
  "type" : "binary",
  "value" : null,
  "valueUrl" : "http://.../runtime/executions/123/variables/binaryVariable/data"
}
```

 

## 更新已经已存在的二进制分支变量

```
PUT runtime/executions/{executionId}/variables/{variableName}
```



| 参数         | 是否必须 | 值     | 描述                         |
| ------------ | -------- | ------ | ---------------------------- |
| executionId  | 是       | String | 希望更新的变量对应的分支id。 |
| variableName | 是       | String | 希望更新的变量名称。         |

**请求体:** 请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域:

- `name`:必须的变量名称.
- `type`:创建的变量类型.如果忽略,会假设使用`binary`,请求的二进制数据会当做二进制数组保存起来.
- `scope`:创建的变量作用于.如果忽略,假设是`local`.



```
{
  "name" : "binaryVariable",
  "scope" : "local",
  "type" : "binary",
  "value" : null,
  "valueUrl" : "http://.../runtime/executions/123/variables/binaryVariable/data"
}
```

 

# 任务



## 获取任务

```
GET runtime/tasks/{taskId}
```



```
{
  "assignee" : "kermit",
  "createTime" : "2013-04-17T10:17:43.902+0000",
  "delegationState" : "pending",
  "description" : "Task description",
  "dueDate" : "2013-04-17T10:17:43.902+0000",
  "execution" : "http://localhost:8182/runtime/executions/5",
  "id" : "8",
  "name" : "My task",
  "owner" : "owner",
  "parentTask" : "http://localhost:8182/runtime/tasks/9",
  "priority" : 50,
  "processDefinition" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
  "processInstance" : "http://localhost:8182/runtime/process-instances/5",
  "suspended" : false,
  "taskDefinitionKey" : "theTask",
  "url" : "http://localhost:8182/runtime/tasks/8"
}
```

- `delegationState`:任务的代理状态.可以为`null`,`"pending"`     或 `"resolved"`.



## 任务列表

```
GET runtime/tasks
```



```
{
  "data": [
    {
      "assignee" : "kermit",
      "createTime" : "2013-04-17T10:17:43.902+0000",
      "delegationState" : "pending",
      "description" : "Task description",
      "dueDate" : "2013-04-17T10:17:43.902+0000",
      "execution" : "http://localhost:8182/runtime/executions/5",
      "id" : "8",
      "name" : "My task",
      "owner" : "owner",
      "parentTask" : "http://localhost:8182/runtime/tasks/9",
      "priority" : 50,
      "processDefinition" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "processInstance" : "http://localhost:8182/runtime/process-instances/5",
      "suspended" : false,
      "taskDefinitionKey" : "theTask",
      "url" : "http://localhost:8182/runtime/tasks/8"
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 查询任务

```
POST query/tasks
```



```
{
  "name" : "My task",
  "description" : "The task description",
 
  ...
 
  "taskVariables" : [
    {
      "name" : "myVariable",
      "value" : 1234,
      "operation" : "equals",
      "type" : "long"
    }
  ],
 
    "processInstanceVariables" : [
      {
         ...
      }
    ]
  ]
}
```

* 此处所有被支持的JSON参数都和[获得任务集合](http://www.mossle.com/docs/activiti/#restTasksGet)完全一样,只是使用JSON体参数的方式替代URL参数,这样就可以使用更加高级的查询方式,并能预防请求uri过长导致的问题.除此之外,可以基于任务和流程变量进行查询.`taskVariables` 和 `processInstanceVariables` 都可以包含 [此处描述](http://www.mossle.com/docs/activiti/#restQueryVariable)的json数组

 

```
{
  "data": [
    {
      "assignee" : "kermit",
      "createTime" : "2013-04-17T10:17:43.902+0000",
      "delegationState" : "pending",
      "description" : "Task description",
      "dueDate" : "2013-04-17T10:17:43.902+0000",
      "execution" : "http://localhost:8182/runtime/executions/5",
      "id" : "8",
      "name" : "My task",
      "owner" : "owner",
      "parentTask" : "http://localhost:8182/runtime/tasks/9",
      "priority" : 50,
      "processDefinition" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "processInstance" : "http://localhost:8182/runtime/process-instances/5",
      "suspended" : false,
      "taskDefinitionKey" : "theTask",
      "url" : "http://localhost:8182/runtime/tasks/8"
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 更新任务

```
PUT runtime/tasks/{taskId}
```



**请求JSON体:**

```
{
  "assignee" : "assignee",
  "delegationState" : "resolved",
  "description" : "New task description",
  "dueDate" : "2013-04-17T13:06:02.438+02:00",
  "name" : "New task name",
  "owner" : "owner",
  "parentTaskId" : "3",
  "priority" : 20
}
```

* 所有请求参数都是可选的.比如,你可以在请求体的JSON对象中只包含'assignee'属性,只更新任务的负责人,其他字段都不填.当包含的字段值为null时,任务的对应属性会被更新为null.比如:`{"dueDate" : null}`会清空任务的持续时间



**成功响应体:** 参考`runtime/tasks/{taskId}`的响应



## 操作任务

```
POST runtime/tasks/{taskId}
```

**完成任务- JSON体:**

```
{
  "action" : "complete",
  "variables" : ...
}
```

* 完成任务.可以使用`variables`参数传递可选的variable数组.关于变量格式的详细信息可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables).注意,此处忽略变量作用域,变量会设置到上级作用域,除非本地作用域应包含了同名变量.这与`TaskService.completeTask(taskId, variables)` 的行为是相同的.

**认领任务- JSON体:**

```
{
  "action" : "claim",
  "assignee" : "userWhoClaims"
}
```

* 指定assignee认领任务.assignee是必填项

**代理任务- JSON体:**

```
{
  "action" : "delegate",
  "assignee" : "userToDelegateTo"
}
```

* 指定assignee代理任务.assignee是必填项.

**处理任务- JSON体:**

```
{
  "action" : "resolve"
}
```

* 处理任务代理.任务会返回给任务的原负责人(如果存在)

 

**成功响应体:** 参考`runtime/tasks/{taskId}`的响应



## 删除任务

```
DELETE runtime/tasks/{taskId}?cascadeHistory={cascadeHistory}&deleteReason={deleteReason}
```



| 参数           | 是否必须 | 值      | 描述                                                         |
| -------------- | -------- | ------- | ------------------------------------------------------------ |
| taskId         | 是       | String  | 希望删除的任务id                                             |
| cascadeHistory | False    | Boolean | 删除任务时是否删除对应的任务历史(如果存在).如果没有设置这个参数,默认为false |
| deleteReason   | False    | String  | 删除任务的原因.`cascadeHistory`为true时,忽略此参数           |

 

## 获得任务的变量

```
GET runtime/tasks/{taskId}/variables?scope={scope}
```



| 参数   | 必须  | 值     | 描述                                                         |
| ------ | ----- | ------ | ------------------------------------------------------------ |
| taskId | 是    | String | 变量对应的任务id                                             |
| scope  | False | String | 返回的变量作用于.如果为 '`local`',只返回任务本身的变量.如果为 '`global`',只返回任务上级分支的变量.如果不指定这个变量,会返回所有局部和全局的变量 |



```
[
  {
    "name" : "doubleTaskVar",
    "scope" : "local",
    "type" : "double",
    "value" : 99.99
  },
  {
    "name" : "stringProcVar",
    "scope" : "global",
    "type" : "string",
    "value" : "This is a ProcVariable"
  },
 
  ...
 
]
```

* 返回JSON数组型的变量.对响应的详细介绍可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables). 



## 获取任务的一个变量

```
GET runtime/tasks/{taskId}/variables/{variableName}?scope={scope}
```



| 参数         | 必须  | 值     | 描述                                                         |
| ------------ | ----- | ------ | ------------------------------------------------------------ |
| taskId       | 是    | String | 获取变量对应的任务id                                         |
| variableName | 是    | String | 获取变量对应的名称                                           |
| scope        | False | String | 返回的变量作用于.如果为 '`local`',只返回任务本身的变量.如果为 '`global`',只返回任务上级分支的变量.如果不指定这个变量,会返回所有局部和全局的变量 |



```
{
  "name" : "myTaskVariable",
  "scope" : "local",
  "type" : "string",
  "value" : "Hello my friend"
}
```

* 对响应的详细介绍可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables). 



## 获取变量的二进制数据

```
GET runtime/tasks/{taskId}/variables/{variableName}/data?scope={scope}
```



| 参数         | 必须  | 值     | 描述                                                         |
| ------------ | ----- | ------ | ------------------------------------------------------------ |
| taskId       | 是    | String | 获取变量数据对应的任务id                                     |
| variableName | 是    | String | 获取数据对应的变量名称.只能使用 `binary` 和 `serializable` 类型的变量.如果使用了其他类型的变量,会返回 `404` |
| scope        | False | String | 返回的变量作用于.如果为 '`local`',只返回任务本身的变量.如果为 '`global`',只返回任务上级分支的变量.如果不指定这个变量,会返回所有局部和全局的变量 |

 

**成功响应体:** 响应体包含了变量的二进制值.当类型为 `binary`时,无论请求的accept-type头部设置了什么值,响应的content-type都为`application/octet-stream`.当类型为 `serializable`时, content-type为`application/x-java-serialized-object`.



## 创建任务变量

```
POST runtime/tasks/{taskId}/variables
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 创建新变量对应的任务id |

**创建简单(非二进制)变量的请求体:** 

```
[
  {
    "name" : "myTaskVariable",
    "scope" : "local",
    "type" : "string",
    "value" : "Hello my friend"
  },
  {
    ...
  }
]
```

* 请求体应该是包含一个或多个JSON对象的数组,对应应该创建的变量
  * `name`:必须的变量名称
  * `scope`:创建的变量的作用域.如果忽略,假设为`local`
  * `type`:创建的变量的类型.如果忽略,转换为对应的JSON的类型(string,boolean,integer或double)
  * `value`:变量值
* 关于变量格式的详细信息可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables)



```
[
  {
    "name" : "myTaskVariable",
    "scope" : "local",
    "type" : "string",
    "value" : "Hello my friend"
  },
  {
    ...
  }
]
```

 

## 创建二进制任务变量

```
POST runtime/tasks/{taskId}/variables
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 创建新变量对应的任务id |

 

**请求体:** 请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域:

- `name`:必须的变量名称.
- `scope`:创建的变量的作用域.如果忽略,假设使用 `local`.
- `type`:创建的变量类型.如果忽略,会假设使用`binary`,请求的二进制数据会当做二进制数组保存起来.



```
{
  "name" : "binaryVariable",
  "scope" : "local",
  "type" : "binary",
  "value" : null,
  "valueUrl" : "http://.../runtime/tasks/123/variables/binaryVariable/data"
}
```

 

## 更新任务的一个已有变量

```
PUT runtime/tasks/{taskId}/variables/{variableName}
```



| 参数         | 是否必须 | 值     | 描述                       |
| ------------ | -------- | ------ | -------------------------- |
| taskId       | 是       | String | 希望更新的变量对应的任务id |
| variableName | 是       | String | 希望更新的变量名称         |

 

**更新简单(非二进制)变量的请求体:** 

```
{
  "name" : "myTaskVariable",
  "scope" : "local",
  "type" : "string",
  "value" : "Hello my friend"
}
```

- `name`:必须的变量名称.
- `scope`:更新的变量的作用域.如果忽略,假设为`local`.
- `type`:更新的变量的类型.如果忽略,转换为对应的JSON的类型     (string, boolean, integer 或     double).
- `value`:变量值.

关于变量格式的详细信息可以参考[REST变量章节](http://www.mossle.com/docs/activiti/#restVariables). 



```
{
  "name" : "myTaskVariable",
  "scope" : "local",
  "type" : "string",
  "value" : "Hello my friend"
}
```

 

## 更新一个二进制任务变量

```
PUT runtime/tasks/{taskId}/variables/{variableName}
```



| 参数         | 是否必须 | 值     | 描述                         |
| ------------ | -------- | ------ | ---------------------------- |
| taskId       | 是       | String | 希望更新的变量对应的任务id。 |
| variableName | 是       | String | 希望更新的变量名称。         |

 

**请求体:** 请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域:

- `name`:必须的变量名称.
- `scope`:创建的变量的作用域.如果忽略,假设使用 `local`.
- `type`:创建的变量类型.如果忽略,会假设使用`binary`,请求的二进制数据会当做二进制数组保存起来.



```
{
  "name" : "binaryVariable",
  "scope" : "local",
  "type" : "binary",
  "value" : null,
  "valueUrl" : "http://.../runtime/tasks/123/variables/binaryVariable/data"
}
```

 

## 删除任务变量

```
DELETE runtime/tasks/{taskId}/variables/{variableName}?scope={scope}
```



| 参数         | 是否必须 | 值     | 描述                                                         |
| ------------ | -------- | ------ | ------------------------------------------------------------ |
| taskId       | 是       | String | 希望删除的变量对应的任务id                                   |
| variableName | 是       | String | 希望删除的变量名称                                           |
| scope        | 否       | String | 希望删除的变量的作用域.可以是`local` 或 `global`.如果忽略,假设为`local` |

 

## 删除任务的所有局部变量

```
DELETE runtime/tasks/{taskId}/variables
```



| 参数   | 是否必须 | 值     | 描述                       |
| ------ | -------- | ------ | -------------------------- |
| taskId | 是       | String | 希望删除的变量对应的任务id |

 

## 获得任务的所有IdentityLink

```
GET runtime/tasks/{taskId}/identitylinks
```



| 参数   | 是否必须 | 值     | 描述                               |
| ------ | -------- | ------ | ---------------------------------- |
| taskId | 是       | String | 希望获得IdentityLink对应的任务id。 |



```
[
  {
    "userId" : "kermit",
    "groupId" : null,
    "type" : "candidate",
    "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/identitylinks/users/kermit/candidate"
  },
  {
    "userId" : null,
    "groupId" : "sales",
    "type" : "candidate",
    "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/identitylinks/groups/sales/candidate"
  },
 
  ...
]
```



## 获得一个任务的所有组或用户的IdentityLink

```
GET runtime/tasks/{taskId}/identitylinks/users
GET runtime/tasks/{taskId}/identitylinks/groups
```

* 返回对应于用户或组的IdentityLink.响应体与状态码与获得一个任务的所有IdentityLink完全一样



## 获得一个任务的一个IdentityLink

```
GET runtime/tasks/{taskId}/identitylinks/{family}/{identityId}/{type}
```



| 参数       | 是否必填 | 数据   | 描述                                                |
| ---------- | -------- | ------ | --------------------------------------------------- |
| taskId     | 是       | String | 任务的id。                                          |
| family     | 是       | String | `groups` 或 `users`，对应期望获得哪种IdentityLink。 |
| identityId | 是       | String | IdentityLink的id。                                  |
| type       | 是       | String | IdentityLink的类型。                                |



```
{
  "userId" : null,
  "groupId" : "sales",
  "type" : "candidate",
  "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/identitylinks/groups/sales/candidate"
}
```



## 为任务创建一个IdentityLink

```
POST runtime/tasks/{taskId}/identitylinks
```



| 参数   | 是否必填 | 数据   | 描述       |
| ------ | -------- | ------ | ---------- |
| taskId | 是       | String | 任务的id。 |

 

**请求体(用户):** 

```
{
  "userId" : "kermit",
  "type" : "candidate",
}
```

**请求体(组):** 

```
{
  "groupId" : "sales",
  "type" : "candidate",
}
```



```
{
  "userId" : null,
  "groupId" : "sales",
  "type" : "candidate",
  "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/identitylinks/groups/sales/candidate"
}
```



## 删除任务的一个IdentityLink

```
DELETE runtime/tasks/{taskId}/identitylinks/{family}/{identityId}/{type}
```



| 参数       | 是否必填 | 数据   | 描述                                          |
| ---------- | -------- | ------ | --------------------------------------------- |
| taskId     | 是       | String | 任务的id。                                    |
| family     | 是       | String | `groups` 或 `users`，对应IdentityLink的种类。 |
| identityId | 是       | String | IdentityLink的id。                            |
| type       | 是       | String | IdentityLink的类型。                          |

 

## 为任务创建评论

```
POST runtime/tasks/{taskId}/comments
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 创建评论对应的任务id。 |

 

**请求体:** 

```
{
  "message" : "This is a comment on the task."
}
```

**成功响应体:** 

```
{
  "id" : "123",
  "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/comments/123",
  "message" : "This is a comment on the task.",
  "author" : "kermit"
}
```



## 获得任务的所有评论

```
GET runtime/tasks/{taskId}/comments
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 获取评论对应的任务id。 |

 

**成功响应体:** 

```
[
  {
    "id" : "123",
    "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/comments/123",
    "message" : "This is a comment on the task.",
    "author" : "kermit"
  },
  {
    "id" : "456",
    "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/comments/456",
    "message" : "This is another comment on the task.",
    "author" : "gonzo"
  }
]
```

 

## 获得任务的一个评论

```
GET runtime/tasks/{taskId}/comments/{commentId}
```



| 参数      | 是否必须 | 值     | 描述                   |
| --------- | -------- | ------ | ---------------------- |
| taskId    | 是       | String | 获取评论对应的任务id。 |
| commentId | 是       | String | 评论的id。             |

 

**成功响应体:** 

```
{
  "id" : "123",
  "url" : "http://localhost:8081/activiti-rest/service/runtime/tasks/100/comments/123",
  "message" : "This is a comment on the task.",
  "author" : "kermit"
}
```

 

## 删除任务的一条评论

```
DELETE runtime/tasks/{taskId}/comments/{commentId}
```



| 参数      | 是否必须 | 值     | 描述                   |
| --------- | -------- | ------ | ---------------------- |
| taskId    | 是       | String | 删除评论对应的任务id。 |
| commentId | 是       | String | 评论的id。             |

 

## 获得任务的所有事件

```
GET runtime/tasks/{taskId}/events
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 获得事件对应的任务id。 |

 

**成功响应体:** 

```
[
  {
    "action" : "AddUserLink",
    "id" : "4",
    "message" : [ "gonzo", "contributor" ],
    "taskUrl" : "http://localhost:8182/runtime/tasks/2",
    "time" : "2013-05-17T11:50:50.000+0000",
    "url" : "http://localhost:8182/runtime/tasks/2/events/4",
    "userId" : null
  },
 
  ...
 
]
```

 

## 获得任务的一个事件

```
GET runtime/tasks/{taskId}/events/{eventId}
```



| 参数    | 是否必须 | 值     | 描述                   |
| ------- | -------- | ------ | ---------------------- |
| taskId  | 是       | String | 获得事件对应的任务id。 |
| eventId | 是       | String | 事件的id。             |

 

**成功响应体:** 

```
{
  "action" : "AddUserLink",
  "id" : "4",
  "message" : [ "gonzo", "contributor" ],
  "taskUrl" : "http://localhost:8182/runtime/tasks/2",
  "time" : "2013-05-17T11:50:50.000+0000",
  "url" : "http://localhost:8182/runtime/tasks/2/events/4",
  "userId" : null
}
```



 

## 为任务创建一个附件,包含外部资源的链接

```
POST runtime/tasks/{taskId}/attachments
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 创建附件对应的任务id。 |

 

**请求体:** 

```
{
  "name":"Simple attachment",
  "description":"Simple attachment description",
  "type":"simpleType",
  "externalUrl":"http://activiti.org"
}
```

* 创建附件只有name是必填的

**成功响应体:** 

```
{
  "id":"3",
  "url":"http://localhost:8182/runtime/tasks/2/attachments/3",
  "name":"Simple attachment",
  "description":"Simple attachment description",
  "type":"simpleType",
  "taskUrl":"http://localhost:8182/runtime/tasks/2",
  "processInstanceUrl":null,
  "externalUrl":"http://activiti.org",
  "contentUrl":null
}
```

 

## 为任务创建一个附件,包含附件文件

```
POST runtime/tasks/{taskId}/attachments
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 创建附件对应的任务id。 |

 

**请求体:** 请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域:

- `name`:必须的变量名称.
- `description`:附件的描述,可选.
- `type`:创建的变量类型.如果忽略,会假设使用`binary`,请求的二进制数据会当做二进制数组保存起来.

**成功响应体:** 

```
{
      "id":"5",
      "url":"http://localhost:8182/runtime/tasks/2/attachments/5",
      "name":"Binary attachment",
      "description":"Binary attachment description",
      "type":"binaryType",
      "taskUrl":"http://localhost:8182/runtime/tasks/2",
      "processInstanceUrl":null,
      "externalUrl":null,
      "contentUrl":"http://localhost:8182/runtime/tasks/2/attachments/5/content"
   }
```

 

## 获得任务的所有附件

```
GET runtime/tasks/{taskId}/attachments
```



| 参数   | 是否必须 | 值     | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| taskId | 是       | String | 获取附件对应的任务id。 |

 

**成功响应体:** 

```
[
  {
    "id":"3",
    "url":"http://localhost:8182/runtime/tasks/2/attachments/3",
    "name":"Simple attachment",
    "description":"Simple attachment description",
    "type":"simpleType",
    "taskUrl":"http://localhost:8182/runtime/tasks/2",
    "processInstanceUrl":null,
    "externalUrl":"http://activiti.org",
    "contentUrl":null
  },
  {
    "id":"5",
    "url":"http://localhost:8182/runtime/tasks/2/attachments/5",
    "name":"Binary attachment",
    "description":"Binary attachment description",
    "type":"binaryType",
    "taskUrl":"http://localhost:8182/runtime/tasks/2",
    "processInstanceUrl":null,
    "externalUrl":null,
    "contentUrl":"http://localhost:8182/runtime/tasks/2/attachments/5/content"
  }
]
```

 

## 获得任务的一个附件

```
GET runtime/tasks/{taskId}/attachments/{attachmentId}
```



| 参数         | 是否必须 | 值     | 描述                   |
| ------------ | -------- | ------ | ---------------------- |
| taskId       | 是       | String | 获取附件对应的任务id。 |
| attachmentId | 是       | String | 附件的id。             |

 

**成功响应体:** 

```
  {
    "id":"5",
    "url":"http://localhost:8182/runtime/tasks/2/attachments/5",
    "name":"Binary attachment",
    "description":"Binary attachment description",
    "type":"binaryType",
    "taskUrl":"http://localhost:8182/runtime/tasks/2",
    "processInstanceUrl":null,
    "externalUrl":null,
    "contentUrl":"http://localhost:8182/runtime/tasks/2/attachments/5/content"
  }
```

- `externalUrl - contentUrl``:`如果附件是一个外部资源链接,`externalUrl`包含外部内容的URL.如果附件内容保存在Activiti引擎中,`contentUrl`会包含获取二进制流内容的URL.     
- `type``:`可以是任何有效值.包含一个格式合法的media-type时(比如application/xml,     text/plain),二进制HTTP响应的content-type会被设置为对应值.     



 

## 获取附件的内容

```
GET runtime/tasks/{taskId}/attachment/{attachmentId}/content
```



| 参数         | 是否必须 | 值     | 描述                                                         |
| ------------ | -------- | ------ | ------------------------------------------------------------ |
| taskId       | 是       | String | 获取附件数据对应的任务id。                                   |
| attachmentId | 是       | String | 附件的id，当附件指向外部URL，而不是Activiti中的内容，就会返回`404`。 |

  

**成功响应体:** 响应体包含了二进制内容.默认,响应的content-type设置为`application/octet-stream`,除非附件类型包含了合法的Content-Type.



## 删除任务的一个附件

```
DELETE runtime/tasks/{taskId}/attachments/{attachmentId}
```



| 参数         | 是否必须 | 值     | 描述                       |
| ------------ | -------- | ------ | -------------------------- |
| taskId       | 是       | String | 希望删除附件对应的任务id。 |
| attachmentId | 是       | String | 附件的id。                 |



# 历史



## 获得历史流程实例

```
GET history/historic-process-instances/{processInstanceId}
```

 

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "5",
      "businessKey" : "myKey",
      "processDefinitionId" : "oneTaskProcess%3A1%3A4",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "startTime" : "2013-04-17T10:17:43.902+0000",
      "endTime" : "2013-04-18T14:06:32.715+0000",
      "durationInMillis" : 86400056,
      "startUserId" : "kermit",
      "startActivityId" : "startEvent",
      "endActivityId" : "endEvent",
      "deleteReason" : null,
      "superProcessInstanceId" : "3",
      "url" : "http://localhost:8182/history/historic-process-instances/5",
      "variables": null
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 历史流程实例列表

```
GET history/historic-process-instances
```

 

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "5",
      "businessKey" : "myKey",
      "processDefinitionId" : "oneTaskProcess%3A1%3A4",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "startTime" : "2013-04-17T10:17:43.902+0000",
      "endTime" : "2013-04-18T14:06:32.715+0000",
      "durationInMillis" : 86400056,
      "startUserId" : "kermit",
      "startActivityId" : "startEvent",
      "endActivityId" : "endEvent",
      "deleteReason" : null,
      "superProcessInstanceId" : "3",
      "url" : "http://localhost:8182/history/historic-process-instances/5",
      "variables": [
        {
          "name": "test",
          "variableScope": "local",
          "value": "myTest"
        }
      ]
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 查询历史流程实例

```
POST query/historic-process-instances
```

**请求体:** 

```
{
  "processDefinitionId" : "oneTaskProcess%3A1%3A4",
  ...
 
  "variables" : [
    {
      "name" : "myVariable",
      "value" : 1234,
      "operation" : "equals",
      "type" : "long"
    }
  ]
}
```

所有支持的JSON参数字段和[获得历史流程实例集合](http://www.mossle.com/docs/activiti/#restHistoricProcessInstancesGet)完全一样,但是传递的是JSON参数,而不是URL参数,这样可以支持更高级的参数,同时避免请求uri过长.除此之外,查询支持基于流程变量查询. `variables`属性是一个json数组,包含[此处描述](http://www.mossle.com/docs/activiti/#restQueryVariable)的格式. 

 

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "5",
      "businessKey" : "myKey",
      "processDefinitionId" : "oneTaskProcess%3A1%3A4",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "startTime" : "2013-04-17T10:17:43.902+0000",
      "endTime" : "2013-04-18T14:06:32.715+0000",
      "durationInMillis" : 86400056,
      "startUserId" : "kermit",
      "startActivityId" : "startEvent",
      "endActivityId" : "endEvent",
      "deleteReason" : null,
      "superProcessInstanceId" : "3",
      "url" : "http://localhost:8182/history/historic-process-instances/5",
      "variables": [
        {
          "name": "test",
          "variableScope": "local",
          "value": "myTest"
        }
      ]
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 删除历史流程实例

```
DELETE history/historic-process-instances/{processInstanceId}
```

 

## 获取历史流程实例的IdentityLink

```
GET history/historic-process-instance/{processInstanceId}/identitylinks
```

 

**成功响应体:** 

```
[
 {
  "type" : "participant",
  "userId" : "kermit",
  "groupId" : null,
  "taskId" : null,
  "taskUrl" : null,
  "processInstanceId" : "5",
  "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/5"
 }
]
```



## 获取历史流程实例变量的二进制数据

```
GET history/historic-process-instances/{processInstanceId}/variables/{variableName}/data
```

 

**成功响应体:** 响应体包含了变量的二进制值.当类型为 `binary`时,无论请求的accept-type头部设置了什么值,响应的content-type都为`application/octet-stream`.当类型为 `serializable`时, content-type为`application/x-java-serialized-object`.



## 获得单独历史任务实例

```
GET history/historic-task-instances/{taskId}
```

 

**成功响应体:** 

```
{
  "id" : "5",
  "processDefinitionId" : "oneTaskProcess%3A1%3A4",
  "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
  "processInstanceId" : "3",
  "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/3",
  "executionId" : "4",
  "name" : "My task name",
  "description" : "My task description",
  "deleteReason" : null,
  "owner" : "kermit",
  "assignee" : "fozzie",
  "startTime" : "2013-04-17T10:17:43.902+0000",
  "endTime" : "2013-04-18T14:06:32.715+0000",
  "durationInMillis" : 86400056,
  "workTimeInMillis" : 234890,
  "claimTime" : "2013-04-18T11:01:54.715+0000",
  "taskDefinitionKey" : "taskKey",
  "formKey" : null,
  "priority" : 50,
  "dueDate" : "2013-04-20T12:11:13.134+0000",
  "parentTaskId" : null,
  "url" : "http://localhost:8182/history/historic-task-instances/5",
  "variables": null
}
```



## 获取历史任务实例

```
GET history/historic-task-instances
```



**成功响应体:** 

```
{
  "data": [
    {
      "id" : "5",
      "processDefinitionId" : "oneTaskProcess%3A1%3A4",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "processInstanceId" : "3",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/3",
      "executionId" : "4",
      "name" : "My task name",
      "description" : "My task description",
      "deleteReason" : null,
      "owner" : "kermit",
      "assignee" : "fozzie",
      "startTime" : "2013-04-17T10:17:43.902+0000",
      "endTime" : "2013-04-18T14:06:32.715+0000",
      "durationInMillis" : 86400056,
      "workTimeInMillis" : 234890,
      "claimTime" : "2013-04-18T11:01:54.715+0000",
      "taskDefinitionKey" : "taskKey",
      "formKey" : null,
      "priority" : 50,
      "dueDate" : "2013-04-20T12:11:13.134+0000",
      "parentTaskId" : null,
      "url" : "http://localhost:8182/history/historic-task-instances/5",
      "taskVariables": [
        {
          "name": "test",
          "variableScope": "local",
          "value": "myTest"
        }
      ],
      "processVariables": [
        {
          "name": "processTest",
          "variableScope": "global",
          "value": "myProcessTest"
        }
      ]
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 查询历史任务实例

```
POST query/historic-task-instances
```

**查询历史任务实例** **-** **请求体:**

```
{
  "processDefinitionId" : "oneTaskProcess%3A1%3A4",
  ...
 
  "variables" : [
    {
      "name" : "myVariable",
      "value" : 1234,
      "operation" : "equals",
      "type" : "long"
    }
  ]
}
```

所有支持的JSON参数字段和[获得历史任务实例集合](http://www.mossle.com/docs/activiti/#restHistoricTaskInstancesGet)完全一样,但是传递的是JSON参数,而不是URL参数,这样可以支持更高级的参数,同时避免请求uri过长.除此之外,查询支持基于流程变量查询. `taskVariables`和`processVariables`属性是一个json数组,包含[此处描述](http://www.mossle.com/docs/activiti/#restQueryVariable)的格式. 

 

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "5",
      "processDefinitionId" : "oneTaskProcess%3A1%3A4",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "processInstanceId" : "3",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/3",
      "executionId" : "4",
      "name" : "My task name",
      "description" : "My task description",
      "deleteReason" : null,
      "owner" : "kermit",
      "assignee" : "fozzie",
      "startTime" : "2013-04-17T10:17:43.902+0000",
      "endTime" : "2013-04-18T14:06:32.715+0000",
      "durationInMillis" : 86400056,
      "workTimeInMillis" : 234890,
      "claimTime" : "2013-04-18T11:01:54.715+0000",
      "taskDefinitionKey" : "taskKey",
      "formKey" : null,
      "priority" : 50,
      "dueDate" : "2013-04-20T12:11:13.134+0000",
      "parentTaskId" : null,
      "url" : "http://localhost:8182/history/historic-task-instances/5",
      "taskVariables": [
        {
          "name": "test",
          "variableScope": "local",
          "value": "myTest"
        }
      ],
      "processVariables": [
        {
          "name": "processTest",
          "variableScope": "global",
          "value": "myProcessTest"
        }
      ]
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 删除历史任务实例

```
DELETE history/historic-task-instances/{taskId}
```

 

## 获得历史任务实例的IdentityLink

```
GET history/historic-task-instance/{taskId}/identitylinks
```

 

**成功响应体:** 

```
[
 {
  "type" : "assignee",
  "userId" : "kermit",
  "groupId" : null,
  "taskId" : "6",
  "taskUrl" : "http://localhost:8182/history/historic-task-instances/5",
  "processInstanceId" : null,
  "processInstanceUrl" : null
 }
]
```



## 获取历史任务实例变量的二进制值

```
GET history/historic-task-instances/{taskId}/variables/{variableName}/data
```

 

**成功响应体:** 响应体包含了变量的二进制值.当类型为 `binary`时,无论请求的accept-type头部设置了什么值,响应的content-type都为`application/octet-stream`.当类型为 `serializable`时, content-type为`application/x-java-serialized-object`.



## 获取历史活动实例

```
GET history/historic-activity-instances
```

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "5",
      "activityId" : "4",
      "activityName" : "My user task",
      "activityType" : "userTask",
      "processDefinitionId" : "oneTaskProcess%3A1%3A4",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "processInstanceId" : "3",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/3",
      "executionId" : "4",
      "taskId" : "4",
      "calledProcessInstanceId" : null,
      "assignee" : "fozzie",
      "startTime" : "2013-04-17T10:17:43.902+0000",
      "endTime" : "2013-04-18T14:06:32.715+0000",
      "durationInMillis" : 86400056
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 查询历史活动实例

```
POST query/historic-activity-instances
```

**请求体:** 

```
{
  "processDefinitionId" : "oneTaskProcess%3A1%3A4"
}
```

所有支持的JSON参数字段和[获得历史任务实例集合](http://www.mossle.com/docs/activiti/#restHistoricTaskInstancesGet)完全一样,但是传递的是JSON参数,而不是URL参数,这样可以支持更高级的参数,同时避免请求uri过长.

 

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "5",
      "activityId" : "4",
      "activityName" : "My user task",
      "activityType" : "userTask",
      "processDefinitionId" : "oneTaskProcess%3A1%3A4",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definitions/oneTaskProcess%3A1%3A4",
      "processInstanceId" : "3",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/3",
      "executionId" : "4",
      "taskId" : "4",
      "calledProcessInstanceId" : null,
      "assignee" : "fozzie",
      "startTime" : "2013-04-17T10:17:43.902+0000",
      "endTime" : "2013-04-18T14:06:32.715+0000",
      "durationInMillis" : 86400056
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 列出历史变量实例

```
GET history/historic-variable-instances
```

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "14",
      "processInstanceId" : "5",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/5",
      "taskId" : "6",
      "variable" : {
        "name" : "myVariable",
        "variableScope", "global",
        "value" : "test"
      }
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 查询历史变量实例

```
POST query/historic-variable-instances
```

**请求体:** 

```
{
  "processDefinitionId" : "oneTaskProcess%3A1%3A4",
  ...
 
  "variables" : [
    {
      "name" : "myVariable",
      "value" : 1234,
      "operation" : "equals",
      "type" : "long"
    }
  ]
}
```

所有支持的JSON参数字段和[获得历史变量实例集合](http://www.mossle.com/docs/activiti/#restHistoricVariableInstancesGet)完全一样,但是传递的是JSON参数,而不是URL参数,这样可以支持更高级的参数,同时避免请求uri过长.除此之外,查询支持基于流程变量查询. `variables`属性是一个json数组,包含[此处描述](http://www.mossle.com/docs/activiti/#restQueryVariable)的格式. 

 

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "14",
      "processInstanceId" : "5",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/5",
      "taskId" : "6",
      "variable" : {
        "name" : "myVariable",
        "variableScope", "global",
        "value" : "test"
      }
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 获取历史任务实例变量的二进制值

```
GET history/historic-variable-instances/{varInstanceId}/data
```

 

**成功响应体:** 响应体包含了变量的二进制值.当类型为 `binary`时,无论请求的accept-type头部设置了什么值,响应的content-type都为`application/octet-stream`.当类型为 `serializable`时, content-type为`application/x-java-serialized-object`.



## 获取历史细节

```
GET history/historic-detail
```

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "26",
      "processInstanceId" : "5",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/5",
      "executionId" : "6",
      "activityInstanceId", "10",
      "taskId" : "6",
      "taskUrl" : "http://localhost:8182/history/historic-task-instances/6",
      "time" : "2013-04-17T10:17:43.902+0000",
      "detailType" : "variableUpdate",
      "revision" : 2,
      "variable" : {
        "name" : "myVariable",
        "variableScope", "global",
        "value" : "test"
      },
      "propertyId", null,
      "propertyValue", null
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 查询历史细节

```
POST query/historic-detail
```

**请求体:** 

```
{
  "processInstanceId" : "5",
}
```

所有支持的JSON参数字段和[获得历史变量实例集合](http://www.mossle.com/docs/activiti/#restHistoricVariableInstancesGet)完全一样,但是传递的是JSON参数,而不是URL参数,这样可以支持更高级的参数,同时避免请求uri过长.

 

**成功响应体:** 

```
{
  "data": [
    {
      "id" : "26",
      "processInstanceId" : "5",
      "processInstanceUrl" : "http://localhost:8182/history/historic-process-instances/5",
      "executionId" : "6",
      "activityInstanceId", "10",
      "taskId" : "6",
      "taskUrl" : "http://localhost:8182/history/historic-task-instances/6",
      "time" : "2013-04-17T10:17:43.902+0000",
      "detailType" : "variableUpdate",
      "revision" : 2,
      "variable" : {
        "name" : "myVariable",
        "variableScope", "global",
        "value" : "test"
      },
      "propertyId", null,
      "propertyValue", null
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 获取历史细节变量的二进制数据

```
GET history/historic-detail/{detailId}/data
```

 

**成功响应体:** 响应体包含了变量的二进制值.当类型为 `binary`时,无论请求的accept-type头部设置了什么值,响应的content-type都为`application/octet-stream`.当类型为 `serializable`时, content-type为`application/x-java-serialized-object`.



# 表单



* 7以上版本中已经去掉了表单功能



## 获取表单数据

```
GET form/form-data
```



| 参数                | 是否必填                          | 数据   | 描述                                         |
| ------------------- | --------------------------------- | ------ | -------------------------------------------- |
| taskId              | 是（如果没有processDefinitionId） | String | 获取表单数据需要对应的任务id。               |
| processDefinitionId | 是（如果没有taskId）              | String | 获取startEvent表单数据需要对应的流程定义id。 |

  

**成功响应体:** 

```
{
  "data": [
    {
      "formKey" : null,
      "deploymentId" : "2",
      "processDefinitionId" : "3",
      "processDefinitionUrl" : "http://localhost:8182/repository/process-definition/3",
      "taskId" : "6",
      "taskUrl" : "http://localhost:8182/runtime/task/6",
      "formProperties" : [
        {
          "id" : "room",
          "name" : "Room",
          "type" : "string",
          "value" : null,
          "readable" : true,
          "writable" : true,
          "required" : true,
          "datePattern" : null,
          "enumValues" : [
            {
              "id" : "normal",
              "name" : "Normal bed"
            },
            {
              "id" : "kingsize",
              "name" : "Kingsize bed"
            },
          ]
        }
      ]
    }
  ],
  "total": 1,
  "start": 0,
  "sort": "name",
  "order": "asc",
  "size": 1
}
```



## 提交任务表单数据

```
POST form/form-data
```

**任务表单的请求体:** 

```
{
  "taskId" : "5",
  "properties" : [
    {
      "id" : "room",
      "value" : "normal"
    }
  ]
}
```

**startEvent****表单的请求体:**

```
{
  "processDefinitionId" : "5",
  "businessKey" : "myKey", (optional)
  "properties" : [
    {
      "id" : "room",
      "value" : "normal"
    }
  ]
}
```

 

**startEvent****表单数据的成功响应体(任务表单数据没有响应):**

```
{
  "id" : "5",
  "url" : "http://localhost:8182/history/historic-process-instances/5",
  "businessKey" : "myKey",
  "suspended", false,
  "processDefinitionId" : "3",
  "processDefinitionUrl" : "http://localhost:8182/repository/process-definition/3",
  "activityId" : "myTask"
}
```



# 数据库表



## 表列表

```
GET management/tables
```

 

**成功响应体:** 

```
[
   {
      "name":"ACT_RU_VARIABLE",
      "url":"http://localhost:8182/management/tables/ACT_RU_VARIABLE",
      "count":4528
   },
   {
      "name":"ACT_RU_EVENT_SUBSCR",
      "url":"http://localhost:8182/management/tables/ACT_RU_EVENT_SUBSCR",
      "count":3
   },
 
   ...
 
]
```



## 获得一张表

```
GET management/tables/{tableName}
```



| 参数      | 是否必须 | 值     | 描述           |
| --------- | -------- | ------ | -------------- |
| tableName | 是       | String | 获取表的名称。 |

 

**成功响应体:** 

```
{
      "name":"ACT_RE_PROCDEF",
      "url":"http://localhost:8182/management/tables/ACT_RE_PROCDEF",
      "count":60
}
```

 

## 获得表的列信息

```
GET management/tables/{tableName}/columns
```



| 参数      | 是否必须 | 值     | 描述           |
| --------- | -------- | ------ | -------------- |
| tableName | 是       | String | 获取表的名称。 |

 

**成功响应体:** 

```
{
   "tableName":"ACT_RU_VARIABLE",
   "columnNames":[
      "ID_",
      "REV_",
      "TYPE_",
      "NAME_",
      ...
   ],
   "columnTypes":[
      "VARCHAR",
      "INTEGER",
      "VARCHAR",
      "VARCHAR",
      ...
   ]
}
```

 

## 获得表的行数据

```
GET management/tables/{tableName}/data
```



| 参数      | 是否必须 | 值     | 描述           |
| --------- | -------- | ------ | -------------- |
| tableName | 是       | String | 获取表的名称。 |

| 参数                  | 是否必须 | 值      | 描述                                |
| --------------------- | -------- | ------- | ----------------------------------- |
| start                 | 否       | Integer | 从哪一行开始获取。默认为0。         |
| size                  | 否       | Integer | 获取行数，从`start`开始。默认为10。 |
| orderAscendingColumn  | 否       | String  | 对结果行进行排序的字段，正序。      |
| orderDescendingColumn | 否       | String  | 对结果行进行排序的字段，倒序。      |

 

**成功响应体:** 

```
{
  "total":3,
   "start":0,
   "sort":null,
   "order":null,
   "size":3,
 
   "data":[
      {
         "TASK_ID_":"2",
         "NAME_":"var1",
         "REV_":1,
         "TEXT_":"123",
         "LONG_":123,
         "ID_":"3",
         "TYPE_":"integer"
      },
      ...
   ]
 
}
```

 

# 引擎



## 获得引擎属性

```
GET management/properties
```

返回引擎内部使用的只读属性. 

**成功响应体:** 

```
{
   "next.dbid":"101",
   "schema.history":"create(5.14)",
   "schema.version":"5.14"
}
```

 

## 获得引擎信息

```
GET management/engine
```

获得REST服务使用的引擎的只读信息.

**成功响应体:** 

```
{
   "name":"default",
   "version":"5.14",
   "resourceUrl":"file://activiti/activiti.cfg.xml",
   "exception":null
}
```

 

# 作业



## 获取一个作业

```
GET management/jobs/{jobId}
```



| 参数  | 是否必须 | 值     | 描述           |
| ----- | -------- | ------ | -------------- |
| jobId | 是       | String | 获取的作业id。 |

 

**成功响应体:** 

```
{
   "id":"8",
   "url":"http://localhost:8182/management/jobs/8",
   "processInstanceId":"5",
   "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
   "processDefinitionId":"timerProcess:1:4",
   "processDefinitionUrl":"http://localhost:8182/repository/process-definitions/timerProcess%3A1%3A4",
   "executionId":"7",
   "executionUrl":"http://localhost:8182/runtime/executions/7",
   "retries":3,
   "exceptionMessage":null,
   "dueDate":"2013-06-04T22:05:05.474+0000"
}
```

 

## 删除作业

```
DELETE management/jobs/{jobId}
```



| 参数  | 是否必须 | 值     | 描述                |
| ----- | -------- | ------ | ------------------- |
| jobId | 是       | String | 期望删除的作业id。. |

 

## 执行作业

```
POST management/jobs/{jobId}
```

**请求****JSON****体:**

```
{
  "action" : "execute"
}
```



| 参数   | 描述                          | 是否必填 |
| ------ | ----------------------------- | -------- |
| action | 执行的操作。只支持`execute`。 | 是       |

 

## 获得作业的异常堆栈

```
GET management/jobs/{jobId}/exception-stracktrace
```



| 参数  | 描述               | 是否必填 |
| ----- | ------------------ | -------- |
| jobId | 获取堆栈的作业id。 | 是       |

 

## 获得作业列表

```
GET management/jobs
```



| 参数                | 类型    | 描述                                                         |
| ------------------- | ------- | ------------------------------------------------------------ |
| id                  | String  | 返回指定id的作业                                             |
| processInstanceId   | String  | 返回指定id流程一部分的作业                                   |
| executionId         | String  | 返回指定id分支一部分的作业                                   |
| processDefinitionId | String  | 返回指定流程定义id的作业                                     |
| withRetriesLeft     | Boolean | `true`,只返回尝试剩下的.如果为false,会忽略此参数             |
| executable          | Boolean | `true`,只返回可执行的作业.如果为false,会忽略此参数           |
| timersOnly          | Boolean | `true`,只返回类型为定时器的作业.如果为false,会忽略此参数.不能与`'messagesOnly'`一起使用 |
| messagesOnly        | Boolean | `true`返回类型为消息的作业.false忽略此参数.不能`'timersOnly'`一起使用 |
| withException       | Boolean | `true`返回执行时出现了异常的作业.false会忽略此参数           |
| dueBefore           | Date    | 只返回在指定时间前到期的作业.如果使用了这个参数,就不会返回没有设置持续时间的作业 |
| dueAfter            | Date    | 只返回在指定时间后到期的作业.如果使用了这个参数,就不会返回没有设置持续时间的作业 |
| exceptionMessage    | String  | Only return jobs  with the given exception message           |
| sort                | String  | 对结果进行排序的字段,可以是 `id`, `dueDate`, `executionId`, `processInstanceId` 或 `retries`其中之一 |
|                     |         |                                                              |

**成功响应体:** 

```
{
   "data":[
      {
         "id":"13",
         "url":"http://localhost:8182/management/jobs/13",
         "processInstanceId":"5",
         "processInstanceUrl":"http://localhost:8182/runtime/process-instances/5",
         "processDefinitionId":"timerProcess:1:4",
         "processDefinitionUrl":"http://localhost:8182/repository/process-definitions/timerProcess%3A1%3A4",
         "executionId":"12",
         "executionUrl":"http://localhost:8182/runtime/executions/12",
         "retries":0,
         "exceptionMessage":"Can't find scripting engine for 'unexistinglanguage'",
         "dueDate":"2013-06-07T10:00:24.653+0000"
      },
 
      ...
   ],
   "total":2,
   "start":0,
   "sort":"id",
   "order":"asc",
   "size":2
}
```

 

# 用户



* 7以上版本取消了用户



## 获得一个用户

```
GET identity/users/{userId}
```



| 参数   | 是否必须 | 值     | 描述           |
| ------ | -------- | ------ | -------------- |
| userId | 是       | String | 获取用户的id。 |

 

**成功响应体:** 

```
{
   "id":"testuser",
   "firstName":"Fred",
   "lastName":"McDonald",
   "url":"http://localhost:8182/identity/users/testuser",
   "email":"no-reply@activiti.org"
}
```

 

## 获取用户列表

```
GET identity/users
```



| 参数             | 类型   | 描述                                                         |
| ---------------- | ------ | ------------------------------------------------------------ |
| id               | String | 只返回指定id的用户.                                          |
| firstName        | String | 只返回指定firstname的用户.                                   |
| lastName         | String | 只返回指定lastname的用户.                                    |
| email            | String | 只返回指定email的用户.                                       |
| firstNameLike    | String | 只返回firstname与指定值匹配的用户.使用`%`通配符.             |
| lastNameLike     | String | 只返回lastname与指定值匹配的用户.使用`%`通配符.              |
| emailLike        | String | 只返回email与指定值匹配的用户.使用`%`通配符.                 |
| memberOfGroup    | String | 只返回指定组成员的用户.                                      |
| potentialStarter | String | 只返回指定流程定义id的默认启动人.                            |
| sort             | String | 结果排序的字段,应该是`id`, `firstName`, `lastname` 或 `email`其中之一. |
|                  |        |                                                              |

**成功响应体:** 

```
{
   "data":[
      {
         "id":"anotherUser",
         "firstName":"Tijs",
         "lastName":"Barrez",
         "url":"http://localhost:8182/identity/users/anotherUser",
         "email":"no-reply@alfresco.org"
      },
      {
         "id":"kermit",
         "firstName":"Kermit",
         "lastName":"the Frog",
         "url":"http://localhost:8182/identity/users/kermit",
         "email":null
      },
      {
         "id":"testuser",
         "firstName":"Fred",
         "lastName":"McDonald",
         "url":"http://localhost:8182/identity/users/testuser",
         "email":"no-reply@activiti.org"
      }
   ],
   "total":3,
   "start":0,
   "sort":"id",
   "order":"asc",
   "size":3
}
```

 

## 更新用户

```
PUT identity/users/{userId}
```

**请求****JSON****体:**

```
{
  "firstName":"Tijs",
  "lastName":"Barrez",
  "email":"no-reply@alfresco.org",
  "password":"pass123"
}
```

所有请求值都是可选的.比如,你可以在请求体JSON对象中只包含'firstName'属性,只更新用户的firstName,其他值都不受影响.当包含的属性设置为null,用户的属性会被更新为null,比如:`{"firstName" : null}`会清空用户的firstName. 

 

**成功响应体:** 参考 `identity/users/{userId}`的响应.



## 创建用户

```
POST identity/users
```

**请求****JSON****体:**

```
{
  "id":"tijs",
  "firstName":"Tijs",
  "lastName":"Barrez",
  "email":"no-reply@alfresco.org",
  "password":"pass123"
}
```

 

**成功响应体:** 参考 `identity/users/{userId}`的响应.



## 删除用户

```
DELETE identity/users/{userId}
```



| 参数   | 是否必填 | 数据   | 描述               |
| ------ | -------- | ------ | ------------------ |
| userId | 是       | String | 期望删除的用户id。 |

 

## 获取用户图片

```
GET identity/users/{userId}/picture
```



| 参数   | 是否必填 | 数据   | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| userId | 是       | String | 期望获得图片的用户id。 |

 

**响应体:** 响应体包含了演示图片数据,展示用户的图片.响应的Content-Type对应着创建图片时设置的mimeType.

 

## 更新用户图片

```
GET identity/users/{userId}/picture
```



| 参数   | 是否必填 | 数据   | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| userId | 是       | String | 获得图片对应的用户id。 |

 

**请求体:** 请求应该是`multipart/form-data`类型.应该只有一个文件区域,包含源码的二进制内容.除此之外,需要提供以下表单域:

- `mimeType`:上传的图片的mime-type.如果省略,默认会使用 `image/jpeg`     作为图片的mime-type.

 

## 列出用户列表

```
PUT identity/users/{userId}/info
```



| 参数   | 是否必填 | 数据   | 描述               |
| ------ | -------- | ------ | ------------------ |
| userId | 是       | String | 获取信息的用户id。 |

 

**响应体:** 

```
[
   {
      "key":"key1",
      "url":"http://localhost:8182/identity/users/testuser/info/key1"
   },
   {
      "key":"key2",
      "url":"http://localhost:8182/identity/users/testuser/info/key2"
   }
]
```

 

## 获取用户信息

```
GET identity/users/{userId}/info/{key}
```



| 参数   | 是否必填 | 数据   | 描述                      |
| ------ | -------- | ------ | ------------------------- |
| userId | 是       | String | 获取信息的用户id。        |
| key    | 是       | String | 希望获取的用户信息的key。 |

 

**响应体:** 

```
{
   "key":"key1",
   "value":"Value 1",
   "url":"http://localhost:8182/identity/users/testuser/info/key1"
}
```

 

## 更新用户的信息

```
PUT identity/users/{userId}/info/{key}
```



| 参数   | 是否必填 | 数据   | 描述                         |
| ------ | -------- | ------ | ---------------------------- |
| userId | 是       | String | 期望更新的信息对应的用户id。 |
| key    | 是       | String | 期望更新的用户信息的key。    |

 

**请求体:** 

```
{
   "value":"The updated value"
}
```

**响应体:** 

```
{
   "key":"key1",
   "value":"The updated value",
   "url":"http://localhost:8182/identity/users/testuser/info/key1"
}
```

 

## 创建用户信息条目

```
POST identity/users/{userId}/info
```



| 参数   | 是否必填 | 数据   | 描述                   |
| ------ | -------- | ------ | ---------------------- |
| userId | 是       | String | 期望创建信息的用户id。 |

 

**请求体:** 

```
{
   "key":"key1",
   "value":"The value"
}
```

**响应体:** 

```
{
   "key":"key1",
   "value":"The value",
   "url":"http://localhost:8182/identity/users/testuser/info/key1"
}
```

 

## 删除用户的信息

```
DELETE identity/users/{userId}/info/{key}
```



| 参数   | 是否必填 | 数据   | 描述                      |
| ------ | -------- | ------ | ------------------------- |
| userId | 是       | String | 希望删除信息的用户id。    |
| key    | 是       | String | 期望删除的用户信息的key。 |

 

# 群组



* 7以上版本已经取消了组功能



## 获得群组

```
GET identity/groups/{groupId}
```



| 参数    | 是否必须 | 值     | 描述               |
| ------- | -------- | ------ | ------------------ |
| groupId | 是       | String | 希望获得的群组id。 |

 

**成功响应体:** 

```
{
   "id":"testgroup",
   "url":"http://localhost:8182/identity/groups/testgroup",
   "name":"Test group",
   "type":"Test type"
}
```

 

## 获取群组列表

```
GET identity/groups
```



| 参数             | 类型   | 描述                                                  |
| ---------------- | ------ | ----------------------------------------------------- |
| id               | String | 只返回指定id的群组.                                   |
| name             | String | 只返回指定名称的群组.                                 |
| type             | String | 只返回指定类型的群组.                                 |
| nameLike         | String | 只返回名称与指定值匹配的群组使用`%`作为通配符.        |
| member           | String | 只返回成员与指定用户ing相同的群组.                    |
| potentialStarter | String | 只返回成员作为指定id流程定义的潜在启动者的劝阻.       |
| sort             | String | 结果排序的字段.应该是 `id`, `name` 或 `type`其中之一. |
|                  |        |                                                       |

**成功响应体:** 

```
{
   "data":[
     {
        "id":"testgroup",
        "url":"http://localhost:8182/identity/groups/testgroup",
        "name":"Test group",
        "type":"Test type"
     },
 
      ...
   ],
   "total":3,
   "start":0,
   "sort":"id",
   "order":"asc",
   "size":3
}
```

 

## 更新群组

```
PUT identity/groups/{groupId}
```

**请求****JSON****体:**

```
{
   "name":"Test group",
   "type":"Test type"
}
```

所有请求值都是可选的.比如,你可以在请求体JSON对象中只包含'name'属性,只更新群组的名称,其他属性都不会受到英系那个.如果把一个属性设置为null,群组的数据就会更新为null.

 

**成功响应体:** 参考`identity/groups/{groupId}`的响应.



## 创建群组

```
POST identity/groups
```

**请求****JSON****体:**

```
{
   "id":"testgroup",
   "name":"Test group",
   "type":"Test type"
}
```

 

**成功响应体:** 参考 `identity/groups/{groupId}`的响应.



## 删除群组

```
DELETE identity/groups/{groupId}
```



| 参数    | 是否必填 | 数据   | 描述               |
| ------- | -------- | ------ | ------------------ |
| groupId | 是       | String | 期望删除的群组id。 |

  

## 获取群组的成员

`identity/groups/members`不允许使用GET.使用 `identity/users?memberOfGroup=sales` URL来获得某个群组下的所有成员.



## 为群组添加一个成员

```
POST identity/groups/{groupId}/members
```



| 参数    | 是否必填 | 数据   | 描述                   |
| ------- | -------- | ------ | ---------------------- |
| groupId | 是       | String | 期望添加成员的群组id。 |

 

**请求****JSON****体:**

```
{
   "userId":"kermit"
}
```

 

**响应体:** 

```
{
   "userId":"kermit",
   "groupId":"sales",
    "url":"http://localhost:8182/identity/groups/sales/members/kermit"
}
```



## 删除群组的成员

```
DELETE identity/groups/{groupId}/members/{userId}
```



| 参数    | 是否必填 | 数据   | 描述                   |
| ------- | -------- | ------ | ---------------------- |
| groupId | 是       | String | 期望删除成员的群组id。 |
| userId  | 是       | String | 期望删除的用户id。     |

 **响应体：** 

```
{
   "userId":"kermit",
   "groupId":"sales",
    "url":"http://localhost:8182/identity/groups/sales/members/kermit"
}
```

 