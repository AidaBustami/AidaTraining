package com.example.demo.test.Models;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    int type ;  	//Start , Shutdown,Restart ,Configure ,	Status
    String 	Description ;
    @CreatedDate
    LocalDateTime Created_Date   ;
    int Last_execution_time ;
    int Number_of_trials ;
    int Next_execution_time  ;
    int Priority ;
    @NotEmpty
    String name ;
    int status = 0; /*[Pending 0, In-Progress 1 , Completed 2 ,   aborted 3 and failed 4*/

    @OneToOne()
    @JoinColumn(name = "threadId", referencedColumnName = "thread_id")
    Thread thread;

    @ManyToMany(mappedBy = "allTasks")
    private Set<Machine> machineList = new HashSet<Machine>();

    @ManyToMany(mappedBy = "taskByUser")
    private Set<User> userList = new HashSet<User>();
    public Task()
    {
    }
    public void removeMachine(Machine machine)
    {
        machineList.remove(machine);
    }
}