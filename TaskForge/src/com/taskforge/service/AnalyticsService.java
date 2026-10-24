package com.taskforge.service;

import com.taskforge.model.Task;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    private final TaskService taskService;

    public AnalyticsService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void printDashboard() {
        List<Task> all = taskService.getAll();
        if (all.isEmpty()) {
            System.out.println("No tasks yet. Add some to see analytics.");
            return;
        }

        long total = all.size();
        long completed = all.stream().filter(Task::isCompleted).count();
        long open = total - completed;
        long overdue = all.stream().filter(Task::isOverdue).count();

        System.out.println("\n========== TASKFORGE ANALYTICS ==========");
        System.out.printf("Total Tasks     : %d%n", total);
        System.out.printf("Completed       : %d (%.1f%%)%n", completed, (completed * 100.0) / total);
        System.out.printf("Open            : %d%n", open);
        System.out.printf("Overdue         : %d%n", overdue);

        System.out.println("\n--- Priority Distribution ---");
        Map<Task.Priority, Long> byPriority = all.stream()
                .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
        for (Task.Priority p : Task.Priority.values()) {
            System.out.printf("%-10s : %d%n", p, byPriority.getOrDefault(p, 0L));
        }

        System.out.println("\n--- Top Categories ---");
        Map<String, Long> byCat = all.stream()
                .collect(Collectors.groupingBy(Task::getCategory, Collectors.counting()));
        byCat.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.printf("%-15s : %d%n", e.getKey(), e.getValue()));

        System.out.println("=========================================\n");
    }
}
