# WEEK3-LIBARARY-SYSTEM
# Console-Based Library Management System

**Author:** Srijita De  
**Date:** July 2026  
**Email:** contact.srijita@gmail.com  
**GitHub Repository:** github.com/your-username/week3-library-system

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Project Objectives](#2-project-objectives)
3. [Key Features](#3-key-features)
4. [Technologies Used](#4-technologies-used)
5. [File Structure](#5-file-structure)
6. [Complete Source Code](#6-complete-source-code)
7. [How to Compile and Run](#7-how-to-compile-and-run)
8. [Sample Menu Output](#8-sample-menu-output)
9. [Data Persistence](#9-data-persistence)
10. [Class Documentation](#10-class-documentation)
11. [Exception Handling](#11-exception-handling)
12. [Customization Guide](#12-customization-guide)
13. [Conclusion](#13-conclusion)
14. [License](#14-license)

---

## 1. Project Overview

The Console-Based Library Management System is a Java application that allows librarians to manage books, members, and borrowing operations through a command-line interface. The system demonstrates Object-Oriented Programming principles including encapsulation, inheritance, and polymorphism. Data persistence is achieved through file I/O operations, storing book and member information in text files.

This project showcases proficiency in Java programming, including classes and objects, ArrayLists for collection management, exception handling, input validation, and console-based user interaction.

---

## 2. Project Objectives

1. Build a complete library management system using Java and OOP principles.
2. Implement proper encapsulation with getters, setters, and constructors.
3. Use ArrayLists for storing and managing collections of books and members.
4. Implement file I/O operations for data persistence (books.txt, members.txt).
5. Create a console-based menu system for user interaction.
6. Add comprehensive input validation and exception handling.
7. Implement search and filter functionality for books and members.
8. Calculate overdue fines based on due date tracking.
9. Generate library statistics (total books, available, borrowed, members).
10. Demonstrate clean code organization with proper package structure.

---

## 3. Key Features

| Feature | Description |
|---------|-------------|
| **Add Books** | Add new books with ISBN, title, author, and publication year |
| **Remove Books** | Remove books from the library collection by ISBN |
| **View All Books** | Display all books with their current availability status |
| **Search Books** | Search for books by title or author (case-insensitive) |
| **Register Members** | Register new library members with unique IDs |
| **Borrow Books** | Allow members to borrow available books with due date tracking (2-week loan period) |
| **Return Books** | Process book returns and calculate overdue fines (₹10 per day overdue) |
| **View Member Details** | Display member information and borrowed books |
| **Library Statistics** | Generate reports on total books, available/borrowed counts, and overdue books |
| **Data Persistence** | Save all data to text files and load on program startup |
| **Input Validation** | Validate all user inputs with appropriate error messages |
| **Export Data** | Export book data to CSV format |

---

## 4. Technologies Used

| Technology | Purpose | Key Applications |
|------------|---------|------------------|
| Java 17+ | Programming Language | OOP concepts, classes, interfaces, collections |
| File I/O | Data Persistence | BufferedReader/BufferedWriter, FileReader/FileWriter |
| ArrayList | Collection Management | Dynamic storage of books and members |
| LocalDate | Date Handling | Due date tracking, overdue calculation |
| Stream API | Data Processing | Filtering, searching, statistics generation |
| Scanner | User Input | Console-based menu interaction |

---

## 5. File Structure
week3-library-system/
│
├── src/
│ └── main/
│ └── java/
│ └── library/
│ ├── Main.java # Entry point, menu system
│ ├── Book.java # Book class with properties
│ ├── Member.java # Member class with properties
│ ├── Library.java # Core business logic
│ └── FileHandler.java # File I/O operations
│
├── data/
│ ├── books.txt # Book data persistence
│ └── members.txt # Member data persistence
│
├── README.md # Project documentation
├── .gitignore # Git ignore rules
└── pom.xml # Maven configuration

---

## 6. Complete Source Code

### Book.java

```java
package library;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private String isbn;
    private String title;
    private String author;
    private int year;
    private boolean available;
    private String borrowedBy;
    private LocalDate dueDate;

    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = true;
        this.borrowedBy = null;
        this.dueDate = null;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }
    public String getBorrowedBy() { return borrowedBy; }
    public LocalDate getDueDate() { return dueDate; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setYear(int year) { this.year = year; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setBorrowedBy(String borrowedBy) { this.borrowedBy = borrowedBy; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isOverdue() {
        if (dueDate == null) return false;
        return LocalDate.now().isAfter(dueDate);
    }

    public long calculateOverdueDays() {
        if (dueDate == null) return 0;
        if (!isOverdue()) return 0;
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }

    public double calculateFine() {
        return calculateOverdueDays() * 10.0;
    }

    @Override
    public String toString() {
        return String.format("ISBN: %s | Title: %s | Author: %s | Year: %d | %s",
                isbn, title, author, year,
                available ? "Available" : "Borrowed by: " + borrowedBy);
    }

    public String toFileFormat() {
        return String.join(",",
                isbn,
                title,
                author,
                String.valueOf(year),
                String.valueOf(available),
                borrowedBy == null ? "null" : borrowedBy,
                dueDate == null ? "null" : dueDate.toString()
        );
    }

    public static Book fromFileFormat(String line) {
        String[] parts = line.split(",");
        Book book = new Book(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
        book.setAvailable(Boolean.parseBoolean(parts[4]));
        if (!parts[5].equals("null")) {
            book.setBorrowedBy(parts[5]);
        }
        if (!parts[6].equals("null")) {
            book.setDueDate(LocalDate.parse(parts[6]));
        }
        return book;
    }
}
