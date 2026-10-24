# TaskForge v1.0

**Zero-Dependency Advanced Command-Line Task Manager**

A complete portfolio project built with pure Java (JDK 8+).  
No external libraries. No frameworks. No Maven/Gradle. Just pure Java.

This project demonstrates solid software engineering fundamentals suitable for a first-year BSIT / Computer Science student portfolio.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Features](#2-features)
3. [Project Structure](#3-project-structure)
4. [Requirements](#4-requirements)
5. [How to Download / Clone](#5-how-to-download--clone)
6. [How to Compile](#6-how-to-compile)
7. [How to Run](#7-how-to-run)
8. [How to Use the Application](#8-how-to-use-the-application)
9. [Architecture & Design Decisions](#9-architecture--design-decisions)
10. [Data Persistence Explained](#10-data-persistence-explained)
11. [Key Concepts Demonstrated](#11-key-concepts-demonstrated)
12. [Possible Future Improvements](#12-possible-future-improvements)
13. [Troubleshooting](#13-troubleshooting)
14. [Author](#14-author)

---

## 1. Project Overview

**TaskForge** is a fully functional command-line task management system.  
Users can create, view, update, delete, search, and filter tasks. The application also provides basic analytics and an undo feature.

All data is saved to a local file (`data/tasks.dat`) so tasks survive after the program is closed.

The entire application is written in pure Java with **zero external dependencies**.

---

## 2. Features

- **Full CRUD**
  - Create new tasks
  - Read / list all tasks
  - Update existing tasks
  - Delete tasks

- **Task Properties**
  - Title
  - Description
  - Category
  - Priority (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`)
  - Due date
  - Completion status
  - Creation date
  - Completion date

- **Smart Features**
  - Automatic overdue detection
  - Search by keyword (title, description, or category)
  - Filter by status (Open / Completed)
  - Filter by priority
  - Filter overdue tasks only
  - Analytics dashboard (completion rate, priority distribution, top categories)
  - Undo last action (up to 10 levels of history)

- **Technical Highlights**
  - Custom text-based file persistence (no JSON, no XML libraries)
  - Clean layered architecture
  - Input validation
  - Proper error handling

---

## 3. Project Structure

```
TaskForge/
├── README.md
├── src/
│   └── com/
│       └── taskforge/
│           ├── Main.java                  ← Entry point + console menu
│           ├── model/
│           │   └── Task.java              ← Task entity + Priority enum
│           ├── storage/
│           │   └── TaskRepository.java    ← File save / load logic
│           ├── service/
│           │   ├── TaskService.java       ← Business logic + Undo
│           │   └── AnalyticsService.java  ← Statistics & reports
│           └── util/
│               ├── InputHelper.java       ← Safe user input methods
│               └── DateUtil.java          ← Date formatting helper
└── data/                                  ← Created automatically at runtime
    └── tasks.dat                          ← Task data file
```

---

## 4. Requirements

- **Java Development Kit (JDK) 8 or higher**
  - Recommended: JDK 11, 17, or 21
- A terminal / command prompt
  - Windows: PowerShell or Command Prompt
  - macOS / Linux: Terminal

### Check if Java is installed

Open your terminal and type:

```bash
java -version
javac -version
```

You should see version information. If you get an error, install JDK first.

**Download JDK:**
- https://adoptium.net/ (recommended)
- or https://www.oracle.com/java/technologies/downloads/

---

## 5. How to Download / Clone

### Option A – Download ZIP (easiest)

1. Go to the GitHub repository page
2. Click the green **Code** button
3. Click **Download ZIP**
4. Extract the ZIP file to any folder (example: `C:\Users\YourName\Downloads\TaskForge`)

### Option B – Clone with Git

```bash
git clone https://github.com/YOUR-USERNAME/TaskForge.git
cd TaskForge
```

---

## 6. How to Compile

Open a terminal **inside the TaskForge folder** (the folder that contains the `src` directory).

### Windows (PowerShell or CMD)

```powershell
javac -encoding UTF-8 -d out src\com\taskforge\model\*.java src\com\taskforge\storage\*.java src\com\taskforge\service\*.java src\com\taskforge\util\*.java src\com\taskforge\Main.java
```

### macOS / Linux

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

### What this command does

- `javac` → Java compiler
- `-encoding UTF-8` → Handles special characters correctly
- `-d out` → Puts compiled `.class` files into the `out` folder
- The rest → Lists all source files to compile

If compilation is successful, you will see **no error messages** and a new folder named `out` will appear.

---

## 7. How to Run

After successful compilation, run this command:

```bash
java -cp out com.taskforge.Main
```

### Explanation

- `java` → Runs the Java Virtual Machine
- `-cp out` → Tells Java to look for classes inside the `out` folder
- `com.taskforge.Main` → The fully qualified name of the main class

You should now see the application start with a menu.

---

## 8. How to Use the Application

When the program starts you will see:

```
+--------------------------------------+
|         TASKFORGE v1.0               |
|   Zero-Dependency Task Manager       |
+--------------------------------------+

--- MENU ---
1. Add Task
2. List All Tasks
3. Update Task
4. Mark Complete
5. Delete Task
6. Search
7. Filter
8. Analytics Dashboard
9. Undo Last Action
0. Exit
Choice:
```

### Menu Options Explained

| Option | What it does |
|--------|--------------|
| **1. Add Task** | Creates a new task. You will be asked for title, description, category, priority, and due date. |
| **2. List All Tasks** | Shows every task currently stored. |
| **3. Update Task** | Lets you change the details of an existing task by ID. Leave fields blank to keep the current value. |
| **4. Mark Complete** | Marks a task as finished. |
| **5. Delete Task** | Permanently removes a task. |
| **6. Search** | Searches title, description, and category for a keyword. |
| **7. Filter** | Filter by Open / Completed / Priority / Overdue. |
| **8. Analytics Dashboard** | Shows total tasks, completion percentage, priority distribution, and top categories. |
| **9. Undo Last Action** | Reverts the last change (add, update, delete, or complete). Up to 10 undos are supported. |
| **0. Exit** | Closes the program. All data is already saved. |

### Example Session

```
Choice: 1
Title: Finish portfolio project
Description: Write README and polish code
Category: School
Priority (LOW/MEDIUM/HIGH/CRITICAL): HIGH
Due date (yyyy-MM-dd or empty): 2026-10-30
Created: [OPEN] ID:1   | Finish portfolio... | School     | HIGH     | Due: 2026-10-30 | Write README and polish code
```

---

## 9. Architecture & Design Decisions

The project follows a simple layered architecture:

```
┌─────────────────────────────────────┐
│              Main.java              │  ← User Interface (Console)
├─────────────────────────────────────┤
│         Service Layer               │
│  TaskService + AnalyticsService     │  ← Business Logic
├─────────────────────────────────────┤
│         Storage Layer               │
│         TaskRepository              │  ← Data Access
├─────────────────────────────────────┤
│         Model Layer                 │
│            Task.java                │  ← Domain Entity
└─────────────────────────────────────┘
```

### Why this structure?

- **Separation of Concerns** — Each class has one clear responsibility.
- **Easier to maintain** — Changing how data is saved does not affect the menu logic.
- **Easier to test** — Business logic is separated from input/output.
- **Good portfolio practice** — Shows understanding of clean code principles even without frameworks.

### Key Classes

| Class | Responsibility |
|-------|----------------|
| `Task` | Represents a single task. Contains all properties and helper methods (`isOverdue()`, `toString()`). |
| `TaskRepository` | Handles saving and loading tasks from the file system. |
| `TaskService` | Contains all business operations (create, update, delete, search, filter, undo). |
| `AnalyticsService` | Calculates and displays statistics. |
| `InputHelper` | Provides safe methods for reading user input (avoids crashes from bad input). |
| `Main` | Displays the menu and calls the appropriate service methods. |

---

## 10. Data Persistence Explained

Tasks are stored in a plain text file: `data/tasks.dat`

### File Format Example

```
NEXT_ID=4
1|Finish portfolio|Write README|School|HIGH|2026-10-30|false|2026-10-24|
2|Buy groceries|Milk and eggs|Personal|MEDIUM|2026-10-25|true|2026-10-20|2026-10-24
3|Study for quiz|Chapter 5 and 6|School|CRITICAL|2026-10-26|false|2026-10-22|
```

### Format Rules

- First line stores the next available ID
- Each following line is one task
- Fields are separated by the `|` character
- Special characters inside text are escaped (`|` becomes `¦`)

### Why custom format instead of JSON?

Because this is a **zero-dependency** project. Using built-in Java file I/O only demonstrates deeper understanding of how data serialization works.

---

## 11. Key Concepts Demonstrated

This project intentionally uses the following Java concepts:

- **Object-Oriented Programming**
  - Classes, objects, encapsulation
  - Enums (`Priority`)
  - Multiple constructors
  - Overriding `toString()`, `equals()`, `hashCode()`

- **Collections Framework**
  - `ArrayList`
  - `Deque` / `ArrayDeque` (for Undo history)
  - `Optional`

- **Java Streams API**
  - Filtering
  - Mapping
  - Grouping
  - Counting

- **File I/O**
  - `BufferedReader` / `BufferedWriter`
  - `Files` and `Paths` (NIO)
  - Custom serialization / deserialization

- **Date Handling**
  - `LocalDate`
  - `DateTimeFormatter`

- **Error Handling**
  - Try-catch blocks
  - Input validation loops

- **Clean Code Practices**
  - Meaningful names
  - Single Responsibility Principle
  - Layered architecture

---

## 12. Possible Future Improvements

If you want to extend this project later, here are good ideas:

- Add sorting options (by due date, priority, title)
- Export tasks to CSV
- Add subtasks / task dependencies
- Simple password protection for the data file
- Colorized console output
- Recurring tasks
- Statistics over time (weekly completion rate)

All of these can still be done with pure Java.

---

## 13. Troubleshooting

### Error: `javac is not recognized`

Java is not installed or not added to PATH.  
Install JDK and restart your terminal.

### Error: `error: no source files`

You are not inside the correct folder, or the path to the `.java` files is wrong.  
Make sure you are in the folder that contains the `src` directory.

### Error: `cannot find symbol` or package errors

You compiled only some files. Use the full compile command provided in section 6.

### Program runs but tasks disappear after closing

The `data` folder could not be created (permission issue).  
Run the terminal as administrator, or check that the folder is writable.

### Windows PowerShell: `find` command not found

This is normal. Use the Windows version of the compile command (with backslashes `\` and `*.java`).

---

## 14. Author

**BSIT First Year Portfolio Project**

Built entirely with pure Java.  
No external libraries. No build tools required.

---

### Quick Start Summary (Copy-Paste)

**Windows:**
```powershell
javac -encoding UTF-8 -d out src\com\taskforge\model\*.java src\com\taskforge\storage\*.java src\com\taskforge\service\*.java src\com\taskforge\util\*.java src\com\taskforge\Main.java
java -cp out com.taskforge.Main
```

**macOS / Linux:**
```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out com.taskforge.Main
```

---

Thank you for checking out **TaskForge**.
