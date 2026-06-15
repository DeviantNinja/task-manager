package taskmanager.UI.CLI;

import taskmanager.model.Task;
import taskmanager.service.TaskManager;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * CLI version of the user interface
 * <p>
 * User interface to create, manage and remove tasks
 */
public class ConsoleUI {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm");
    private final TaskManager taskManager;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() {
        boolean running = true;

        while (running) {
            mainMenu();

            int choice = getMenuChoice(7);

            switch (choice) {
                case 1 -> displayTasks();
                case 2 -> createTask();
                case 3 -> updateTaskMenu();
                case 4 -> markTaskComplete();
                case 5 -> reopenTask();
                case 6 -> deleteTask();
                case 7 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        System.out.println("Session Closed!");
    }

    //menus
    private void mainMenu() {
        System.out.println("1. View all tasks");
        System.out.println("2. Create Task");
        System.out.println("3. Update Task");
        System.out.println("4. Mark Task as Complete");
        System.out.println("5. Reopen Task");
        System.out.println("6. Delete Task");
        System.out.println("7. Exit");
    }
    private void updateTaskMenu() {
        if(taskManager.getTasks().isEmpty()) {
            System.out.println("There are no tasks to update!");
            start();
        }

        displayTasks();
        System.out.println("Choose the task you want to update...");
        getMenuChoice(taskManager.getTasks().size());
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

    private void updateTask() {

    }


    private void deleteTask() {
        if(taskManager.getTasks().isEmpty()) {
            System.out.println("No Tasks Found!");
            return;
        }
        UUID taskId;
        System.out.println("Delete Task...");
        displayTasks();
        taskId = getUUID(getMenuChoice(taskManager.getTasks().size()));
        taskManager.deleteTask(taskId);

        System.out.println("Task Removed Successfully!\n");
    }

    private void markTaskComplete() {
        if(taskManager.getTasks().isEmpty()) {
            System.out.println("No Tasks Found!");
            return;
        }
        UUID taskId;
        System.out.println("Mark Task as Complete...");
        displayTasks();
        taskId = getUUID(getMenuChoice(taskManager.getTasks().size()));
        taskManager.markTaskComplete(taskId);

        System.out.println("Task Completed Successfully!\n");
    }

    private void reopenTask() {
        if(taskManager.getTasks().isEmpty()) {
            System.out.println("No Tasks Found!");
            return;
        }

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
            try {
                System.out.print("Select option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice >= 1 && choice <= totalChoices) {
                    return choice;
                }

                System.out.println("Invalid Menu Entry!");
            } catch (NumberFormatException e) {
                System.out.println("Must be a Number!");
            }
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

        while (true) {
            try {
                System.out.print("Enter Year: ");
                year = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Month: ");
                month = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Day: ");
                day = Integer.parseInt(scanner.nextLine());
                return LocalDate.of(year, month, day);

            } catch (NumberFormatException e) {
                System.out.println("Must be a valid number!");
            } catch (DateTimeException e) {
                System.out.println("Invalid Date Format!");
            }
        }
    }

    private LocalTime buildTime() {
        int hour;
        int minute;
        while (true) {
            try {
                System.out.print("Enter Hour: ");
                hour = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Minute: ");
                minute = Integer.parseInt(scanner.nextLine());
                return LocalTime.of(hour, minute);
            }  catch (NumberFormatException e) {
                System.out.println("Must be a valid number!");
            } catch (DateTimeException e) {
                System.out.println("Invalid Time Format!");
            }
        }
    }

}
