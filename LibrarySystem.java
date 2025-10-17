import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LibrarySystem {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private User loggedInUser;

    public LibrarySystem() {
        loadUsers();
        loadBooks();
        loadTransactions();
    }

    // ---------- LOGIN ----------
    public boolean login() {
        Scanner sc = new Scanner(System.in);
        int attempts = 3;

        while (attempts > 0) {
            System.out.print("Enter username: ");
            String name = sc.nextLine();
            System.out.print("Enter password: ");
            String pass = sc.nextLine();

            for (User u : users) {
                if (u.getName().equalsIgnoreCase(name) && u.getPassword().equals(pass)) {
                    loggedInUser = u;
                    System.out.println("\nLogin successful! Welcome, " + loggedInUser.getName() + ".");
                    return true;
                }
            }

            attempts--;
            System.out.println("Invalid credentials. Attempts left: " + attempts);
        }

        return false;
    }

    // ---------- MENU ----------
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Library Menu ---");
            System.out.println("1. View All Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewAllBooks();
                case 2 -> borrowBook();
                case 3 -> returnBook();
                case 4 -> saveAll();
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 4);
    }

    // ---------- FILE LOADING ----------
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                users.add(new User(data[0], data[1], data[2], data[3]));
            }
        } catch (IOException e) {
            System.out.println("Error loading users.txt");
        }
    }

    private void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                books.add(new Book(data[0], data[1], data[2], Boolean.parseBoolean(data[3])));
            }
        } catch (IOException e) {
            System.out.println("Error loading books.txt");
        }
    }

    private void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                transactions.add(new Transaction(data[0], data[1], data[2], data[3], data[4]));
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions.txt");
        }
    }

    // ---------- SAVE DATA ----------
    private void saveAll() {
        saveBooks();
        saveTransactions();
    }

    private void saveBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book b : books) {
                bw.write(b.getBookId() + "," + b.getTitle() + "," + b.getAuthor() + "," + b.isAvailable());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books.txt");
        }
    }

    private void saveTransactions() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.txt"))) {
            for (Transaction t : transactions) {
                bw.write(t.getTransactionId() + "," + t.getUserId() + "," + t.getBookId() + "," + t.getDateBorrowed() + "," + t.getDateReturned());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions.txt");
        }
    }

    // ---------- ACTIONS ----------
    private void viewAllBooks() {
        for (Book b : books) b.displayBookDetails();
    }

    private void borrowBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID: ");
        String bookId = sc.nextLine();

        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                if (!b.isAvailable()) {
                    System.out.println("Sorry, that book is already borrowed.");
                    return;
                }

                b.setAvailable(false);
                String tId = "T" + String.format("%03d", transactions.size() + 1);
                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                transactions.add(new Transaction(tId, loggedInUser.getId(), bookId, date, "null"));
                System.out.println("Book borrowed successfully!");
                saveAll();
                return;
            }
        }
        System.out.println("Book not found!");
    }

    private void returnBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to return: ");
        String bookId = sc.nextLine();

        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                b.setAvailable(true);
                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                for (Transaction t : transactions) {
                    if (t.getUserId().equals(loggedInUser.getId()) && t.getBookId().equals(bookId) && t.getDateReturned().equals("null")) {
                        t.setDateReturned(date);
                        System.out.println("Book returned successfully!");
                        saveAll();
                        return;
                    }
                }
            }
        }
        System.out.println("No borrowed record found for this book.");
    }
}
