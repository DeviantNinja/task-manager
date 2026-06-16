package taskmanager.model;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/***
 * Represents a Task managed by the application
 *
 * A Task contains a description, creation date,
 * due date and completion date, a task is considered
 * incomplete until completionDate has a date
 */
public class Task {
    public static final int MAX_DESCRIPTION_LENGTH = 100;
    private final UUID id;
    private final LocalDateTime creationDate;
    private String description;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;

    /***
     * Creates a new incomplete task
     * @param description the task description
     * @param dueDate the date and time the task is due
     */
    public Task(String description, LocalDateTime dueDate) {
        this(UUID.randomUUID(), description, LocalDateTime.now(), dueDate, null);
    }

    /***
     * Recreates a task from existing stored Data
     * @param id the unique task ID
     * @param description the task description
     * @param creationDate the date adn time the task was created
     * @param dueDate the date and time the task is due
     * @param completedDate the data and time the task was completed
     */
    public Task(UUID id, String description, LocalDateTime creationDate, LocalDateTime dueDate, LocalDateTime completedDate) {
        if(id == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }

        if(creationDate == null) {
            throw new IllegalArgumentException("Task Creation Date cannot be null");
        }

        this.id = id;
        this.description = normaliseDescription(description);
        this.creationDate = creationDate;
        this.dueDate = verifyAndReturnDueDate(dueDate, creationDate);
        this.completedDate = verifyAndReturnCompletedDate(completedDate, creationDate);
    }

    private static String normaliseDescription(String description) {
        if(description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }

        String trimmedDescription = description.trim();

        if(trimmedDescription.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }

        if(trimmedDescription.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description is too long");
        }

        return trimmedDescription;
    }
    private static LocalDateTime verifyAndReturnDueDate(LocalDateTime dueDate, LocalDateTime creationDate) {
        if(dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }

        if(dueDate.isBefore(creationDate)) {
            throw new IllegalArgumentException("Due date cannot be before completed date");
        }

        return dueDate;
    }
    private static LocalDateTime verifyAndReturnCompletedDate(LocalDateTime completedDate, LocalDateTime creationDate) {
        if(completedDate != null && completedDate.isBefore(creationDate)) {
            throw new IllegalArgumentException("Completed date cannot be before completed date");
        }
        return completedDate;
    }

    //gets
    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    /***
     * checks whether the task has been completed
     * @return true if the task has been completed, otherwise false
     */
    public boolean isCompleted() {
        return completedDate != null;
    }

    //sets
    public void setDescription(String description) {
        this.description = normaliseDescription(description);
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = verifyAndReturnDueDate(dueDate, creationDate);
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nDescription: " + description +
                "\nCreation Date: " + creationDate +
                "\nDue Date: " + dueDate +
                "\nCompleted Date: " + completedDate;
    }

    /**
     * sets or changes current tasks complete date
     */
    public void complete() {
        completedDate = LocalDateTime.now();
    }

    /**
     * marks a task as incomplete by setting it to null
     */
    public void reopen() {
        completedDate = null;
    }
}
