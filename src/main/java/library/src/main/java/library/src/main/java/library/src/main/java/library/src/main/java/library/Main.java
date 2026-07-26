package library;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  LIBRARY MANAGEMENT SYSTEM");
        System.out.println("========================================");

        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addBookMenu();
                    break;
                case 2:
                    library.displayAllBooks();
                    break;
                case 3:
                    searchBooksMenu();
                    break;
                case 4:
                    registerMemberMenu();
                    break;
                case 5:
                    borrowBookMenu();
                    break;
                case 6:
                    returnBookMenu();
                    break;
                case 7:
                    library.displayStatistics();
                    break;
                case 8:
                    viewMemberMenu();
                    break;
                case 9:
                    library.exportData();
                    break;
                case 10:
                    System.out.println("Thank you for using Library Management System!");
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            scanner.nextLine();
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
        System.out.println("1. Add New Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search Books");
        System.out.println("4. Register Member");
        System.out.println("5. Borrow Book");
        System.out.println("6. Return Book");
        System.out.println("7. View Library Statistics");
        System.out.println("8. View Member Details");
        System.out.println("9. Export Data to CSV");
        System.out.println("10. Exit");
        System.out.println("=".repeat(40));
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void addBookMenu() {
        System.out.println("\n--- ADD NEW BOOK ---");
        String isbn = getStringInput("Enter ISBN: ");
        String title = getStringInput("Enter Title: ");
        String author = getStringInput("Enter Author: ");
        int year = getIntInput("Enter Publication Year: ");
        Book book = new Book(isbn, title, author, year);
        library.addBook(book);
    }

    private static void searchBooksMenu() {
        System.out.println("\n--- SEARCH BOOKS ---");
        String keyword = getStringInput("Enter search keyword (title or author): ");
        List<Book> results = library.searchBooks(keyword);
        if (results.isEmpty()) {
            System.out.println("No books found matching: " + keyword);
        } else {
            System.out.println("\nFound " + results.size() + " book(s):");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    private static void registerMemberMenu() {
        System.out.println("\n--- REGISTER MEMBER ---");
        String id = getStringInput("Enter Member ID: ");
        String name = getStringInput("Enter Name: ");
        String email = getStringInput("Enter Email: ");
        String phone = getStringInput("Enter Phone: ");
        Member member = new Member(id, name, email, phone);
        library.registerMember(member);
    }

    private static void borrowBookMenu() {
        System.out.println("\n--- BORROW BOOK ---");
        library.displayAvailableBooks();
        String isbn = getStringInput("Enter Book ISBN: ");
        String memberId = getStringInput("Enter Member ID: ");
        library.borrowBook(isbn, memberId);
    }

    private static void returnBookMenu() {
        System.out.println("\n--- RETURN BOOK ---");
        String isbn = getStringInput("Enter Book ISBN: ");
        String memberId = getStringInput("Enter Member ID: ");
        library.returnBook(isbn, memberId);
    }

    private static void viewMemberMenu() {
        System.out.println("\n--- VIEW MEMBER ---");
        library.displayAllMembers();
        String memberId = getStringInput("Enter Member ID: ");
        library.displayMemberDetails(memberId);
    }
}
