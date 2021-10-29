package com.example.demo.test.Services;
import com.example.demo.test.Models.Machine;
import com.example.demo.test.Models.Task;
import com.example.demo.test.Models.User;
import com.example.demo.test.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    public TaskService()
    {
    }
    public Task createTask(Task task)
    {
        Optional<Task> taskOptional = taskRepository.findById(task.getTaskId());
        if (!(taskOptional.isPresent()))
        {
            taskRepository.save(task);
            return task  ;
        }
        else
        { //alraedy exsist  with this id
            return null;
        }
    }
    public Task checkIfTaskIdExsit(Long taskId)
    {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if ((taskOptional.isPresent()))
        {
            return  taskOptional.get();
        }
        else
        {   // does not exsist
            return null;
        }
    }
    public void removeMachine(Task task, Machine deletedMachine)
    {
        task.removeMachine(deletedMachine);
        taskRepository.save(task);
    }
    public Optional<Task> findById(Long taskID)
    {
        return taskRepository.findById(taskID);
    }
    public String deleteTaskToMachineReference(Machine deletedMachine)
    {
        Set<Task> allTasks = deletedMachine.getAllTasks();
        User user = null;
        if (allTasks.isEmpty())
            return "machine has no tasks to be deleted";
        Iterator iterator = allTasks.iterator();
        for (int i = 0; i < allTasks.size(); i++)
        {
            Task task = (Task) iterator.next();
            removeMachine(task, deletedMachine);
        }
        return "successes";
    }
    public List<Task> findByStatusLessThanEqual(int i)
    {
        return taskRepository.findByStatusLessThanEqual(i);
    }
    public void save(Task currentTask)
    {
        taskRepository.save(currentTask);
    }
    public Long count()
    {
        return taskRepository.count();
    }
}
