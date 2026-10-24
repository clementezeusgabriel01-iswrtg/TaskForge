package com.taskforge;

import com.taskforge.model.Task;
import com.taskforge.service.AnalyticsService;
import com.taskforge.service.TaskService;
import com.taskforge.storage.TaskRepository;
import com.taskforge.util.InputHelper;

import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final TaskRepository repo = new TaskRepository();
    private static final TaskService service = new TaskService(repo);
    private static final AnalyticsService analytics = new AnalyticsService(service);

    public static void main(String[] args) {
        System.out.println("+--------------------------------------+");
        System.out.println("|         TASKFORGE v1.0               |");
        System.out.println("|   Zero-Dependency Task Manager       |");
        System.out.println("+--------------------------------------+");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputHelper.readInt("Choice: ");
            switch (choice) {
                case 1: addTask(); break;
                case 2: listTasks(service.getAll()); break;
                case 3: updateTask(); break;
                case 4: completeTask(); break;
                case 5: deleteTask(); break;
                case 6: searchTasks(); break;
                case 7: filterMenu(); break;
                case 8: analytics.printDashboard(); break;
                case 9: service.undo(); break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Add Task");
        System.out.println("2. List All Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Mark Complete");
        System.out.println("5. Delete Task");
        System.out.println("6. Search");
        System.out.println("7. Filter");
        System.out.println("8. Analytics Dashboard");
        System.out.println("9. Undo Last Action");
        System.out.println("0. Exit");
    }

    private static void addTask() {
        String title = InputHelper.readLine("Title: ");
        String desc = InputHelper.readLine("Description: ");
        String cat = InputHelper.readLine("Category: ");
        Task.Priority p = InputHelper.readPriority();
        LocalDate due = InputHelper.readDate("Due date");
        Task t = service.create(title, desc, cat, p, due);
        System.out.println("Created: " + t);
    }

    private static void listTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        System.out.println();
        for (Task t : tasks) {
            System.out.println(t);
        }
    }

    private static void updateTask() {
        int id = InputHelper.readInt("Task ID: ");
        System.out.println("(Leave blank to keep current value)");
        String title = InputHelper.readLine("New title: ");
        String desc = InputHelper.readLine("New description: ");
        String cat = InputHelper.readLine("New category: ");
        System.out.print("New priority (or blank): ");
        String pStr = InputHelper.readLine("");
        Task.Priority p = null;
        if (!pStr.isEmpty()) {
            try {
                p = Task.Priority.valueOf(pStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority, keeping current.");
            }
        }
        LocalDate due = InputHelper.readDate("New due date");

        if (service.update(id,
                title.isEmpty() ? null : title,
                desc.isEmpty() ? null : desc,
                cat.isEmpty() ? null : cat,
                p, due)) {
            System.out.println("Updated successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void completeTask() {
        int id = InputHelper.readInt("Task ID to complete: ");
        if (service.complete(id)) {
            System.out.println("Marked as complete.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void deleteTask() {
        int id = InputHelper.readInt("Task ID to delete: ");
        if (service.delete(id)) {
            System.out.println("Deleted.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void searchTasks() {
        String kw = InputHelper.readLine("Keyword: ");
        listTasks(service.search(kw));
    }

    private static void filterMenu() {
        System.out.println("1. Open tasks");
        System.out.println("2. Completed tasks");
        System.out.println("3. By priority");
        System.out.println("4. Overdue only");
        int c = InputHelper.readInt("Filter: ");
        switch (c) {
            case 1: listTasks(service.filterByStatus(false)); break;
            case 2: listTasks(service.filterByStatus(true)); break;
            case 3:
                Task.Priority p = InputHelper.readPriority();
                listTasks(service.filterByPriority(p));
                break;
            case 4: listTasks(service.getOverdue()); break;
            default: System.out.println("Invalid.");
        }
    }
}
