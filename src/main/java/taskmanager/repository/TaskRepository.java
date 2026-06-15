package main.java.taskmanager.repository;

import main.java.taskmanager.model.Task;

import java.util.List;

/**
 * Defines the operations required to store and retrieve tasks.
 *
 * Implementations may use different storage mechanisms,
 * such as files, database or in-memory collections.
 */
public interface TaskRepository {
    /**
     * Returns all tasks stored in the repository.
     * @return a list containing all stored tasks
     */
    List<Task> getTasks();

    /**
     * Saves all tasks to underlying storage mechanism
     */
    void saveTasks(List<Task> tasks);
}
