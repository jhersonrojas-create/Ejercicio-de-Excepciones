package com.devsenior.projects.model;
import java.util.Objects;

public class Task {
    private String taskId;
    private String description;
    private String priority;
    private boolean completed;

    public Task(String taskId, String description, String priority) {
        this.taskId = taskId;
        this.description = description;
        this.priority = priority;
        this.completed = false;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    // Setter para cambiar estado
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    @Override
    public String toString() {
        return "Task[ID=" + taskId + ", Desc=" + description + 
            ", Priority=" + priority + ", Completed=" + completed + "]";
    }
}
