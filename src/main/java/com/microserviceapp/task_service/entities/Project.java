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
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "project_seq"
    )
    @SequenceGenerator(
            name = "project_seq",
            sequenceName = "project_sequence",
            allocationSize = 20
    )
    private Long id;
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TaskState> taskStates = new ArrayList<>();

}
