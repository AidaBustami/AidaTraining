package com.example.demo.test.Services;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.User;
import com.example.demo.test.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserService()
    {
    }
   public void addTaskByUser(User user , Task task)
   {
        user.getTaskByUser().add(task);
        userRepository.save(user);
    }
    public void addMachineByUser(Machine machine, User user)
    {
        user.getMachineByUser().add(machine);
        userRepository.save(user);
    }
    public Optional<User> findUser(Long userId)
    {
        return userRepository.findById(userId);
    }
    public void save(User user)
    {
        userRepository.save(user);
    }
    public void deleteUserToMachineReference(Machine machine)
    {
        List<User> allUsers = userRepository.findAll();
        for (int i = 0; i < userRepository.count(); i++)
        {
            User user = allUsers.get(i);
            Set<Machine> allMachines = user.getMachineByUser();
            for (int j = 0; j < allMachines.size(); j++)
            {
                Machine machine1 = allMachines.iterator().next();
                if (machine1.getMachineId() == machine.getMachineId())
                {
                    allMachines.remove(machine1);
                }
            }
            userRepository.save(user);
        }
    }
    public void deleteUserToTaskReference(Task tempTask)
    {
        Set<User> allUsers = tempTask.getUserList();
        Iterator iterator = allUsers.iterator();
        for(int y=0; y< allUsers.size() ;y++)
        {
            User user = (User) iterator.next();
            Set<Task>   allTasks=   user.getTaskByUser();
            for(int u=0;u< allTasks.size() ;u++)
            {
                Task task= allTasks.iterator().next();
                if(task.getTaskId() ==tempTask.getTaskId() )
                {
                    allTasks.remove(task);
                }
            }
           userRepository.save(user);
        }
    }
    public String deleteUserToTaskPointer(Machine deletedMachine)
    {
        Set<Task> allTasks = deletedMachine.getAllTasks();
        User user = null;
        if (allTasks.isEmpty())
            return "machine has no tasks to be deleted";
        Iterator iterator = allTasks.iterator();
        for (int i = 0; i < allTasks.size(); i++)
        {
            Task task = (Task) iterator.next();
            deleteUserToTaskReference(task);
        }
        return "successes";
    }
    public void deleteUserToTaskAndUserToMachine(Machine tempMachine) {
        deleteUserToMachineReference(tempMachine);
        deleteUserToTaskPointer(tempMachine);
    }
}
