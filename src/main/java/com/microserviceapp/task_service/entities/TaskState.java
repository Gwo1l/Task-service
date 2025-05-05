package com.microserviceapp.task_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @Builder.Default
    @OneToMany(mappedBy = "taskState", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

}
