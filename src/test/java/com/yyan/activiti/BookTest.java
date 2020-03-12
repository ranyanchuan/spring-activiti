package com.yyan.activiti;


import com.yyan.App;
import com.yyan.pojo.Book;
import com.yyan.service.BookService;
import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {App.class})
public class BookTest {


    @Autowired
    private BookService bookService;

    //  初始化流程引擎对象
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    //  使用流程引擎获取到需要的服务对象实例
    private RepositoryService repositoryService = processEngine.getRepositoryService();


    /**
     * 验证数据库
     */
    @Test
    public void testInsertBook() {

        Book book = new Book();
        book.setTitle("川 web ");
        book.setPage(200);
        bookService.insertBook(book);
    }


    /**
     * 1、流程发布
     */
    @Test
    public void testDeployFlow() {

        // 创建流程发布配置对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        // 在配置对象中添加发布的规则信息
        deploymentBuilder
                .name("2014请假流程") // 当前部署流程的“显示别名”
                .addClasspathResource("LeaveFlow.bpmn")// 设置规则文件
                .addClasspathResource("LeaveFlow.png"); // 设置流程图片
        // 调用方法完成流程的发布
        deploymentBuilder.deploy();
    }

    /**
     * 2、启动流程实例
     */

    @Test
    public void testStartFlow() {
        // 创建核心流程引擎对象 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建运行时服务对象
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 使用服务
        runtimeService.startProcessInstanceByKey("LeaveFlow");

    }

    /**
     * 3、查看任务
     */

    @Test
    public void testQueryTask() {

        // 创建核心流程引擎对象 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 得到查询服务
        TaskService taskService = processEngine.getTaskService();
        // 使用服务
        List<Task> list = taskService.createTaskQuery() // 创建查询对象
                .list();

        for(Task task:list){
            System.out.println("Id "+ task.getId());
            System.out.println("getTaskDefinitionKey "+ task.getTaskDefinitionKey());
            System.out.println("taskName "+ task.getName());
            System.out.println("taskOwner "+ task.getOwner());
        }
    }


    /**
     * 4、办理任务
     */
    @Test
    public void testCompleteTask() {
        // 创建核心流程引擎对象 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 得到查询服务
        TaskService taskService = processEngine.getTaskService();
        // 提交任务
        // todo 验证成功
        String taskId = "2502";
        taskService.complete(taskId);




    }




    }
