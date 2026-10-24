package com.taskforge.service;

import com.taskforge.model.Task;
import com.taskforge.storage.TaskRepository;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository repo;
    private final Deque<List<Task>> history = new ArrayDeque<>();
    private static final int MAX_HISTORY = 10;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    private void snapshot() {
        history.push(new ArrayList<>(repo.findAll()));
        if (history.size() > MAX_HISTORY) {
            history.removeLast();
        }
    }

    public void undo() {
        if (history.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        List<Task> previous = history.pop();
        repo.replaceAll(previous);
        System.out.println("Undo successful.");
    }

    public Task create(String title, String desc, String category,
                       Task.Priority priority, LocalDate due) {
        snapshot();
        Task t = new Task(repo.getNextId(), title, desc, category, priority, due);
        repo.add(t);
        return t;
    }

    public boolean update(int id, String title, String desc, String category,
                          Task.Priority priority, LocalDate due) {
        Optional<Task> opt = repo.findById(id);
        if (!opt.isPresent()) return false;
        snapshot();
        Task t = opt.get();
        if (title != null) t.setTitle(title);
        if (desc != null) t.setDescription(desc);
        if (category != null) t.setCategory(category);
        if (priority != null) t.setPriority(priority);
        if (due != null) t.setDueDate(due);
        return repo.update(t);
    }

    public boolean complete(int id) {
        Optional<Task> opt = repo.findById(id);
        if (!opt.isPresent()) return false;
        snapshot();
        Task t = opt.get();
        t.setCompleted(true);
        return repo.update(t);
    }

    public boolean delete(int id) {
        snapshot();
        return repo.delete(id);
    }

    public List<Task> getAll() {
        return repo.findAll();
    }

    public List<Task> search(String keyword) {
        String k = keyword.toLowerCase();
        return repo.findAll().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(k)
                        || t.getDescription().toLowerCase().contains(k)
                        || t.getCategory().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public List<Task> filterByStatus(boolean completed) {
        return repo.findAll().stream()
                .filter(t -> t.isCompleted() == completed)
                .collect(Collectors.toList());
    }

    public List<Task> filterByPriority(Task.Priority p) {
        return repo.findAll().stream()
                .filter(t -> t.getPriority() == p)
                .collect(Collectors.toList());
    }

    public List<Task> getOverdue() {
        return repo.findAll().stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }
}
