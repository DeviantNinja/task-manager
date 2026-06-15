package main.java.taskmanager.UI.CLI;

import main.java.taskmanager.model.Task;
import main.java.taskmanager.model.TaskSortOrder;
import main.java.taskmanager.service.TaskManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI2 {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm");
    private static final String VIEW_TASK_FORMAT = "%-5s %-40s %-20s %-10s%n";
    private final TaskManager taskManager;
    private final Scanner scanner = new Scanner(System.in);
    private final String MENU_QUESTION = "Select Choice: ";
    private TaskSortOrder defaultSortOrder;

    public ConsoleUI2(TaskManager taskManager, TaskSortOrder sortOrder) {
        this.taskManager = taskManager;
        this.defaultSortOrder = sortOrder;
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            running = handleMainMenuAction(getChoice(6, MENU_QUESTION));
        }
    }


    // main menu actions
    private void viewTasks() {
        boolean viewingTasks = true;
        displayTasks(applySort(taskManager.getTasks()));
        while (viewingTasks) {
            displayViewTasksMenu();
            viewingTasks = handleViewTasksMenuAction(getChoice(5, MENU_QUESTION));
        }

    }

    private void createTask() {
        taskManager.createTask(captureDescription(), captureDateTime());
    }

    private void markTaskComplete(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No Incomplete Tasks Found!");
            return;
        }
        taskManager.markTaskComplete(selectTask(tasks).getId());
    }

    private void markTaskIncomplete(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No Complete Tasks Found!");
            return;
        }
        taskManager.reopenTask(selectTask(tasks).getId());
    }

    private void updateTask() {
        boolean editingTask = true;
        Task task = selectTask(applySort(taskManager.getTasks()));

        while (editingTask) {
            if (!taskManager.taskExists(task.getId())) {
                System.out.println("Selected task no longer exists!");
                task = selectTask(applySort(taskManager.getTasks()));
            }

            System.out.println("Currently Selected Task...");
            displayTask(task);
            displayUpdateTaskMenu();

            int choice = getChoice(5, MENU_QUESTION);
            if (choice == 4) {
                task = selectTask(applySort(taskManager.getTasks()));
            } else {
                editingTask = handleUpdateTaskMenuAction(choice, task);
            }

        }
    }

    ;

    //view tasks menu actions
    private void sortOrderMenu() {
        displaySortOrderMenu();
        handleSortOrderMenuAction(getChoice(5, "Choose how Tasks should be Sorted: "));
    }

    //edit task menu actions
    private void editTaskDescription(Task task) {
        task.setDescription(captureDescription());
        taskManager.editTaskDescription(task.getId(), task.getDescription());
    }

    private void editTaskDueDate(Task task) {
        task.setDueDate(captureDateTime());
        taskManager.editTaskDueDate(task.getId(), task.getDueDate());
    }

    private void deleteTask(Task task) {
        taskManager.deleteTask(task.getId());
    }

    //helper methods
    private int getChoice(int NoOfChoices, String choiceText) {
        while (true) {
            try {
                System.out.print(choiceText);
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice >= 1 && choice <= NoOfChoices) {
                    System.out.println();
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Menu Entry!");
            }
        }
    }

    private Task selectTask(List<Task> tasks) {
        displayTasks(tasks);
        return tasks.get(getChoice(tasks.size(), "Select Task: ") - 1);
    }

    private LocalDate captureDate() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true) {
            System.out.print("Enter Date (dd/mm/yyyy): ");
            String dateString = scanner.nextLine().trim();

            try {
                LocalDate date = LocalDate.parse(dateString, dateFormat);

                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Date cannot be in the past");
                } else {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Date Format! Use dd/mm/yyyy!");
            }
        }
    }

    private LocalTime captureTime() {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        while (true) {
            System.out.print("Enter 24 hour Time(hh:mm): ");
            String timeString = scanner.nextLine().trim();
            try {
                return LocalTime.parse(timeString, timeFormat);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Time Format! Use hh:mm!");
            }
        }
    }

    private LocalDateTime captureDateTime() {
        LocalDate date = captureDate();
        LocalTime time = captureTime();
        while (true) {
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            if (dateTime.isAfter(LocalDateTime.now())) {
                return dateTime;
            }
            System.out.println("cannot be in the past");
            time = captureTime();
        }
    }

    private String captureDescription() {
        System.out.print("Enter Task Description: ");
        return scanner.nextLine().trim();
    }

    private List<Task> applySort(List<Task> tasks) {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        switch (defaultSortOrder) {
            case CREATION_DATE -> sortedTasks.sort(Comparator.comparing(Task::getCreationDate));
            case DUE_DATE -> sortedTasks.sort(Comparator.comparing(Task::getDueDate));
            case COMPLETION_DATE -> sortedTasks.sort(
                    Comparator.comparing(
                            Task::getCompletedDate,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    ).thenComparing(Task::getDueDate)
            );
            case TASK_STATUS -> sortedTasks.sort(
                    Comparator.comparing(
                            Task::getCompletedDate,
                            Comparator.nullsFirst(Comparator.naturalOrder())
                    ).thenComparing(Task::getDueDate)
            );
            case DESCRIPTION -> sortedTasks.sort(Comparator.comparing(Task::getDescription));
        }
        return sortedTasks;
    }

    private List<Task> getTasksByCompletion(boolean isComplete) {
        List<Task> tasks = taskManager.getTasks();
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted() == isComplete) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    private void displayTasks(List<Task> tasks) {
        int taskNumber = 1;
        System.out.printf(
                VIEW_TASK_FORMAT,
                "No",
                "Description",
                "Due Date",
                "Completed");
        for (Task task : tasks) {
            System.out.printf(
                    VIEW_TASK_FORMAT,
                    taskNumber++,
                    task.getDescription(),
                    task.getDueDate().format(DATE_TIME_FORMAT),
                    task.isCompleted() ? task.getCompletedDate().format(DATE_TIME_FORMAT) : "Not Complete");
        }
        System.out.println();
    }

    private void displayTask(Task task) {
        System.out.printf(
                VIEW_TASK_FORMAT,
                "No",
                "Description",
                "Due Date",
                "Completed");
        System.out.printf(
                VIEW_TASK_FORMAT,
                "1",
                task.getDescription(),
                task.getDueDate().format(DATE_TIME_FORMAT),
                task.isCompleted() ? task.getCompletedDate().format(DATE_TIME_FORMAT) : "Not Complete");
    }

    //Menu methods
    private void displayMainMenu() {
        System.out.println("Main Menu...");
        System.out.println("1. View all tasks");
        System.out.println("2. Create a Task");
        System.out.println("3. Mark Task as Complete");
        System.out.println("4. Mark Task as Incomplete");
        System.out.println("5. Update Task");
        System.out.println("6. Exit");
        System.out.println();
    }

    private boolean handleMainMenuAction(int menuChoice) {
        switch (menuChoice) {
            case 1 -> viewTasks();
            case 2 -> createTask();
            case 3 -> markTaskComplete(getTasksByCompletion(false));
            case 4 -> markTaskIncomplete(getTasksByCompletion(true));
            case 5 -> updateTask();
            case 6 -> {
                return false;
            }
            default -> System.out.println("Invalid option");
        }
        return true;
    }

    private void displayViewTasksMenu() {
        System.out.println("View Tasks Menu...");
        System.out.println("1. View all tasks");
        System.out.println("2. View Completed Tasks");
        System.out.println("3. View Incomplete Tasks");
        System.out.println("4. Change Sort Method");
        System.out.println("5. Main Menu");
    }

    private boolean handleViewTasksMenuAction(int menuChoice) {
        switch (menuChoice) {
            case 1 -> displayTasks(applySort(taskManager.getTasks()));
            case 2 -> displayTasks(applySort(getTasksByCompletion(true)));
            case 3 -> displayTasks(applySort(getTasksByCompletion(false)));
            case 4 -> sortOrderMenu();
            case 5 -> {
                return false;
            }
            default -> System.out.println("Invalid Menu Entry!");
        }
        return true;
    }

    private void displaySortOrderMenu() {
        System.out.println("Sort Order Menu...");
        System.out.println("1. Sort by Creation Date");
        System.out.println("2. Sort by Due Date");
        System.out.println("3. Sort by Description");
        System.out.println("4. Sort by Completion Date");
        System.out.println("5. Sort by Status");
    }

    private void handleSortOrderMenuAction(int menuChoice) {
        switch (menuChoice) {
            case 1 -> defaultSortOrder = TaskSortOrder.CREATION_DATE;
            case 2 -> defaultSortOrder = TaskSortOrder.DUE_DATE;
            case 3 -> defaultSortOrder = TaskSortOrder.DESCRIPTION;
            case 4 -> defaultSortOrder = TaskSortOrder.COMPLETION_DATE;
            case 5 -> defaultSortOrder = TaskSortOrder.TASK_STATUS;
        }
    }

    private void displayUpdateTaskMenu() {
        System.out.println("Update Task Menu...");
        System.out.println("1. Edit task description");
        System.out.println("2. Edit task due date");
        System.out.println("3. Delete task");
        System.out.println("4. Change Task");
        System.out.println("5. Main Menu");
        System.out.println("");
    }

    private boolean handleUpdateTaskMenuAction(int menuChoice, Task task) {
        switch (menuChoice) {
            case 1 -> editTaskDescription(task);
            case 2 -> editTaskDueDate(task);
            case 3 -> deleteTask(task);
            case 5 -> {
                return false;
            }
            default -> System.out.println("Invalid option");
        }
        return true;
    }
}
