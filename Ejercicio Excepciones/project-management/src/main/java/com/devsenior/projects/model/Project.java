package com.devsenior.projects.model;
import java.util.Objects;

import com.devsenior.projects.exceptions.ArrayFullException;
import com.devsenior.projects.exceptions.IdAlreadyExistsException;
import com.devsenior.projects.exceptions.InvalidDataException;
import com.devsenior.projects.exceptions.TaskAlreadyCompletedException;
import com.devsenior.projects.exceptions.TaskNotFoundException;

public class Project {
    private String projectId;
    private String name;
    private String description;
    private Task[] tasks;
    private int taskCount;

    // Constructor con tamaño fijo para el array de tareas (ej. 5)
    public Project(String projectId, String name, String description) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.tasks = new Task[5]; // Tamaño fijo para pruebas
        this.taskCount = 0;
    }      

    // Getters básicos
    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Task[] getTasks() { return tasks; }
    public int getTaskCount() { return taskCount; }

    // 1. Agregar Tarea
    public void addTask(Task task) throws ArrayFullException {
        // Validación de datos inválidos (Unchecked)
        if (task == null || task.getTaskId() == null || task.getTaskId().trim().isEmpty() 
            || task.getDescription() == null || task.getDescription().trim().isEmpty()) {
            throw new InvalidDataException("Task data cannot be null or empty.");
        }

        // Verificar si el array está lleno (Checked)
        if (taskCount >= tasks.length) {
            throw new ArrayFullException("The task list for project '" + name + "' is full.");
        }

        // Verificar si el ID ya existe en este proyecto (Unchecked)
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId().equals(task.getTaskId())) {
                throw new IdAlreadyExistsException("Task ID '" + task.getTaskId() + "' already exists in this project.");
            }
        }

        // Si pasa todas las validaciones, se añade
        tasks[taskCount] = task;
        taskCount++;
    }

    // 2. Marcar Tarea como Completada
    public void markTaskCompleted(String taskId) throws TaskNotFoundException, TaskAlreadyCompletedException {
        if (taskId == null || taskId.trim().isEmpty()) {
            throw new InvalidDataException("Task ID cannot be null or empty.");
        }

        // Busca la tarea reutilizando nuestro propio método findTask
        Task task = findTask(taskId);

        // Si ya está completada, lanza excepción (Checked)
        if (task.isCompleted()) {
            throw new TaskAlreadyCompletedException("Task '" + taskId + "' is already completed.");
        }

        task.setCompleted(true);
    }

    // 3. Buscar Tarea por ID
    public Task findTask(String taskId) throws TaskNotFoundException {
        if (taskId == null || taskId.trim().isEmpty()) {
            throw new InvalidDataException("Task ID cannot be null or empty.");
        }

        // Recorre solo los elementos agregados (evita NullPointerException)
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId().equals(taskId)) {
                return tasks[i];
            }
        }

        // Si termina el ciclo y no la encuentra (Checked)
        throw new TaskNotFoundException("Task with ID '" + taskId + "' not found in project '" + name + "'.");
    }

    // 4. Listar todas las tareas no nulas
    public void listTasks() {
        System.out.println("--- Tasks for Project: " + name + " ---");
        if (taskCount == 0) {
            System.out.println(" No tasks available.");
            return;
        }
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" - " + tasks[i].toString());
        }
    }

    // Equals y HashCode basados únicamente en projectId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectId, project.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }

    @Override
    public String toString() {
        return "Project[ID=" + projectId + ", Name=" + name + ", Description=" + description + ", TasksCount=" + taskCount + "]";
    }
}
