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
