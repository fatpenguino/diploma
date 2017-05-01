/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

/**
 *
 * @author fatpenguino
 */
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

/**
 * Example that uses the remote Java Client (through REST) to connect to the execution server
 * of the jBPM Console and execute the Evaluation process there.
 * 
 * This example assumes:
 *  - you have the jbpm console running at http://localhost:8080/jbpm-console
 *    (automatically when using jbpm-installer)
 *  - you have users krisv/krisv, john/john and mary/mary
 *    (automatically when using jbpm-installer)
 *  - you have deployed the Evaluation project (part of the jbpm-playground)
 *
 */
public class EvaluationExampleJavaThroughRESTOtherUser {

	public static void main(String[] args) throws Exception {
		RuntimeEngine engine = RemoteRuntimeEngineFactory.newRestBuilder()
			.addUrl(new URL("http://localhost:8080/jbpm-console"))
			.addUserName("mary").addPassword("mary")
			.addDeploymentId("org.jbpm:Evaluation:1.0")
				.build();
		KieSession ksession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();
                taskService.getTasksAssignedAsPotentialOwner("mary", "en-UK");
                System.out.println(taskService.getTasksAssignedAsPotentialOwner("mary", "en-UK").get(0).getDescription());
                
        }
	
	private static TaskSummary findTask(List<TaskSummary> tasks, long processInstanceId) {
		for (TaskSummary task: tasks) {
			if (task.getProcessInstanceId() == processInstanceId) {
				return task;
			}
		}
		throw new RuntimeException("Could not find task for process instance "
			+ processInstanceId + " [" + tasks.size() + " task(s) in total]");
	}
	
}