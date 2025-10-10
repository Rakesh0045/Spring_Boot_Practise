package com.taskapp.task_tracker_backend.domain.mappers.impl;

import com.taskapp.task_tracker_backend.domain.dto.TaskListDto;
import com.taskapp.task_tracker_backend.domain.entities.Task;
import com.taskapp.task_tracker_backend.domain.entities.TaskList;
import com.taskapp.task_tracker_backend.domain.entities.TaskStatus;
import com.taskapp.task_tracker_backend.domain.mappers.TaskListMapper;
import com.taskapp.task_tracker_backend.domain.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {

    private final TaskMapper taskMapper;
    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }
    @Override
    public TaskList fromDto(TaskListDto dto) {
        return new TaskList(
                dto.id(),
                dto.title(),
                dto.description(),
                Optional.ofNullable(dto.tasks())
                        .map(tasks-> tasks.stream()
                                .map(taskMapper::fromDto)
                                .toList()
                        )
                        .orElse(null),
                null,
                null
        );
    }
    @Override
    public TaskListDto toDto(TaskList taskList) {
        final List<Task> tasks = taskList.getTasks();
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                Optional.ofNullable(tasks)
                        .map(List::size)
                        .orElse(0),
                calculateTaskListProgress(tasks),
                Optional.ofNullable(tasks)
                        .map(t-> t.stream()
                                .map(taskMapper::toDto)
                                .toList()
                        )
                        .orElse(null)
        );
    }
    private Double calculateTaskListProgress(List<Task> tasks) {
        if(null == tasks) {
            return null;
        }
        long closedTaskCount = tasks.stream()
                .filter(task-> TaskStatus.CLOSED == task.getStatus())
                .count();
        return (double) closedTaskCount / tasks.size();
    }
}