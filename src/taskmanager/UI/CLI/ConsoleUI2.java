package taskmanager.UI.CLI;

import taskmanager.model.Task;
import taskmanager.model.TaskSortOrder;
import taskmanager.service.TaskManager;

import java.time.format.DateTimeFormatter;
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

    //Menu
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
            case 2 -> System.out.println("create a Task");
            case 3 -> System.out.println("Mark Task as Complete");
            case 4 -> System.out.println("Mark Task as Incomplete");
            case 5 -> System.out.println("Update Task");
            case 6 -> {
                return false;
            }
            default -> System.out.println("Invalid option");
        }
        return true;
    }


    private int getChoice(int NoOfChoices, String choiceText) {
        while (true) {
            try {
                System.out.print(choiceText);
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice >= 1 || choice <= NoOfChoices) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Menu Entry!");
            }
        }
    }

    //View Tasks

    private void displayTasks(List<Task> tasks) {
        int taskNumber = 1;
        System.out.printf(
                VIEW_TASK_FORMAT,
                "No",
                "Description",
                "Due Date",
                "status");
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

    private void viewTasks() {
        boolean viewingTasks = true;
        displayTasks(applySort(taskManager.getTasks()));
        while (viewingTasks) {
            viewTasksMenu();
            viewingTasks = handleViewTasksMenu(getChoice(5, MENU_QUESTION));
        }

    }

    private void viewTasksMenu() {
        System.out.println("View Tasks Menu...");
        System.out.println("1. View all tasks");
        System.out.println("2. View Completed Tasks");
        System.out.println("3. View Incomplete Tasks");
        System.out.println("4. Change Sort Method");
        System.out.println("5. Main Menu");
    }

    private boolean handleViewTasksMenu(int menuChoice) {
        switch (menuChoice) {
            case 1 -> displayTasks(applySort(taskManager.getTasks()));
            case 2 -> displayTasks(applySort(getTasksByCompletion(true)));
            case 3 -> displayTasks(applySort(getTasksByCompletion(false)));
            case 4 -> System.out.println("Change Sort Method");
            case 5 -> {return false;}
            default -> System.out.println("Invalid Menu Entry!");
        }
        return true;
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

    private List<Task> applySort(List<Task> tasks) {
        List<Task> sortedTasks = tasks;
        switch(defaultSortOrder) {
            case CREATION_DATE -> sortedTasks.sort(Comparator.comparing(Task::getCreationDate));
            case DUE_DATE -> sortedTasks.sort(Comparator.comparing(Task::getDueDate));
            case COMPLETION_DATE -> sortedTasks.sort(
                    Comparator.comparing(
                        Task::getCompletedDate,
                        Comparator.nullsLast(Comparator.naturalOrder())
                    ).thenComparing(Task::getDueDate)
            );
            case COMPLETION_ORDER -> sortedTasks.sort(
                    Comparator.comparing(
                        Task::getCompletedDate,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                    ).thenComparing(Task::getDueDate)
            );
            case DESCRIPTION ->  sortedTasks.sort(Comparator.comparing(Task::getDescription));
        }
        return sortedTasks;
    }

}
//    private void displayUpdateTaskMenu() {
//        System.out.println("Update Task Menu...");
//        System.out.println("1. Edit task description");
//        System.out.println("2. Edit task due date");
//        System.out.println("3. Delete task");
//        System.out.println("4. Main Menu");
//        System.out.println("");
//    }
//
//    private boolean taskMenuAction(int menuChoice) {
//        switch (menuChoice) {
//            case 1 -> markTaskComplete();
//            case 2 -> reopenTask();
//            case 3 -> updateTaskDescription();
//            case 4 -> updateTaskDueDate();
//            case 5 -> deleteTask();
//            case 6 -> {return false;}
//            default -> System.out.println("Invalid option");
//        }
//        return true;
//    }
