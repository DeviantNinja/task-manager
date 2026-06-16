package taskmanager.repository;

import taskmanager.model.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Temporary in-memory  implmention of TaskRepository
 * Used during development before persistenceis implmented.
 */
public class MemoryTaskRepository implements TaskRepository {
    private List<Task> tasks;

    public MemoryTaskRepository() {
        loadDemoTasks();
    }

    private void loadDemoTasks() {
        tasks = new ArrayList<>();
        tasks.add(new Task("Demo task 1", LocalDateTime.of(2026,6,15,14,30)));
        tasks.add(new Task("Demo task 2", LocalDateTime.of(2026,6,20,9,30)));
        tasks.add(new Task("Demo task 3", LocalDateTime.of(2026,7,20,9,30)));
    }

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        this.tasks = tasks;

    }
}
