package com.devsenior.projects;

import com.devsenior.projects.manager.ProjectManager;
import com.devsenior.projects.model.Project;
import com.devsenior.projects.model.Task;
import com.devsenior.projects.exceptions.*;

public class App {
    public static void main(String[] args) throws ArrayFullException {
        ProjectManager manager = new ProjectManager();

        System.out.println("==================================================");
        System.out.println("   FASE 1: DEMOSTRACIÓN DE ESCENARIOS EXITOSOS   ");
        System.out.println("==================================================");

        try {
            // 1. Añadir dos proyectos válidos
            Project proj1 = new Project("P-01", "E-Commerce Website", "Developing online store");
            Project proj2 = new Project("P-02", "Mobile App", "Building Android/iOS app");
            
            manager.addProject(proj1);
            manager.addProject(proj2);
            System.out.println("[OK] Proyectos iniciales agregados con éxito.");

            // 2. Añadir tareas únicas dentro de cada proyecto
            manager.addTaskToProject("P-01", new Task("T-01", "Database design", "HIGH"));
            manager.addTaskToProject("P-01", new Task("T-02", "Payment gateway integration", "HIGH"));
            manager.addTaskToProject("P-02", new Task("T-01", "UI wireframes creation", "MEDIUM")); // ID repetido pero en diferente proyecto (Válido)
            manager.addTaskToProject("P-02", new Task("T-03", "Push notifications config", "LOW"));
            System.out.println("[OK] Tareas asignadas correctamente.");

            // 3. Marcar tareas como completadas
            manager.markTaskCompletedInProject("P-01", "T-01");
            manager.markTaskCompletedInProject("P-02", "T-01");
            System.out.println("[OK] Tareas marcadas como completadas con éxito.");

            // 4. Buscar un proyecto y una tarea específica
            Project foundProject = manager.findProject("P-01");
            Task foundTask = foundProject.findTask("T-02");
            System.out.println("[OK] Búsqueda exitosa -> Proyecto encontrado: " + foundProject.getName());
            System.out.println("     Tarea encontrada en el proyecto: " + foundTask.getDescription());

            // 5. Listar estado actual
            manager.listAllProjects();

        } catch (Exception e) {
            System.out.println("[ERROR INESPERADO EN FASE EXITOSA]: " + e.getMessage());
        }


        System.out.println("\n==================================================");
        System.out.println("   FASE 2: PROBANDO ESCENARIOS DE ERROR (CATCH)   ");
        System.out.println("==================================================");

        // --- ERROR 1: Datos inválidos en Proyecto (Unchecked) ---
        try {
            System.out.println("\n-> Intentando agregar proyecto con nombre vacío...");
            manager.addProject(new Project("P-03", "", "Invalid project"));
        } catch (InvalidDataException e) {
            System.out.println("[CAPTURED] InvalidDataException: " + e.getMessage());
        }

        // --- ERROR 2: ID de Proyecto Duplicado (Unchecked) ---
        try {
            System.out.println("\n-> Intentando agregar un proyecto con ID existente ('P-01')...");
            manager.addProject(new Project("P-01", "Duplicate Project", "Description"));
        } catch (IdAlreadyExistsException e) {
            System.out.println("[CAPTURED] IdAlreadyExistsException: " + e.getMessage());
        }

        // --- ERROR 3: Array de Proyectos Lleno (Checked) ---
        try {
            System.out.println("\n-> Intentando llenar el registro para forzar ArrayFullException en proyectos...");
            // Ya tenemos 2 proyectos. Forzamos meter 4 más para superar el límite de 5.
            manager.addProject(new Project("P-03", "Proj 3", "Desc"));
            manager.addProject(new Project("P-04", "Proj 4", "Desc"));
            manager.addProject(new Project("P-05", "Proj 5", "Desc"));
            manager.addProject(new Project("P-06", "Proj 6", "Este debería fallar porque el límite es 5"));
        } catch (ArrayFullException e) {
            System.out.println("[CAPTURED] ArrayFullException (Manager): " + e.getMessage());
        }

        // --- ERROR 4: Agregar tarea a proyecto inexistente (Checked) ---
        try {
            System.out.println("\n-> Intentando añadir tarea a un proyecto fantasma ('P-99')...");
            manager.addTaskToProject("P-99", new Task("T-99", "Ghost task", "LOW"));
        } catch (ProjectNotFoundException e) {
            System.out.println("[CAPTURED] ProjectNotFoundException: " + e.getMessage());
        } catch (ArrayFullException e) {
            System.out.println("Error de capacidad: " + e.getMessage());
        }

        // --- ERROR 5: Tarea con ID Duplicado en el mismo proyecto (Unchecked) ---
        try {
            System.out.println("\n-> Intentando duplicar la tarea 'T-02' en el proyecto 'P-01'...");
            manager.addTaskToProject("P-01", new Task("T-02", "Duplicate task identification", "MEDIUM"));
        } catch (IdAlreadyExistsException e) {
            System.out.println("[CAPTURED] IdAlreadyExistsException (Task): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Otro error: " + e.getMessage());
        }

        // --- ERROR 6: Array de Tareas Lleno en un Proyecto (Checked) ---
        try {
            System.out.println("\n-> Intentando llenar la lista de tareas del proyecto 'P-01' para forzar su límite (5)...");
            // P-01 ya tiene T-01 y T-02. Agregamos 3 más para completar 5, y la sexta fallará.
            manager.addTaskToProject("P-01", new Task("T-03", "Task 3", "LOW"));
            manager.addTaskToProject("P-01", new Task("T-04", "Task 4", "LOW"));
            manager.addTaskToProject("P-01", new Task("T-05", "Task 5", "LOW"));
            manager.addTaskToProject("P-01", new Task("T-06", "Esta tarea desbordará el array", "HIGH"));
        } catch (ArrayFullException e) {
            System.out.println("[CAPTURED] ArrayFullException (Project Tasks): " + e.getMessage());
        } catch (ProjectNotFoundException e) {
            System.out.println("Proyecto no encontrado: " + e.getMessage());
        }

        // --- ERROR 7: Marcar tarea que no existe (Checked) ---
        try {
            System.out.println("\n-> Intentando completar la tarea 'T-88' que no existe en 'P-02'...");
            manager.markTaskCompletedInProject("P-02", "T-88");
        } catch (TaskNotFoundException e) {
            System.out.println("[CAPTURED] TaskNotFoundException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // --- ERROR 8: Marcar una tarea que ya está completada (Checked) ---
        try {
            System.out.println("\n-> Intentando completar de nuevo la tarea 'T-01' en 'P-02' (ya estaba completada)...");
            manager.markTaskCompletedInProject("P-02", "T-01");
        } catch (TaskAlreadyCompletedException e) {
            System.out.println("[CAPTURED] TaskAlreadyCompletedException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println("      ¡PRUEBAS COMPLETADAS EXITOSAMENTE!         ");
        System.out.println("==================================================");
    }
}
