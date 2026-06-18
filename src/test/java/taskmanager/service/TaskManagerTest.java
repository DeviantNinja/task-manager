package taskmanager.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.model.Task;
import taskmanager.repository.FakeTaskRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private FakeTaskRepository fakeTaskRepository;
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        fakeTaskRepository = new FakeTaskRepository();
        taskManager = new TaskManager(fakeTaskRepository);
    }

    @Test
    void createTaskAddsNewTaskToTaskList() {
        String taskDescription = "new Task";
        LocalDateTime taskDueDate = LocalDateTime.of(2027,1,1,12,0);

        taskManager.createTask(taskDescription, taskDueDate);
        Task task = taskManager.getTasks().getFirst();

        assertEquals(taskDescription, task.getDescription());
        assertEquals(taskDueDate, task.getDueDate());
        assertEquals(1, taskManager.getTasks().size());
        assertTrue(fakeTaskRepository.saveCalled);
    }

    @Test
    void markTaskCompleteReturnFalseIfNotFound() {
        assertFalse(taskManager.markTaskComplete(UUID.randomUUID()));
        assertFalse(fakeTaskRepository.saveCalled);
    }

    @Test
    void markTaskCompleteReturnTrueIfTaskFound() {
        taskManager.createTask("Test Task", LocalDateTime.now().plusDays(1));
        Task task = taskManager.getTasks().getFirst();
        fakeTaskRepository.saveCalled = false;

        assertTrue(taskManager.markTaskComplete(task.getId()));
        assertTrue(task.isCompleted());
        assertTrue(fakeTaskRepository.saveCalled);
    }

    @Test
    void reopenTaskReturnFalseIfTaskNotFound() {
        assertFalse(taskManager.reopenTask(UUID.randomUUID()));
        assertFalse(fakeTaskRepository.saveCalled);
    }

    @Test
    void reopenTaskReturnTrueIfTaskFound() {
        taskManager.createTask("Test Task", LocalDateTime.now().plusDays(1));
        Task task = taskManager.getTasks().getFirst();
        task.complete();
        fakeTaskRepository.saveCalled = false;

        assertTrue(taskManager.reopenTask(task.getId()));
        assertFalse(task.isCompleted());
        assertTrue(fakeTaskRepository.saveCalled);
    }

    @Test
    void deleteTaskReturnFalseIfTaskNotFound() {
        assertFalse(taskManager.deleteTask(UUID.randomUUID()));
        assertFalse(fakeTaskRepository.saveCalled);
    }

    @Test
    void deleteTaskReturnTrueIfTaskFound() {
        taskManager.createTask("Test Task", LocalDateTime.now().plusDays(1));
        Task task = taskManager.getTasks().getFirst();
        fakeTaskRepository.saveCalled = false;

        assertTrue(taskManager.deleteTask(task.getId()));
        assertEquals(0, taskManager.getTasks().size());
        assertTrue(fakeTaskRepository.saveCalled);
    }

    @Test
    void editTaskDescriptionReturnFalseIfTaskNotFound() {
        assertFalse(taskManager.editTaskDescription(UUID.randomUUID(),"New Task Description"));
        assertFalse(fakeTaskRepository.saveCalled);
    }

    @Test
    void editTaskDescriptionReturnTrueIfTaskFound() {
        taskManager.createTask("Test Task", LocalDateTime.now().plusDays(1));
        Task task = taskManager.getTasks().getFirst();
        String newTaskDescription = "New Test Task Description";
        fakeTaskRepository.saveCalled = false;

        assertTrue(taskManager.editTaskDescription(task.getId(), newTaskDescription));
        assertEquals(newTaskDescription, task.getDescription());
        assertTrue(fakeTaskRepository.saveCalled);
    }

    @Test
    void editTaskDueDateReturnFalseIfTaskNotFound() {
        assertFalse(taskManager.editTaskDueDate(UUID.randomUUID(),LocalDateTime.now().plusDays(2)));
        assertFalse(fakeTaskRepository.saveCalled);
    }

    @Test
    void editTaskDueDateReturnTrueIfTaskFound() {
        taskManager.createTask("Test Task", LocalDateTime.now().plusDays(1));
        Task task = taskManager.getTasks().getFirst();
        LocalDateTime newTaskDueDate = LocalDateTime.of(2027,1,1,12,0);
        fakeTaskRepository.saveCalled = false;

        assertTrue(taskManager.editTaskDueDate(task.getId(), newTaskDueDate));
        assertEquals(newTaskDueDate, task.getDueDate());
        assertTrue(fakeTaskRepository.saveCalled);
    }

    @Test
    void taskExistsReturnFalseIfTaskNotFound() {
        assertFalse(taskManager.taskExists(UUID.randomUUID()));
    }

    @Test
    void taskExistsReturnTrueIfTaskFound() {
        taskManager.createTask("Test Task", LocalDateTime.now().plusDays(1));
        Task task = taskManager.getTasks().getFirst();

        assertTrue(taskManager.taskExists(task.getId()));
    }


}
