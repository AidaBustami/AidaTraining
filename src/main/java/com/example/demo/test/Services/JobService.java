package com.example.demo.test.Services;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public JobService() {
    }

    @KafkaListener(topics = "topic")
    public void consumeFromTopic(String task) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Task task1 = mapper.readValue(task, Task.class);
            taskservice.save(task1);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public Machine addTaskToMachine(Task task, Machine machine) {
        Machine result = machineservice.addTaskToMachine(task, machine);
        if (result != null) {
            workerservice.work(task);
            return result;
        } else {
            return null;
        }
    }

    public void deleteTasks(Machine tempMachine) {
        userService.deleteUserToTaskAndUserToMachine(tempMachine);
        taskservice.deleteTaskToMachineReference(tempMachine);
    }

    public Optional<User> findUser(Long userId) {
        return userService.findUser(userId);
    }
    public void addTaskByUser(User user, Task task) {
        userService.addTaskByUser(user, task);
    }
    public Optional<Machine> findMachine(Long machineId) {
        return machineservice.findMachine(machineId);
    }
    public void addMachineByUser(Machine machine, User user) {
        userService.addMachineByUser(machine, user);
    }
    public Task saveTask(Task task) {
        return taskservice.save(task);
    }
}
