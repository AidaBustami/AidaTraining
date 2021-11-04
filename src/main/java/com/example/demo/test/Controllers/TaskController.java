package com.example.demo.test.Controllers;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.User;
import com.example.demo.test.Services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/Task")
public class TaskController {
    @Autowired
    JobService jobService;
    @PutMapping("/add_task/{userId}/{machineId}")
    public Machine addTask(@RequestBody Task task1, @PathVariable Long userId, @PathVariable Long machineId) {
        Task task = jobService.saveTask(task1);
        // I  used the job service because it contains reference to user service.
        Optional<User> optionalUser = jobService.findUser(userId);
        if (optionalUser.isPresent()) {
            jobService.addTaskByUser(optionalUser.get(), task);
            // I  used the job service because it contains reference to machine  service.
            Optional<Machine> optionalMachine = jobService.findMachine(machineId);
            if (optionalMachine.isPresent()) {
                jobService.addMachineByUser(optionalMachine.get(), optionalUser.get());
                Machine result = jobService.addTaskToMachine(task, optionalMachine.get());
                return result;
            } else {
                //"machine does not exist";
                return null;
            }
        } else {
            //  "user with this id does not exist";
            return null;
        }
    }
}