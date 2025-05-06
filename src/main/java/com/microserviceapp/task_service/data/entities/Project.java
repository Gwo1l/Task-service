package com.microserviceapp.task_service.data.entities;

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
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskState> taskStates = new ArrayList<>();


}
