package library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String BOOKS_FILE = "data/books.txt";
    private static final String MEMBERS_FILE = "data/members.txt";
    private static final String BOOKS_CSV = "data/books_export.csv";

    public List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);
        if (!file.exists()) {
            System.out.println("Books file not found. Starting with empty collection.");
            return books;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        books.add(Book.fromFileFormat(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing book: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    public void saveBooks(List<Book> books) {
        try {
            File file = new File(BOOKS_FILE);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Book book : books) {
                    writer.write(book.toFileFormat());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    public List<Member> loadMembers() {
        List<Member> members = new ArrayList<>();
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) {
            System.out.println("Members file not found. Starting with empty collection.");
            return members;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        members.add(Member.fromFileFormat(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing member: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        return members;
    }

    public void saveMembers(List<Member> members) {
        try {
            File file = new File(MEMBERS_FILE);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Member member : members) {
                    writer.write(member.toFileFormat());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    public void exportBooksToCSV(List<Book> books) {
        try {
            File file = new File(BOOKS_CSV);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("ISBN,Title,Author,Year,Status,BorrowedBy,DueDate");
                writer.newLine();
                for (Book book : books) {
                    writer.write(book.toFileFormat());
                    writer.newLine();
                }
            }
            System.out.println("Books exported to: " + BOOKS_CSV);
        } catch (IOException e) {
            System.err.println("Error exporting books: " + e.getMessage());
        }
    }
}
