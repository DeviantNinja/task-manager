package taskmanager.repository;

import taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class FakeTaskRepository implements TaskRepository {

    public boolean saveCalled = false;
    @Override
    public List<Task> getTasks() {
        return new ArrayList<Task>();
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        saveCalled = true;
    }
}
