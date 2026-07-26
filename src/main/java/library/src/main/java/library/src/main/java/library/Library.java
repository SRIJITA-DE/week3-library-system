package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private List<Book> books;
    private List<Member> members;
    private FileHandler fileHandler;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.fileHandler = new FileHandler();
        loadData();
    }

    private void loadData() {
        List<Book> loadedBooks = fileHandler.loadBooks();
        if (loadedBooks != null) {
            books = loadedBooks;
        }
        List<Member> loadedMembers = fileHandler.loadMembers();
        if (loadedMembers != null) {
            members = loadedMembers;
        }
        System.out.println("Loaded " + books.size() + " books and " + members.size() + " members");
    }

    public void addBook(Book book) {
        if (findBookByIsbn(book.getIsbn()) != null) {
            System.out.println("Book with ISBN " + book.getIsbn() + " already exists!");
            return;
        }
        books.add(book);
        fileHandler.saveBooks(books);
        System.out.println("Book added successfully: " + book.getTitle());
    }

    public void removeBook(String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found with ISBN: " + isbn);
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Cannot remove a borrowed book!");
            return;
        }
        books.remove(book);
        fileHandler.saveBooks(books);
        System.out.println("Book removed successfully: " + book.getTitle());
    }

    public Book findBookByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchBooks(String keyword) {
        String lower = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lower) ||
                        book.getAuthor().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        System.out.println("\n=== ALL BOOKS ===");
        System.out.println("Total books: " + books.size());
        System.out.println("-".repeat(80));
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
        System.out.println("-".repeat(80));
    }

    public void displayAvailableBooks() {
        List<Book> available = books.stream().filter(Book::isAvailable).collect(Collectors.toList());
        if (available.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("\n=== AVAILABLE BOOKS ===");
        for (int i = 0; i < available.size(); i++) {
            System.out.println((i + 1) + ". " + available.get(i));
        }
    }

    public void registerMember(Member member) {
        if (findMemberById(member.getId()) != null) {
            System.out.println("Member with ID " + member.getId() + " already exists!");
            return;
        }
        members.add(member);
        fileHandler.saveMembers(members);
        System.out.println("Member registered successfully: " + member.getName());
    }

    public Member findMemberById(String id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void displayAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        System.out.println("\n=== ALL MEMBERS ===");
        System.out.println("Total members: " + members.size());
        System.out.println("-".repeat(80));
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i));
        }
        System.out.println("-".repeat(80));
    }

    public void borrowBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is already borrowed!");
            return;
        }
        if (member.getBorrowedCount() >= 5) {
            System.out.println("Member has reached maximum borrowing limit (5 books)!");
            return;
        }
        book.setAvailable(false);
        book.setBorrowedBy(memberId);
        book.setDueDate(LocalDate.now().plusWeeks(2));
        member.borrowBook(isbn);
        fileHandler.saveBooks(books);
        fileHandler.saveMembers(members);
        System.out.println("Book borrowed successfully!");
        System.out.println("Due date: " + book.getDueDate());
    }

    public void returnBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        if (book.isAvailable()) {
            System.out.println("Book is not borrowed!");
            return;
        }
        if (!book.getBorrowedBy().equals(memberId)) {
            System.out.println("This book was not borrowed by this member!");
            return;
        }
        double fine = 0;
        if (book.isOverdue()) {
            fine = book.calculateFine();
            System.out.println("Book is overdue! Fine: ₹" + String.format("%.2f", fine));
        }
        book.setAvailable(true);
        book.setBorrowedBy(null);
        book.setDueDate(null);
        member.returnBook(isbn);
        fileHandler.saveBooks(books);
        fileHandler.saveMembers(members);
        System.out.println("Book returned successfully!");
        if (fine > 0) {
            System.out.println("Fine paid: ₹" + String.format("%.2f", fine));
        }
    }

    public void displayMemberDetails(String memberId) {
        Member member = findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        System.out.println("\n=== MEMBER DETAILS ===");
        System.out.println("ID: " + member.getId());
        System.out.println("Name: " + member.getName());
        System.out.println("Email: " + member.getEmail());
        System.out.println("Phone: " + member.getPhone());
        System.out.println("Books Borrowed: " + member.getBorrowedCount());
        if (!member.getBorrowedBooks().isEmpty()) {
            System.out.println("\nBorrowed Books:");
            for (String isbn : member.getBorrowedBooks()) {
                Book book = findBookByIsbn(isbn);
                if (book != null) {
                    System.out.println("  - " + book.getTitle() + " (Due: " + book.getDueDate() + ")");
                }
            }
        }
    }

    public void displayStatistics() {
        long availableBooks = books.stream().filter(Book::isAvailable).count();
        long borrowedBooks = books.size() - availableBooks;
        long overdueBooks = books.stream()
                .filter(book -> !book.isAvailable() && book.isOverdue())
                .count();
        System.out.println("\n=== LIBRARY STATISTICS ===");
        System.out.println("Total Books: " + books.size());
        System.out.println("Available Books: " + availableBooks);
        System.out.println("Borrowed Books: " + borrowedBooks);
        System.out.println("Registered Members: " + members.size());
        System.out.println("Overdue Books: " + overdueBooks);
        if (overdueBooks > 0) {
            System.out.println("\nOverdue Books List:");
            books.stream()
                    .filter(book -> !book.isAvailable() && book.isOverdue())
                    .forEach(book -> {
                        Member member = findMemberById(book.getBorrowedBy());
                        System.out.println("  - " + book.getTitle() +
                                " (Borrower: " + (member != null ? member.getName() : "Unknown") +
                                ", Days Overdue: " + book.calculateOverdueDays() +
                                ", Fine: ₹" + String.format("%.2f", book.calculateFine()) + ")");
                    });
        }
    }

    public void exportData() {
        fileHandler.exportBooksToCSV(books);
        System.out.println("Data exported successfully!");
    }
}
