
Regular User:

Username: John Doe
Password: pass123

Admin User:

Username: Admin
Password: admin123


Users can log in, view books, borrow, and return books.
All data is stored in text files so it stays even after the program closes.

Files Used

users.txt – Stores user info (ID, Name, Password, Role)

books.txt – Stores book details (ID, Title, Author, Availability)

transactions.txt – Records borrowed and returned books

Classes

Person – Base class (id, name)

User – Inherits from Person (password, role, borrowed books)

Book – Holds book information

Transaction – Records book borrow/return data

LibrarySystem – Main controller for login, menu, and file handling

🧠 Features

Login system (with 3 attempts)

Borrow and return books

View all books

Admin users can manage users, books, and transactions

Data saved automatically in text files

HOW TO RUN:

1. GO TO INTELLIJ IDEA
2. CREATE A FOLDER
3. THEN PASTE THE CODE ONE BY ONE (MAKE SURE THE FILE IS AS THE NAME OF THE CLASS)
4. THE TXT FILE SHOULD BE OUTSIDE THE FOLDER, SO RIGHT CLICK THE FOLDER AND THEN CREATE THE TXT FILE
5. GO TO THE Main.java THEN RUN
