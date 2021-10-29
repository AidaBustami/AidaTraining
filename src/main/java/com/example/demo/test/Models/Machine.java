package com.example.demo.test.Models;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machineId", nullable = false)
    private Long machineId;
    @NotEmpty
    String name   ;
    @NotEmpty
    String ip;
    @NotEmpty
    String location;
    @ManyToMany
    @JoinTable(
            name = "MachineAndTask",
            joinColumns = @JoinColumn(name = "machineId"),
            inverseJoinColumns = @JoinColumn(name = "taskId"))
    private Set<Task> allTasks = new HashSet<Task>();

    @ManyToMany(mappedBy = "machineByUser")
    private Set<User> userList = new HashSet<User>();

    public Machine() {
    }
}
