package library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
    private String id;
    private String name;
    private String email;
    private String phone;
    private List<String> borrowedBooks;

    public Member(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    public void borrowBook(String isbn) {
        borrowedBooks.add(isbn);
    }

    public void returnBook(String isbn) {
        borrowedBooks.remove(isbn);
    }

    public int getBorrowedCount() {
        return borrowedBooks.size();
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Email: %s | Phone: %s | Books: %d",
                id, name, email, phone, borrowedBooks.size());
    }

    public String toFileFormat() {
        return String.join(",",
                id,
                name,
                email,
                phone,
                String.join(";", borrowedBooks)
        );
    }

    public static Member fromFileFormat(String line) {
        String[] parts = line.split(",");
        Member member = new Member(parts[0], parts[1], parts[2], parts[3]);
        if (parts.length > 4 && !parts[4].isEmpty()) {
            String[] books = parts[4].split(";");
            for (String isbn : books) {
                if (!isbn.isEmpty()) {
                    member.borrowBook(isbn);
                }
            }
        }
        return member;
    }
}
