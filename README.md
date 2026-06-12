# Task Manager

A command-line task management application written in Java.

## Purpose

This project is being developed incrementally, starting as a simple CLI application and later evolving into a more complete task management system.

## Planned Features
v1.0
- [x] Create tasks
- [x] List tasks
- [x] Mark tasks complete
- [x] Delete tasks
- [x] Save tasks to file
- [x] Load tasks from file

v1.1
- [ ] Edit Task
    - [ ] Edit Description
    - [ ] Edit Due Date
- [ ] Add Task Sorting
    - [ ] Sort by Due Date
    - [ ] Sort by Creation Date
- [ ] Add Task Filtering
    - [ ] Show All Tasks
    - [ ] Show Completed Tasks
    - [ ] Show Active Tasks
- [ ] Improve Input Validation

v1.2
- [ ] Convert Project to Maven
- [ ] Add JUnit 5
- [ ] Write TaskManager Tests
- [ ] Write Repository Tests
- [ ] Add Jackson
- [ ] Convert Persistence from Text to JSON

v1.3
- [ ] Refactor after testing
- [ ] Improve exception handling
- [ ] Improve persistence reliability
- [ ] Code cleanup

v2.0 - Spring Boot API
- Convert app to Spring Boot
- Add REST endpoints
- Keep service/repository structure
- Use JSON request/response

v2.1 - Database persistence
- Add database
- Create DbTaskRepository
- Replace JSON/file repository
- Add repository tests

v2.3 - Hosted application
- Deploy API online
- Use cheap/free hosting
- Connect hosted app to database
- Make it accessible from anywhere

v3.0 
- User registration
- User login
- Password hashing
- Authentication
- User-specific tasks
- Basic roles/admin
## Technologies

- Java
- Git
- GitHub