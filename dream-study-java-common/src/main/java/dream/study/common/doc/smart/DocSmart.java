package dream.study.common.doc.smart;

/**
 * Smart Doc文档,需要先添加插件smart-doc-maven-plugin.无侵入性,根据相关注解自动生成文档
 * 
 * 配置好json之后,在IDEA中,可以直接利用smart插件生成文档,也可以使用maven命令
 * 
 * <pre>
 * mvn smart-doc:html
 * mvn smart-doc:markdown
 * mvn smart-doc:adoc
 * mvn smart-doc:postman: 生成的JSON文件可以导入到postman中
 * mvn smart-doc:openapi: 生成的文件可利用Swagger UI展示
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-10-31 09:13:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DocSmart {

}