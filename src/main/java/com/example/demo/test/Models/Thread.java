package com.example.demo.test.Models;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thread_id", nullable = false)
    Long thread_id;
    boolean busy = false;
    @OneToOne(mappedBy = "thread")
    Task task;
    public Thread() {
    }
}
