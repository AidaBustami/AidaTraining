package com.example.demo.test.Models;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToMany
    @JoinTable(
            name = "UserWithTask",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "taskId"))
    private Set<Task> taskByUser = new HashSet<Task>();
    @ManyToMany
    @JoinTable(
            name = "userWithMachine",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "machineId"))
    private Set<Machine> machineByUser = new HashSet<Machine>();
    public User() {
    }

}
