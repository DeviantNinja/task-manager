package taskmanager.UI.CLI;

import taskmanager.model.Task;
import taskmanager.service.TaskManager;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {
    private TaskManager taskManager;
    private final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm");
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(TaskManager taskManager) {
        this.taskManager = taskManager;

    }

    private void start() {
        boolean running = true;

        while (running) {
            displayMenu();

            int choice = getMenuChoice();

            switch (choice) {
                case 1 -> displayTasks();
                case 2 -> createTask();
                case 3 -> markTaskComplete();
                case 4 -> updateTask();
                case 5 -> deleteTask();
                case 6 -> running = false;
                default ->  System.out.println("Invalid option");
            }
        }
        System.out.println("Session Closed!");
    }

    private int getMenuChoice() {
        while (true) {
            System.out.print("Select option: ");
            int choice = scanner.nextInt();

            if(choice >= 1 || choice <= 6) {
                return choice;
            }
            System.out.println("Invalid option");
        }
    }

    //menus
    private void displayMenu() {
        System.out.println("My Task Manager UI");
        System.out.println("----------------------------");
        System.out.println("1. View all tasks");
        System.out.println("2. Create Task");
        System.out.println("3. Mark Task as Complete");
        System.out.println("4. Update Task");
        System.out.println("5. Delete Task");
        System.out.println("6. Exit");
    }

    //actions
    private void displayTasks() {
        int taskCount = 1;

        System.out.printf(
                "%-5s %-25s %-20s %-10s%n",
                "No",
                "Description",
                "Due Date",
                "Status"
        );

        for (Task task : taskManager.getTasks()) {
            System.out.printf(
                    "%-5s %-25s %-20s %-10s%n",
                    taskCount++,
                    task.getDescription(),
                    task.getDueDate().format(DATE_TIME_FORMAT),
                    task.isCompleted() ? task.getCompletedDate().format(DATE_TIME_FORMAT) : "Not Complete"
            );
        }
    }
    //TODO: CreateTask
    private void createTask() {
    }
    //TODO: DeleteTask
    private void deleteTask() {
    }
    //TODO: Complete Task
    private void markTaskComplete() {
    }
    //TODO: Reopen Task
    private void reopenTask() {
    }
    //TODO: update Task
    private void updateTask() {}

    //helpers
    private UUID getUUID(int taskNumber) {
        List<Task> tasks = taskManager.getTasks();
        if(taskNumber < 1 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.get(taskNumber - 1).getId();
    }



}
