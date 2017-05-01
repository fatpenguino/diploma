/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.ProcessDefinition;
import entities.ProcessInstance;
import entities.TaskSummary;
import entities.User;
import java.util.List;
import javax.ejb.Remote;


/**
 *
 * @author Feisal
 */
@Remote
public interface RemoteAPI {
    List<TaskSummary> getPotentialTasks(User user);
    List<TaskSummary> getTasks(User user);
    List<ProcessDefinition> getProcessDefinitions(User user);
    ProcessDefinition getProcessDefinition(String id);
    ProcessInstance getProcessInstance(String id);
    List<ProcessInstance> getProcessInstances(User user);
    String getSvgResponse(ProcessInstance processInstance);
    boolean claimTask(User user,String id);
    boolean startTask(User user,String id);
    boolean completeTask(User user,String id);
    int startProcess(User user,ProcessDefinition processDef,String param);
    int abortProcess(User user,ProcessInstance processInstance);
    void send(String to, String sub, String msg); 
}

