package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

//TODO: implement all required functions to handle the menu actions

private fun addBook(service: LibraryService) {
    print("Enter ISBN: ")
    val isbn = readln().trim()

    print("Enter title: ")
    val title = readln().trim()

    print("Enter author: ")
    val author = readln().trim()

    print("Enter publication year: ")
    val year = readln().trim().toIntOrNull()
    //If the user enters hello, the result is null
    if (year == null) {
        println("Invalid year.")
        return
    }

    val book = Book(isbn, title, author, year)

    if (service.addBook(book)) {
        println("Book added successfully.")
    } else {
        println("A book with this ISBN already exists.")
    }
}

private fun registerPatron(repository: LibraryRepository) {
    print("Enter patron ID: ")
    val id = readln().trim()

    print("Enter patron name: ")
    val name = readln().trim()

    val patron = Patron(id, name)

    if (repository.addPatron(patron)) {
        println("Patron registered successfully.")
    } else {
        println("A patron with this ID already exists.")
    }
}

private fun borrowBook(service: LibraryService) {
    print("Enter patron ID: ")
    val patronId = readln().trim()

    print("Enter book ISBN: ")
    val isbn = readln().trim()

    when (service.borrowBook(patronId, isbn)) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully.")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found.")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found.")
        BorrowResult.NOT_AVAILABLE -> println("Book is already borrowed.")
        BorrowResult.LIMIT_REACHED -> println("Borrowing limit reached.")
    }
}

private fun returnBook(service: LibraryService) {
    print("Enter patron ID: ")
    val patronId = readln().trim()

    print("Enter book ISBN: ")
    val isbn = readln().trim()

    if (service.returnBook(patronId, isbn)) {
        println("Book returned successfully.")
    } else {
        println("Return failed. Check the patron ID and ISBN.")
    }
}

private fun search(service: LibraryService) {
    print("Enter book title or author: ")
    val query = readln().trim()

    val books = service.search(query)

    if (books.isEmpty()) {
        println("No matching books found.")
    } else {
        books.forEach { book ->
            println("$book")
        }
    }
}

private fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()

    if (books.isEmpty()) {
        println("No books available.")
    } else {
        books.forEach { book ->
            println("$book")
        }
    }
}
