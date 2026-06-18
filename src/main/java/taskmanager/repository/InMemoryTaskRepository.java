package taskmanager.repository;

import taskmanager.model.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Temporary in-memory  implmention of TaskRepository
 * Used during development before persistenceis implmented.
 */
public class InMemoryTaskRepository implements TaskRepository {
    private List<Task> tasks;

    public InMemoryTaskRepository() {
        tasks = new ArrayList<>();
    }

    public static InMemoryTaskRepository withDemoData() {
        InMemoryTaskRepository repoWithDemoData = new InMemoryTaskRepository();
        repoWithDemoData.loadDemoTasks();
        return repoWithDemoData;
    }

    private void loadDemoTasks() {
        tasks.add(new Task("Demo task 1", LocalDateTime.now().plusDays(1)));
        tasks.add(new Task("Demo task 2", LocalDateTime.now().plusDays(2)));
        tasks.add(new Task("Demo task 3", LocalDateTime.now().plusDays(3)));
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
