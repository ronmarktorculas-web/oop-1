public class Main {
    public static void main(String[] args) {
        LibrarySystem lib = new LibrarySystem();

        if (lib.login()) {
            lib.showMenu();
        } else {
            System.out.println("Login failed. Exiting system...");
        }
    }
}
