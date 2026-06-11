package taskmanager.repository;

import taskmanager.model.Task;

import java.util.List;

public class FileTaskRepository implements TaskRepository {
    @Override
    public List<Task> getTasks() {
        return List.of();
    }

    @Override
    public void saveTasks(List<Task> tasks) {

    }
}
