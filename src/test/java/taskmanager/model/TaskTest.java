package taskmanager.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    //Task Creation
    @Test
    void taskCreationStoresValidDescriptionAndDueDate() {
        String taskDescription = "Task description";
        LocalDateTime dueDate = LocalDateTime.of(2030,1,1,12,0);
        Task task = new Task(taskDescription, dueDate);

        assertEquals(taskDescription, task.getDescription());
        assertEquals(dueDate, task.getDueDate());
    }

    @Test
    void taskCreationRejectsBlankDescriptions() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> new Task("", dueDate));
        assertThrows(IllegalArgumentException.class, () -> new Task(" ", dueDate));
        assertThrows(IllegalArgumentException.class, () -> new Task("  ", dueDate));
        assertThrows(IllegalArgumentException.class, () -> new Task("\t", dueDate));
    }

    @Test
    void taskCreationRejectsNullDescriptions() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> new Task(null, dueDate));
    }

    @Test
    void taskCreationRejectsDescriptionOverMaxLength() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        String longString = "a".repeat(Task.MAX_DESCRIPTION_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new Task(longString, dueDate));
    }

    @Test
    void taskCreationRejectsNullDueDate() {
        LocalDateTime dueDate = null;
        assertThrows(IllegalArgumentException.class, () -> new Task("Test Description", dueDate));
    }

    @Test
    void taskCreationRejectsDueDateBeforeCreationDate() {
        LocalDateTime dueDate = LocalDateTime.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> new Task("Test Description", dueDate));
    }

    //Load Tasks creation
    @Test
    void loadTaskCreationStoresAllProvidedValues() {
        UUID id = UUID.randomUUID();
        String description = "Test Description";
        LocalDateTime creationDate = LocalDateTime.of(2026,1,1,12,0);
        LocalDateTime dueDate = LocalDateTime.of(2026,1,3,12,0);
        LocalDateTime completedDate = LocalDateTime.of(2026,1,2,12,0);

        Task task = new Task(id, description, creationDate, dueDate, completedDate);

        assertEquals(id, task.getId());
        assertEquals(description, task.getDescription());
        assertEquals(creationDate, task.getCreationDate());
        assertEquals(dueDate, task.getDueDate());
        assertEquals(completedDate, task.getCompletedDate());
        assertTrue(task.isCompleted());
    }
    @Test
    void loadTaskCreationRejectsNullID() {
        UUID id = null;
        String description = "Test Description";
        LocalDateTime creationDate = LocalDateTime.of(2026,1,1,12,0);
        LocalDateTime dueDate = LocalDateTime.of(2026,1,3,12,0);
        LocalDateTime completedDate = LocalDateTime.of(2026,1,2,12,0);

        assertThrows(IllegalArgumentException.class, () -> new Task(id, description, creationDate, dueDate, completedDate));
    }

    @Test
    void loadTaskCreationRejectsNullCreationDate() {
        UUID id = UUID.randomUUID();
        String description = "Test Description";
        LocalDateTime creationDate = null;
        LocalDateTime dueDate = LocalDateTime.of(2026,1,3,12,0);
        LocalDateTime completedDate = LocalDateTime.of(2026,1,2,12,0);

        assertThrows(IllegalArgumentException.class, () -> new Task(id, description, creationDate, dueDate, completedDate));
    }


    //setDescription Tests
    @Test
    void descriptionUpdateValidDescription() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        String newDescription = "Test New Descripiton";
        Task task = new Task("Test Description", dueDate);

        task.setDescription(newDescription);
        assertEquals(newDescription, task.getDescription());
    }

    @Test
    void descriptionUpdateRejectNullDescription() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        String newDescription = null;
        Task task = new Task("Test Description", dueDate);

        assertThrows(IllegalArgumentException.class, () -> task.setDescription(newDescription));
    }

    @Test
    void descriptionUpdateRejectBlankDescriptions() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        Task task = new Task("Test Description", dueDate);

        assertThrows(IllegalArgumentException.class, () -> task.setDescription(""));
        assertThrows(IllegalArgumentException.class, () -> task.setDescription(" "));
        assertThrows(IllegalArgumentException.class, () -> task.setDescription("  "));
        assertThrows(IllegalArgumentException.class, () -> task.setDescription("\t"));
    }

    //setDueDate tests
    @Test
    void dueDateUpdateValidDate() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(2);
        Task task = new Task("Test Description", dueDate);

        task.setDueDate(newDueDate);
        assertEquals(newDueDate, task.getDueDate());
    }

    @Test
    void dueDateUpdateRejectNull() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        LocalDateTime newDueDate = null;
        Task task = new Task("Test Description", dueDate);

        assertThrows(IllegalArgumentException.class, () -> task.setDueDate(newDueDate));
    }

    @Test
    void dueDateUpdateRejectsDateBeforeCreationDate() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        LocalDateTime newDueDate = LocalDateTime.now().minusDays(1);
        Task task = new Task("Test Description", dueDate);

        assertThrows(IllegalArgumentException.class, () -> task.setDueDate(newDueDate));
    }

    //completion tests
    @Test
    void completeTaskUpdateUpdatesCompletionDate() {
        Task task = new Task("Test Description", LocalDateTime.now().plusDays(1));

        LocalDateTime startDateTime = LocalDateTime.now().minusMinutes(1);
        task.complete();
        assertTrue(
                task.getCompletedDate().isAfter(startDateTime) &&
                        task.getCompletedDate().isBefore(LocalDateTime.now().plusMinutes(1))
        );
    }

    @Test
    void completeTaskUpdateClearsCompletionDate() {
        Task task  = new Task(
                UUID.randomUUID(),
                "Test Description",
                LocalDateTime.of(2025,1,1,22,0),
                LocalDateTime.of(2025,1,2,22,0),
                LocalDateTime.of(2025,1,2,21,0));

        task.reopen();
        assertNull(task.getCompletedDate());
    }


}
