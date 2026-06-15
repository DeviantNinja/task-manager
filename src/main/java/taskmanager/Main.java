package main.java.taskmanager;

import main.java.taskmanager.UI.CLI.ConsoleUI2;
import main.java.taskmanager.model.TaskSortOrder;
import main.java.taskmanager.repository.FileTaskRepository;
import main.java.taskmanager.repository.TaskRepository;
import main.java.taskmanager.service.TaskManager;

import java.nio.file.Paths;

/**
 * Entry point for Task Manager Application
 */
public class Main {
    public static void main(String[] args) {
        TaskRepository repository = new FileTaskRepository(Paths.get("c:\\Users\\d_joh\\Desktop\\tasks.txt"));
        TaskManager taskManager = new TaskManager(repository);
        ConsoleUI2 ui = new ConsoleUI2(taskManager, TaskSortOrder.COMPLETION_DATE);
        ui.start();
    }
}