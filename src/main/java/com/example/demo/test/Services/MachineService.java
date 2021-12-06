package com.example.demo.test.Services;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Repositories.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service
public class MachineService {
    @Autowired
    MachineRepository machineRepository;
    public MachineService() {
    }
    public Optional<Machine> findMachine(Long machineId) {
        return machineRepository.findById(machineId);
    }

    public Machine addTaskToMachine(Task task, Machine machine) {
        Set<Task> allTasks = machine.fetchAllTasks();
        for (int i = 0; i < allTasks.size(); i++) {
            Task task1 = allTasks.iterator().next();
            // return "can't add , task name must be unique unless task reached final state";
            if ((task1.getName().equals(task.getName())) && (task1.getStatus() <= 1))
                return null;
        }
        machine.fetchAllTasks().add(task);
        return machineRepository.save(machine);
    }
    public void delete(Long machineId) {
        machineRepository.deleteById(machineId);
    }

    public Machine save(Machine machine) {
        return machineRepository.save(machine);
    }

    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    public Optional<Machine> findById(Long machineId) {
        return machineRepository.findById(machineId);
    }
}
