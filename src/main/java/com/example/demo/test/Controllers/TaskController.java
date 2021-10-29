package com.example.demo.test.Controllers;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.User;
import com.example.demo.test.Services.JobService;
import com.example.demo.test.Services.ThreadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/Task")
public class TaskController {
    @Autowired
    ThreadService threadservice;
    @Autowired
    JobService jobService;
    @Autowired
    ObjectMapper mapper;

    @PutMapping("/add_task")
    public String addTask(@RequestBody ObjectNode object) {
        //here to add task , I need to know the task id , on which machine (machine id) the task will be added
        // And which user added the task.
        Long  taskId =object.get("taskId").asLong();
        Long  machineId =object.get("machineId").asLong();
        Long  userId =object.get("userId").asLong();
        // I  used the job service because it contains reference to task service.
        Task task = jobService.checkIfTaskIdExsist(taskId);
        if (task == null)
            return "task  does not exist";
        // I  used the job service because it contains reference to user service.
        Optional<User> optionalUser = jobService.findUser(userId);
        if (optionalUser.isPresent())
        {
            jobService.addTaskByUser(optionalUser.get(), task);
            // I  used the job service because it contains reference to machine  service.
            Optional<Machine> optionalMachine = jobService.findMachine(machineId);
            if (optionalMachine.isPresent())
            {
                jobService.addMachineByUser(optionalMachine.get(), optionalUser.get());
                jobService.addTaskToMachine(task, optionalMachine.get());
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("id", task.getTaskId());
                objectNode.put("name", task.getName());
                objectNode.put("status", task.getStatus());
                objectNode.put("thread", task.getThread().getThread_id());
                return objectNode.toString() ;
            }
            else
            {
                return "machine does not exist";
            }
        }
        else
        {
            return "user with this id does not exist";
        }
    }
    @PostMapping("/create_TASK")
    public Task createTask(@RequestBody Task task)
    {
        initializeThreads();
        return jobService.createTask(task);
    }
    void initializeThreads()
    {
        if (jobService.taskCount() == 0)
        {
            threadservice.intilaize();
        }
    }
}