package com.example.demo.web;

import com.example.demo.exception.InvalidTaskStatusException;
import com.example.demo.model.TaskStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusConverter implements Converter<String, TaskStatus> {

    @Nullable
    @Override
    public TaskStatus convert(@Nullable String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        try {
            return TaskStatus.from(source);
        } catch (IllegalArgumentException ex) {
            throw new InvalidTaskStatusException(source);
        }
    }
}
