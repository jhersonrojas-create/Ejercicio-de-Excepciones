package com.devsenior.projects.manager;

import com.devsenior.projects.model.Project;
import com.devsenior.projects.model.Task;
// Importamos nuestras excepciones (ajusta las mayúsculas/minúsculas según tu paquete)
import com.devsenior.projects.exceptions.ProjectNotFoundException;
import com.devsenior.projects.exceptions.TaskNotFoundException;
import com.devsenior.projects.exceptions.TaskAlreadyCompletedException;
import com.devsenior.projects.exceptions.ArrayFullException;
import com.devsenior.projects.exceptions.InvalidDataException;
import com.devsenior.projects.exceptions.IdAlreadyExistsException;

public class ProjectManager {
    private Project[] projects;
    private int projectCount;

    // Constructor con tamaño fijo para el array de proyectos
    public ProjectManager() {
        this.projects = new Project[5]; // Capacidad para 5 proyectos de prueba
        this.projectCount = 0;
    }

    // 1. Agregar un Proyecto
    public void addProject(Project project) throws ArrayFullException {
        // Validación de datos inválidos (Unchecked)
        if (project == null || project.getProjectId() == null || project.getProjectId().trim().isEmpty()
            || project.getName() == null || project.getName().trim().isEmpty()) {
            throw new InvalidDataException("Project data or ID cannot be null or empty.");
        }

        // Verificar si el array de proyectos está lleno (Checked)
        if (projectCount >= projects.length) {
            throw new ArrayFullException("The global project manager registry is full.");
        }

        // Verificar si el ID del proyecto ya existe (Unchecked)
        for (int i = 0; i < projectCount; i++) {
            if (projects[i].getProjectId().equals(project.getProjectId())) {
                throw new IdAlreadyExistsException("Project ID '" + project.getProjectId() + "' already exists.");
            }
        }

        // Si pasa los controles, se agrega al array fijo
        projects[projectCount] = project;
        projectCount++;
    }

    // 2. Buscar un Proyecto por ID
    public Project findProject(String projectId) throws ProjectNotFoundException {
        if (projectId == null || projectId.trim().isEmpty()) {
            throw new InvalidDataException("Project ID cannot be null or empty.");
        }

        // Busqueda secuencial en las posiciones ocupadas
        for (int i = 0; i < projectCount; i++) {
            if (projects[i].getProjectId().equals(projectId)) {
                return projects[i];
            }
        }

        // Si no se encuentra en el bucle (Checked)
        throw new ProjectNotFoundException("Project with ID '" + projectId + "' was not found.");
    }

    // 3. Agregar una Tarea a un Proyecto Específico
    public void addTaskToProject(String projectId, Task task) throws ProjectNotFoundException, ArrayFullException {
        if (projectId == null || projectId.trim().isEmpty() || task == null) {
            throw new InvalidDataException("Project ID and Task details must be valid.");
        }

        // Buscamos el proyecto (puede lanzar ProjectNotFoundException)
        Project project = findProject(projectId);
        
        // Si lo encuentra, delega la inserción al proyecto (puede lanzar ArrayFullException o IdAlreadyExistsException)
        project.addTask(task);
    }

    // 4. Marcar Tarea como Completada en un Proyecto Específico
    public void markTaskCompletedInProject(String projectId, String taskId) 
            throws ProjectNotFoundException, TaskNotFoundException, TaskAlreadyCompletedException {
        if (projectId == null || projectId.trim().isEmpty() || taskId == null || taskId.trim().isEmpty()) {
            throw new InvalidDataException("Project ID and Task ID cannot be null or empty.");
        }

        // Buscamos el proyecto
        Project project = findProject(projectId);

        // Delegamos la actualización al proyecto
        project.markTaskCompleted(taskId);
    }

    // 5. Listar todos los proyectos con sus respectivas tareas
    public void listAllProjects() {
        System.out.println("\n========== GLOBAL PROJECTS REPORT ==========");
        if (projectCount == 0) {
            System.out.println("No projects registered in the system.");
            return;
        }

        for (int i = 0; i < projectCount; i++) {
            System.out.println("\n" + projects[i].toString());
            // Llama al método interno del proyecto para imprimir sus tareas
            projects[i].listTasks();
        }
        System.out.println("============================================");
    }
}
