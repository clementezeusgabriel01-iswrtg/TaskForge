package com.taskforge.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String description;
    private String category;
    private Priority priority;
    private LocalDate dueDate;
    private boolean completed;
    private LocalDate createdAt;
    private LocalDate completedAt;

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public Task(int id, String title, String description, String category,
                Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = false;
        this.createdAt = LocalDate.now();
        this.completedAt = null;
    }

    // Full constructor used when loading from disk
    public Task(int id, String title, String description, String category,
                Priority priority, LocalDate dueDate, boolean completed,
                LocalDate createdAt, LocalDate completedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = completed;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public LocalDate getCreatedAt() { return createdAt; }
    public LocalDate getCompletedAt() { return completedAt; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.completedAt = completed ? LocalDate.now() : null;
    }

    public boolean isOverdue() {
        return !completed && dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String status = completed ? "[DONE]" : (isOverdue() ? "[OVERDUE]" : "[OPEN]");
        return String.format("%s ID:%-3d | %-20s | %-10s | %-8s | Due: %s | %s",
                status, id, truncate(title, 20), category,
                priority, dueDate != null ? dueDate.format(fmt) : "N/A",
                truncate(description, 30));
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
