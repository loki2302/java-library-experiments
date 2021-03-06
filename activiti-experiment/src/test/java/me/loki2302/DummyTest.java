package me.loki2302;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DummyTest {

    // the report process is triggered
    // accountant prepares the report
    // manager checks if everything is ok
    // done
    @Test
    public void playADummyScenario() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
                .setAsyncExecutorEnabled(true)
                .setAsyncExecutorActivate(false)
                .setJobExecutorActivate(false)
                .buildProcessEngine();

        IdentityService identityService = processEngine.getIdentityService();
        identityService.saveGroup(identityService.newGroup("management"));
        identityService.saveGroup(identityService.newGroup("accountancy"));

        User accountantUser = identityService.newUser("accountant");
        accountantUser.setPassword("accountant");
        identityService.saveUser(accountantUser);
        identityService.createMembership("accountant", "accountancy");

        User managerUser = identityService.newUser("manager");
        managerUser.setPassword("manager");
        identityService.saveUser(managerUser);
        identityService.createMembership("manager", "management");

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("FinancialReportProcess.bpmn20.xml")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");

        TaskService taskService = processEngine.getTaskService();

        // MANAGER HAS NOTHING TO DO
        assertEquals(0, taskService.createTaskQuery().taskCandidateUser("manager").list().size());
        //

        // ACCOUNTANT DOES THEIR JOB
        List<Task> tasksAvailableToAccountant = taskService.createTaskQuery().taskCandidateUser("accountant").list();
        assertEquals(1, tasksAvailableToAccountant.size());

        Task theOnlyTaskAvailableToAccountant = tasksAvailableToAccountant.get(0);
        taskService.claim(theOnlyTaskAvailableToAccountant.getId(), "accountant");
        taskService.complete(theOnlyTaskAvailableToAccountant.getId());
        //

        // ACCOUNTANT HAS NOTHING TO DO
        assertEquals(0, taskService.createTaskQuery().taskCandidateUser("accountant").list().size());
        //

        // THE PROCESS IS NOT FINISHED YET
        assertNull(processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult()
                .getEndTime());
        //

        // MANAGER DOES THEIR JOB
        List<Task> tasksAvailableToManager = taskService.createTaskQuery().taskCandidateUser("manager").list();
        assertEquals(1, tasksAvailableToManager.size());

        Task theOnlyTaskAvailableToManager = tasksAvailableToManager.get(0);
        taskService.claim(theOnlyTaskAvailableToManager.getId(), "manager");
        taskService.complete(theOnlyTaskAvailableToManager.getId());
        //

        // MANAGER HAS NOTHING TO DO
        assertEquals(0, taskService.createTaskQuery().taskCandidateUser("manager").list().size());
        //

        // THE PROCESS IS FINISHED
        assertNotNull(processEngine.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult()
                .getEndTime());
        //
    }

    @Test
    public void xxx() {
        UserTask userTask = new UserTask();
        userTask.setId("tid");
        userTask.setCandidateGroups(Arrays.asList("dummies"));

        Process p = new Process();
        p.setName("pname");
        p.setId("pid");
        p.addFlowElement(new StartEvent() {{
            setId("start");
        }}.clone());
        p.addFlowElement(userTask);
        p.addFlowElement(new EndEvent() {{setId("end");}}.clone());
        p.addFlowElement(new SequenceFlow("start", "tid"));
        p.addFlowElement(new SequenceFlow("tid", "end"));

        BpmnModel m = new BpmnModel();
        m.addProcess(p);

        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
                .setJdbcUrl("jdbc:h2:mem:my-own-db2;DB_CLOSE_DELAY=1000")
                .setAsyncExecutorEnabled(true)
                .setAsyncExecutorActivate(false)
                .setJobExecutorActivate(false)
                .buildProcessEngine();

        IdentityService identityService = processEngine.getIdentityService();
        identityService.saveGroup(identityService.newGroup("dummies"));

        User dummyUser = identityService.newUser("dummy");
        dummyUser.setPassword("dummy");
        identityService.saveUser(dummyUser);
        identityService.createMembership("dummy", "dummies");

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addBpmnModel("xxx.bpmn", m)
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("pid");

        assertEquals(1, processEngine.getTaskService()
                .createTaskQuery()
                .taskCandidateGroup("dummies").list().size());
    }
}
