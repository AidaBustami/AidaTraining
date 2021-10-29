package com.example.demo.test.Services;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Repositories.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
public class MachineService {

    @Autowired
    MachineRepository machineRepository;
    public MachineService()
    {
    }
    public Optional<Machine> findMachine(Long machineId)
    {
        return machineRepository.findById(machineId);
    }
    public String addTaskToMachine(Task task, Machine machine) {
            Set<Task> allTasks = machine.getAllTasks();
            for (int i = 0; i < allTasks.size(); i++)
            {
                Task task1 = allTasks.iterator().next();
                if ((task1.getName().equals(task.getName())) && (task1.getStatus() <= 1))
                    return "can't add , task name must be unique unless task reached final state";
            }
            machine.getAllTasks().add(task);
            machineRepository.save(machine);
            return "added" ;
    }
    public void delete(Machine tempMachine)
    {
        machineRepository.delete(tempMachine);
    }
    public void save(Machine machine)
    {
        machineRepository.save(machine);
    }
}
