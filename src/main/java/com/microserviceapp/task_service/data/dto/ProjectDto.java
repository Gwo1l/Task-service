package com.microserviceapp.task_service.data.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDto {
    Long id;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
