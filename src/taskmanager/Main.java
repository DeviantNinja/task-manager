package taskmanager;

import taskmanager.UI.CLI.ConsoleUI;
import taskmanager.service.TaskManager;

/**
 * Entry point for Task Manager Application
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Task Manager Started!");
        TaskManager taskManager = new TaskManager();
        ConsoleUI ui = new ConsoleUI(taskManager);
        ui.start();

    }
}