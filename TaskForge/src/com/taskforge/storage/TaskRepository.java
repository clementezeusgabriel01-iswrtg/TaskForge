package com.taskforge.storage;

import com.taskforge.model.Task;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class TaskRepository {
    private static final String DATA_FILE = "data/tasks.dat";
    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public TaskRepository() {
        load();
    }

    public void add(Task task) {
        tasks.add(task);
        save();
    }

    public Optional<Task> findById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst();
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    public boolean update(Task updated) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updated.getId()) {
                tasks.set(i, updated);
                save();
                return true;
            }
        }
        return false;
    }

    public boolean delete(int id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (removed) save();
        return removed;
    }

    public int getNextId() {
        return nextId++;
    }

    public void replaceAll(List<Task> newTasks) {
        this.tasks = new ArrayList<>(newTasks);
        this.nextId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
        save();
    }

    // Custom text-based serialization (zero dependency)
    private void save() {
        try {
            Path dir = Paths.get("data");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DATA_FILE))) {
                writer.write("NEXT_ID=" + nextId);
                writer.newLine();
                for (Task t : tasks) {
                    // Format: id|title|description|category|priority|dueDate|completed|createdAt|completedAt
                    writer.write(String.join("|",
                            String.valueOf(t.getId()),
                            escape(t.getTitle()),
                            escape(t.getDescription()),
                            escape(t.getCategory()),
                            t.getPriority().name(),
                            t.getDueDate() != null ? t.getDueDate().toString() : "",
                            String.valueOf(t.isCompleted()),
                            t.getCreatedAt() != null ? t.getCreatedAt().toString() : "",
                            t.getCompletedAt() != null ? t.getCompletedAt().toString() : ""
                    ));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
        }
    }

    private void load() {
        Path path = Paths.get(DATA_FILE);
        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            if (line != null && line.startsWith("NEXT_ID=")) {
                nextId = Integer.parseInt(line.substring(8));
            }

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 9) continue;

                LocalDate due = parts[5].isEmpty() ? null : LocalDate.parse(parts[5]);
                LocalDate created = parts[7].isEmpty() ? LocalDate.now() : LocalDate.parse(parts[7]);
                LocalDate completedAt = parts[8].isEmpty() ? null : LocalDate.parse(parts[8]);

                Task t = new Task(
                        Integer.parseInt(parts[0]),
                        unescape(parts[1]),
                        unescape(parts[2]),
                        unescape(parts[3]),
                        Task.Priority.valueOf(parts[4]),
                        due,
                        Boolean.parseBoolean(parts[6]),
                        created,
                        completedAt
                );
                tasks.add(t);
            }
        } catch (Exception e) {
            System.err.println("Failed to load tasks (starting fresh): " + e.getMessage());
            tasks.clear();
            nextId = 1;
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("|", "¦").replace("\n", "\\n");
    }

    private String unescape(String s) {
        return s.replace("¦", "|").replace("\\n", "\n");
    }
}
