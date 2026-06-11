package taskmanager.service;

import taskmanager.model.Task;
import taskmanager.repository.MemoryTaskRepository;
import taskmanager.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class responsible for managing tasks
 * <p>
 * The TaskManager owns the current task list and
 * uses TaskRepository to load and save the tasks
 */
public class TaskManager {
    private final List<Task> tasks;
    private final TaskRepository taskRepository;

    public TaskManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        tasks = taskRepository.getTasks();
    }

    /**
     * Returns a copy of the current task list
     * <p>
     * Modifications to the returned list do not affect the internal
     * task list managed by TaskManager
     *
     * @return a list containing all current tasks
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Creates a new Task
     *
     * @param taskDescription description of the task
     * @param taskDueDate     date the task is due
     */
    public void createTask(String taskDescription, LocalDateTime taskDueDate) {
        tasks.add(new Task(taskDescription, taskDueDate));
        saveTasks();
    }

    /**
     * Marks the task relating to the ID provided as complete
     *
     * @param taskId the UUID of the task that you want to mark as completed
     * @return returns true if a task can be found and marked as completed, otherwise false
     */
    public boolean markTaskComplete(UUID taskId) {
        Task task = findTask(taskId);
        if (task == null) {
            return false;
        }

        task.setComplete(true);
        saveTasks();
        return true;
    }

    /**
     * Reopens the task with the given UUID
     *
     * @param taskId the UUID of the task that you want to reopen
     * @return returns true if the task can be found and reopened, otherwise false.
     */
    public boolean reopenTask(UUID taskId) {
        Task task = findTask(taskId);
        if (task == null) {
            return false;
        }

        task.setComplete(false);
        saveTasks();
        return true;
    }

    /**
     * Removes a task with a valid id from the list
     *
     * @param taskId UUID of the task that should be removed
     * @return returns true if task is found and can be removed, otherwise false.
     */
    public boolean deleteTask(UUID taskId) {
        Task task = findTask(taskId);
        if (task == null) {
            return false;
        }

        tasks.remove(task);
        saveTasks();
        return true;
    }

    private void saveTasks() {
        taskRepository.saveTasks(tasks);
    }

    private Task findTask(UUID taskId) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

}
