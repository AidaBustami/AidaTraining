package com.example.demo.test.Services;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.Thread;
import com.example.demo.test.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class JobService {

    @Autowired
    TaskService taskservice;
    @Autowired
    WorkerService workerservice;
    @Autowired
    MachineService machineservice;
    @Autowired
    UserService userService;
    @Autowired
    ThreadService  threadService;
    public JobService()
    {
    }

    @KafkaListener(topics = "topic")
    public void consumeFromTopic(String task)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(task);
            Task task1 = taskservice.findById(Long.parseLong(jsonObject.getString("id"))).get();
            task1.setName(jsonObject.getString("name"));
            Optional<Thread> thread = threadService.findById(Long.parseLong(jsonObject.getString("thread")) );
            task1.setThread(thread.get());
            task1.setStatus(Integer.parseInt(jsonObject.getString("status")));
            taskservice.save(task1);
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
    }
    public String addTaskToMachine(Task task, Machine machine)
    {
            String isAdded = machineservice.addTaskToMachine(task, machine);
            if (isAdded.equals("added"))
            {
                workerservice.work(task);
                return "task is added ";
            }
            else
            {
                return "task is  not added ";
            }
    }
    public void deleteTasks(Machine tempMachine)

    {
        userService.deleteUserToTaskAndUserToMachine(tempMachine);
        taskservice.deleteTaskToMachineReference(tempMachine);
    }
    public Task checkIfTaskIdExsist(Long taskId)
    {
        return taskservice.checkIfTaskIdExsit(taskId);
    }
    public Optional<User> findUser(Long userId)
    {
        return  userService.findUser(userId);
    }
    public void addTaskByUser(User user, Task task)
    {
        userService.addTaskByUser(user, task);
    }
    public Optional<Machine> findMachine(Long machineId)
    {
       return machineservice.findMachine(machineId);
    }
    public void addMachineByUser(Machine machine, User user)
    {
      userService.addMachineByUser(machine, user);
    }
    public Task createTask(Task task) {
      return   taskservice.createTask(task) ;
    }
    public long taskCount() {
       return taskservice.count();
    }
}
