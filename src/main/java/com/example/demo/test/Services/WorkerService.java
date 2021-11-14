package com.example.demo.test.Services;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.Thread;
import com.fasterxml.jackson.core.JsonProcessingException;
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
                currentTask.getThread().setBusy(false);
                threadservice.save(currentTask.getThread());
                currentTask.setThread(null);
                taskservice.save(currentTask);
                ObjectMapper mapper = new ObjectMapper();
                try {
                   String json = mapper.writeValueAsString(currentTask);
                    kafkaTemplate.send("topic", json);
                    System.out.println(json);
               } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
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