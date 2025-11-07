package com.taskapp.task_tracker_backend.domain.mappers;

import com.taskapp.task_tracker_backend.domain.dto.TaskDto;
import com.taskapp.task_tracker_backend.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);

}
