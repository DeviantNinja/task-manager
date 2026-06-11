package taskmanager;

import taskmanager.UI.CLI.ConsoleUI;
import taskmanager.repository.MemoryTaskRepository;
import taskmanager.repository.TaskRepository;
import taskmanager.service.TaskManager;

/**
 * Entry point for Task Manager Application
 */
public class Main {
    public static void main(String[] args) {
        TaskRepository repository = new MemoryTaskRepository();
        TaskManager taskManager = new TaskManager(repository);
        ConsoleUI ui = new ConsoleUI(taskManager);
        ui.start();

    }
}