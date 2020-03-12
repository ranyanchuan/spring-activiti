package com.yyan.activiti;


import com.yyan.App;
import com.yyan.pojo.Book;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.util.ResourceUtils;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * https://www.cnblogs.com/telwanggs/p/7491564.html 流程表结构
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {App.class})
public class ProcessDefinitionTest {

    //  初始化流程引擎对象
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    //  使用流程引擎获取到需要的服务对象实例
    private RepositoryService repositoryService = processEngine.getRepositoryService();


    /**
     * 1、发布规则
     * ac_get_bytearray 表: 添加2条数据 (流程规则文件bpmn，流程图片png，还有其他)
     * ac_get_deployment 表: 添加1条数据 (定义新规则的“显示别名“ 和发布时间)
     * ac_re_procdef 表(已部署的流程定义): 添加1条数据 (流程定义)
     */
    @Test
    public void deployProcess() {
        // 创建流程发布配置对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("LeaveFlow.bpmn");
        InputStream image = Thread.currentThread().getContextClassLoader().getResourceAsStream("LeaveFlow.png");

        deploymentBuilder.name("发布流程")  //ac_get_deployment表
                .addInputStream("LeaveFlow.bpmn", inputStream) // ac_get_byearray表
                .addInputStream("LeaveFlow.png", image);// ac_get_byearray表


//         在配置对象中添加发布的规则信息
//        deploymentBuilder
//                .name("2020请假流程") // 当前部署流程的“显示别名”
//                .addClasspathResource("LeaveFlow.bpmn")// 设置规则文件
//                .addClasspathResource("LeaveFlow.png"); // 设置流程图片
        // 调用方法完成流程的发布
        deploymentBuilder.deploy();
    }


    // 使用zip方式发布流程
    @Test
    public void deployByZIP() throws Exception {
        // 创建流程发布配置对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        // 在配置对象中添加发布的规则信息
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("LeaveFlow.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        deploymentBuilder
                .name("2020请假流程V2")
                .addZipInputStream(zipInputStream);
        // 调用deploy方法发布流程
        deploymentBuilder.deploy();
    }


    //	2.查看流程定义
    // createXXXQuery()
    @Test
    public void queryProcessDefinition() throws Exception {
        // 获取流程定义对应的查询对象
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        // 添加查询参数
        String processDefinitionId = "LeaveFlow:1:4";
        query
                // 过滤条件
//			.processDefinitionKey(processDefinitionKey)
//			.processDefinitionName(processDefinitionName )
                .processDefinitionId(processDefinitionId)
                //分页条件
//			.listPage(firstResult, maxResults)
                // 排序条件
                .orderByProcessDefinitionVersion().desc();
        // 执行查询得到结果
        ProcessDefinition pd = query.singleResult();
        // 遍历结果，显示相关信息
        System.out.println("id:" + pd.getId() + ",name:" + pd.getName() + ",key:" + pd.getKey() + ",version:" + pd.getVersion());

        // 查询指定流程定义下的活动信息
        ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) repositoryService.getProcessDefinition(pd.getId());
        System.out.println(pdImpl.getActivities());

    }

    //	3.删除流程规则
    @Test
    public void deleteDeploy() throws Exception {
        String deploymentId ="1";
        // 普通删除，如果当前规则下有正在执行的流程，会报错。
        repositoryService.deleteDeployment(deploymentId );
        // 级联删除，会删除所有相关的信息，包括正在执行的流程。相对比较暴力，慎用！！
        repositoryService.deleteDeployment(deploymentId,true );
    }
    //	4.获取流程图
    // 获取仓库中，图片文件对应的输入流
    @Test
    public void viewImage() throws Exception {
        // 通过部署ID获取到某一次部署下的所有资源文件名称集合
        String deploymentId = "1"; // ACT_RE_DEPLOYMENT 表id
        List<String> names = repositoryService.getDeploymentResourceNames(deploymentId );
        // 添加规则，得到图片名称
        String imageName = null;
        for (String name : names) {
            if(name.indexOf(".png")>=0){
                imageName = name;
            }
        }
        // 通过部署ID和文件名称得到对应文件的输入流
        if(imageName!=null){
            InputStream in  = repositoryService.getResourceAsStream(deploymentId, imageName);
            // 使用工具完成文件的拷贝
            File file  = new File("/Users/person/spring-activiti/src/main/resources/xxx.png");
            FileUtils.copyInputStreamToFile(in, file);
        }
    }


}
