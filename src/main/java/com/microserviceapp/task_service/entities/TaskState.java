package com.microserviceapp.task_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_states")
public class TaskState {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_state_seq")
    @SequenceGenerator(
            name = "task_state_seq",
            sequenceName = "task_state_sequence",
            allocationSize = 20
    )
    @Id
    private Long id;
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "taskState")
    private List<Task> tasks = new ArrayList<>();

}
