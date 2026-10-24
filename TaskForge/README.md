# TaskForge v1.0

**Zero-Dependency Advanced Command-Line Task Manager**

A complete portfolio project for BSIT / first-year Computer Science students.  
Built with pure Java (JDK 8+) — **no external libraries**.

---

## Features

- Full CRUD (Create, Read, Update, Delete)
- Priority levels: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`
- Due dates + overdue detection
- Categories / tags
- Persistent storage using a custom text file format
- Search by keyword
- Filter by status, priority, or overdue
- Analytics dashboard (completion rate, priority distribution, top categories)
- Undo last action (history stack)
- Robust input validation and error handling
- Clean layered architecture

---

## Project Structure

```
TaskForge/
├── src/
│   └── com/
│       └── taskforge/
│           ├── Main.java
│           ├── model/
│           │   └── Task.java
│           ├── storage/
│           │   └── TaskRepository.java
│           ├── service/
│           │   ├── TaskService.java
│           │   └── AnalyticsService.java
│           └── util/
│               ├── DateUtil.java
│               └── InputHelper.java
├── data/                  ← created automatically
│   └── tasks.dat
└── README.md
```

---

## How to Compile & Run

### Option 1 – Simple
```bash
cd TaskForge
javac -d out $(find src -name "*.java")
java -cp out com.taskforge.Main
```

### Option 2 – From source
```bash
cd TaskForge
javac src/com/taskforge/model/*.java \
      src/com/taskforge/storage/*.java \
      src/com/taskforge/service/*.java \
      src/com/taskforge/util/*.java \
      src/com/taskforge/Main.java

java -cp src com.taskforge.Main
```

---

## Architecture Highlights

| Layer       | Responsibility                          |
|-------------|-----------------------------------------|
| **Model**   | `Task` entity + Priority enum           |
| **Storage** | Custom file persistence (no JSON libs)  |
| **Service** | Business logic + Undo history           |
| **UI**      | Console menu in `Main`                  |

- Uses Java Streams for filtering & analytics
- `LocalDate` for date handling
- Custom escaping for the text-based data file
- Snapshot-based Undo (max 10 levels)

---

## Sample Data File Format (`data/tasks.dat`)

```
NEXT_ID=5
1|Finish portfolio|Write README and polish code|School|HIGH|2026-10-30|false|2026-10-24|
2|Buy groceries|Milk, eggs, bread|Personal|MEDIUM|2026-10-25|true|2026-10-20|2026-10-24
```

---

## Why this project is strong for a portfolio

- Demonstrates **OOP**, **file I/O**, **collections**, **streams**, and **clean architecture**
- Zero dependencies → pure language skills
- Real persistence (data survives program restart)
- Thoughtful features (Undo, Analytics, Overdue detection)
- Easy to extend later (sorting, export to CSV, subtasks, etc.)

---

## Author

BSIT First Year Portfolio Project  
Created with pure Java – no frameworks, no libraries.
