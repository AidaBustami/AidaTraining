package com.example.demo.test.Services;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.Thread;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class WorkerService {
    @Autowired
    TaskService taskservice;
    @Autowired
    ThreadService threadservice;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    ObjectMapper mapper;
    public WorkerService()
    {
    }
    public String work(Task task)
    {
        if (task.getStatus() > 1)
            return "task already finished ";
        List<Thread> threadList = threadservice.findByBusy(false);
        if (threadList.size() > 0)
        {
            List<Task> taskList = taskservice.findByStatusLessThanEqual(1);
            if (taskList.size() > 0)
            {
                Thread currentThread = threadList.get(0);
                Task currentTask = taskList.get(0);
                currentThread.setBusy(true);
                currentThread.setTask(currentTask);
                threadservice.save(currentThread);
                currentTask.setStatus(1);
                currentTask.setThread(currentThread);
                taskservice.save(currentTask);
                try
                {
                    TimeUnit.SECONDS.sleep(5);
                }
                catch (Exception e)
                {
                    System.out.print(e);
                }
                currentTask.setStatus(2);
                taskservice.save(currentTask);
                currentTask.getThread().setBusy(false);
                currentTask.getThread().setTask(null);
                threadservice.save(currentTask.getThread());
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("id", currentTask.getTaskId());
                objectNode.put("name", currentTask.getName());
                objectNode.put("status", currentTask.getStatus());
                objectNode.put("thread", currentTask.getThread().getThread_id());
                kafkaTemplate.send("topic", objectNode.toString());
                return "task is assigned to a thread";
            }
            else
            {
                return "all tasks reached final state";
            }
        }
        return " no thread is free";
    }
}