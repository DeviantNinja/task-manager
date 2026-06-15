package taskmanager.repository;

import taskmanager.model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileTaskRepository implements TaskRepository {
    private final Path path;

    public FileTaskRepository(Path taskPath) {
        this.path = taskPath;
        checkIfFileExists();
    }

    private void checkIfFileExists() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            if(Files.readAllLines(path).isEmpty()) {
                return tasks;
            }

            for (String task : Files.readAllLines(path)) {
                String[] taskString = task.split("\\|");
                UUID taskUUID = UUID.fromString(taskString[0]);
                String taskDescription = taskString[1];
                LocalDateTime creationDate = LocalDateTime.parse(taskString[2]);
                LocalDateTime dueDate = taskString[3].equals("null") ? null : LocalDateTime.parse(taskString[3]);
                LocalDateTime completedDate = taskString[4].equals("null") ? null : LocalDateTime.parse(taskString[4]);
                tasks.add(new Task(taskUUID, taskDescription, creationDate, dueDate, completedDate));
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        return tasks;
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        List<String> stringifiedTaskList = new ArrayList<>();
        for (Task task : tasks) {
            stringifiedTaskList.add(String.format("%s|%s|%s|%s|%s",
                    task.getId(),
                    task.getDescription(),
                    task.getCreationDate(),
                    task.getDueDate(),
                    task.getCompletedDate()));
        }

        try {
            Files.write(path, stringifiedTaskList);
        } catch ( IOException e) {
            System.out.println("Error saving tasks to file");
        }
    }
}
