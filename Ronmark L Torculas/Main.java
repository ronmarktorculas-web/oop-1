import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ------------------- MAIN (MUST BE FIRST!) -------------------
public class LibraryManagementSystem {
    public static void main(String[] args) {
        LibrarySystem lib = new LibrarySystem();
        if (lib.login()) {
            lib.showMenu();
        } else {
            System.out.println("Login failed. Exiting system...");
        }
    }
}

// ------------------- Person.java -------------------
class Person {
    protected String id;
    protected String name;

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// ------------------- User.java -------------------
class User extends Person {
    private String password;
    private String role;
    private ArrayList<String> borrowedBooks;

    public User(String id, String name, String password, String role) {
        super(id, name);
        this.password = password;
        this.role = role;
        this.borrowedBooks = new ArrayList<>();
    }

    @Override
    public void displayInfo() {
        System.out.println("\n--- USER DETAILS ---");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
        System.out.println("Borrowed Books: " + borrowedBooks.size());
    }

    public String getPassword() { return password; }
    public String getRole() { return role; }
    public boolean isAdmin() { return role.equalsIgnoreCase("admin"); }
    public ArrayList<String> getBorrowedBooks() { return borrowedBooks; }
    public boolean canBorrowMore() { return borrowedBooks.size() < 3; }
    public void addBorrowedBook(String bookId) { borrowedBooks.add(bookId); }
    public void removeBorrowedBook(String bookId) { borrowedBooks.remove(bookId); }
}

// ------------------- Book.java -------------------
class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean available;

    public Book(String bookId, String title, String author, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public void displayBookDetails() {
        String status = available ? "Available" : "Borrowed";
        System.out.printf("%-8s %-30s %-20s %-10s%n", bookId, title, author, status);
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}

// ------------------- Transaction.java -------------------
class Transaction {
    private String transactionId;
    private String userId;
    private String bookId;
    private String dateBorrowed;
    private String dateReturned;

    public Transaction(String transactionId, String userId, String bookId,
                       String dateBorrowed, String dateReturned) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }

    public void displayTransaction() {
        String returned = dateReturned.equals("null") ? "Not Returned" : dateReturned;
        System.out.printf("%-8s %-8s %-8s %-15s %-15s%n",
                transactionId, userId, bookId, dateBorrowed, returned);
    }

    public String getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public String getDateBorrowed() { return dateBorrowed; }
    public String getDateReturned() { return dateReturned; }
    public void setDateReturned(String dateReturned) { this.dateReturned = dateReturned; }
}

// ------------------- LibrarySystem.java -------------------
class LibrarySystem {
    private ArrayList<User> users;
    private ArrayList<Book> books;
    private ArrayList<Transaction> transactions;
    private User loggedInUser;
    private Scanner sc = new Scanner(System.in);

    public LibrarySystem() {
        users = new ArrayList<>();
        books = new ArrayList<>();
        transactions = new ArrayList<>();
        loadDefaultData();
    }

    private void loadDefaultData() {
        users.add(new User("U001", "John Doe", "pass123", "user"));
        users.add(new User("A001", "Admin", "admin123", "admin"));

        books.add(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", true));
        books.add(new Book("B002", "1984", "George Orwell", true));
        books.add(new Book("B003", "To Kill a Mockingbird", "Harper Lee", false));
    }

    public boolean login() {
        System.out.println("\n========================================");
        System.out.println("  LIBRARY MANAGEMENT SYSTEM - LOGIN");
        System.out.println("========================================");
        
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("\nUsername: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            for (User u : users) {
                if (u.getName().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                    loggedInUser = u;
                    System.out.println("\n✓ Login successful! Welcome, " + u.getName() + "!");
                    return true;
                }
            }
            attempts--;
            System.out.println("✗ Invalid credentials. Attempts left: " + attempts);
        }
        return false;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("         LIBRARY MAIN MENU");
            System.out.println("========================================");
            System.out.println("1. View All Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            if (loggedInUser.isAdmin()) {
                System.out.println("4. View Transactions (Admin)");
            }
            System.out.println("0. Exit");
            System.out.println("========================================");
            System.out.print("Choice: ");

            String choice = sc.nextLine();
            switch (choice) {
                case "1": 
                    viewAllBooks(); 
                    break;
                case "2": 
                    borrowBook(); 
                    break;
                case "3": 
                    returnBook(); 
                    break;
                case "4": 
                    if (loggedInUser.isAdmin()) {
                        viewAllTransactions();
                    } else {
                        System.out.println("✗ Access denied. Admin only.");
                    }
                    break;
                case "0": 
                    System.out.println("\n✓ Thank you for using Library System. Goodbye!"); 
                    return;
                default: 
                    System.out.println("✗ Invalid choice. Please try again.");
            }
        }
    }

