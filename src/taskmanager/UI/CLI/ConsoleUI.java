package taskmanager.UI.CLI;

import taskmanager.model.Task;
import taskmanager.service.TaskManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {
    private final TaskManager taskManager;
    private final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm");
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMenu();

            int choice = getMenuChoice(6);

            switch (choice) {
                case 1 -> displayTasks();
                case 2 -> createTask();
                case 3 -> markTaskComplete();
                case 4 -> reopenTask();
                case 5 -> deleteTask();
                case 6 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        System.out.println("Session Closed!");
    }

    //menus
    private void displayMenu() {
        System.out.println("1. View all tasks");
        System.out.println("2. Create Task");
        System.out.println("3. Mark Task as Complete");
        System.out.println("4. Reopen Task");
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
        System.out.println();
    }

    private void createTask() {
        String description;
        LocalDateTime dueDate;

        System.out.println("Create new Task...");
        System.out.print("Task Description: ");
        description = scanner.nextLine();
        dueDate = dateTimeBuilder();

        taskManager.createTask(description, dueDate);
        System.out.println("Task Created!\n");

    }
    private void deleteTask() {
        UUID taskId;
        System.out.println("Delete Task...");
        displayTasks();
        taskId = getUUID(getMenuChoice(taskManager.getTasks().size()));
        taskManager.deleteTask(taskId);

        System.out.println("Task Removed Successfully!\n");
    }
    private void markTaskComplete() {
        UUID taskId;
        System.out.println("Mark Task as Complete...");
        displayTasks();
        taskId = getUUID(getMenuChoice(taskManager.getTasks().size()));
        taskManager.markTaskComplete(taskId);

        System.out.println("Task Completed Successfully!\n");
    }
    private void reopenTask() {
        UUID taskId;
        System.out.println("Reopen Task...");
        displayTasks();
        taskId = getUUID(getMenuChoice(taskManager.getTasks().size()));
        taskManager.reopenTask(taskId);

        System.out.println("Task Opened Successfully!\n");
    }

    //helpers
    private UUID getUUID(int taskNumber) {
        List<Task> tasks = taskManager.getTasks();
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.get(taskNumber - 1).getId();
    }
    private int getMenuChoice(int totalChoices) {
        while (true) {
            System.out.print("Select option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice >= 1 && choice <= totalChoices) {
                return choice;
            }
            System.out.println("Invalid option");
        }
    }
    private LocalDateTime dateTimeBuilder() {
        LocalDate date = buildDate();

        while (date.isBefore(LocalDate.now())) {
            System.out.println("Date cannot be in the past!");
            date = buildDate();
        }

        LocalTime time = buildTime();
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        while (dateTime.isBefore(LocalDateTime.now())) {
            System.out.println("Task cannot be in the past!");
            time = buildTime();
            dateTime = LocalDateTime.of(date, time);
        }

        return dateTime;
    }
    private LocalDate buildDate() {
        int year;
        int month;
        int day;

        System.out.print("Enter Year: ");
        year = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Month: ");
        month = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Day: ");
        day = Integer.parseInt(scanner.nextLine());
        return LocalDate.of(year, month, day);
    }
    private LocalTime buildTime() {
        int hour;
        int minute;
        System.out.print("Enter Hour: ");
        hour = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Minute: ");
        minute = Integer.parseInt(scanner.nextLine());
        return LocalTime.of(hour, minute);
    }
}
