package com.taskapp.task_tracker_backend.domain.mappers;

import com.taskapp.task_tracker_backend.domain.dto.TaskListDto;
import com.taskapp.task_tracker_backend.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);

}