    private void viewAllBooks() {
        System.out.println("\n========================================");
        System.out.println("           BOOK CATALOGUE");
        System.out.println("========================================");
        System.out.printf("%-8s %-30s %-20s %-10s%n", "ID", "Title", "Author", "Status");
        System.out.println("------------------------------------------------------------------------");
        for (Book b : books) {
            b.displayBookDetails();
        }
    }

    private void borrowBook() {
        System.out.println("\n--- BORROW BOOK ---");
        viewAllBooks();
        System.out.print("\nEnter Book ID to borrow: ");
        String bookId = sc.nextLine();

        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                if (!b.isAvailable()) {
                    System.out.println("✗ Sorry, book is already borrowed.");
                    return;
                }
                if (!loggedInUser.canBorrowMore()) {
                    System.out.println("✗ You already borrowed 3 books (maximum limit).");
                    return;
                }
                b.setAvailable(false);
                loggedInUser.addBorrowedBook(bookId);
                
                String transId = "T00" + (transactions.size() + 1);
                transactions.add(new Transaction(transId, loggedInUser.getId(), 
                                                bookId, getToday(), "null"));
                
                System.out.println("✓ Book borrowed successfully!");
                System.out.println("  Transaction ID: " + transId);
                System.out.println("  Please return by: " + getFutureDate(14));
                return;
            }
        }
        System.out.println("✗ Book not found.");
    }

    private void returnBook() {
        System.out.println("\n--- RETURN BOOK ---");
        
        if (loggedInUser.getBorrowedBooks().isEmpty()) {
            System.out.println("✗ You have no books to return.");
            return;
        }
        
        System.out.println("Your borrowed books:");
        for (String bookId : loggedInUser.getBorrowedBooks()) {
            for (Book b : books) {
                if (b.getBookId().equals(bookId)) {
                    b.displayBookDetails();
                }
            }
        }
        
        System.out.print("\nEnter Book ID to return: ");
        String bookId = sc.nextLine();

        if (!loggedInUser.getBorrowedBooks().contains(bookId)) {
            System.out.println("✗ You didn't borrow this book.");
            return;
        }

        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                b.setAvailable(true);
                loggedInUser.removeBorrowedBook(bookId);

                for (Transaction t : transactions) {
                    if (t.getBookId().equals(bookId) && 
                        t.getUserId().equals(loggedInUser.getId()) && 
                        t.getDateReturned().equals("null")) {
                        t.setDateReturned(getToday());
                    }
                }

                System.out.println("✓ Book returned successfully! Thank you.");
                return;
            }
        }
    }

    private void viewAllTransactions() {
        System.out.println("\n========================================");
        System.out.println("        ALL TRANSACTIONS (ADMIN)");
        System.out.println("========================================");
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.printf("%-8s %-8s %-8s %-15s %-15s%n", "T_ID", "User", "Book", "Borrowed", "Returned");
        System.out.println("------------------------------------------------------------------------");
        for (Transaction t : transactions) {
            t.displayTransaction();
        }
    }

    private String getToday() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    private String getFutureDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}