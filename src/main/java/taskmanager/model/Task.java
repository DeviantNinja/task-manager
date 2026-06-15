package main.java.taskmanager.model;

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
    private final UUID id;
    private String description;
    private final LocalDateTime creationDate;
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
        this.id = id;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.completedDate = completedDate;
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
        this.description = description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Updates the task completion state.
     * <p>
     * completing the task records the current date and time.
     * Marking it incomplete clears the completed data.
     *
     * @param complete true to mark the task complete, false to mark it as incomplete.
     */
    public void setComplete(boolean complete) {
        if (complete) {
            completedDate = LocalDateTime.now();
        } else {
            completedDate = null;
        }
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "\nDescription: " + description +
                "\nCreation Date: " + creationDate +
                "\nDue Date: " + dueDate +
                "\nCompleted Date: " + completedDate;
    }
}
