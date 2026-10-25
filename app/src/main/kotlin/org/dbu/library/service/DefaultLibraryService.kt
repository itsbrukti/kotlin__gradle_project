
package org.dbu.library.service

import org.dbu.library.model.Book
import org.dbu.library.repository.LibraryRepository

class DefaultLibraryService(
    // The service receives access to the repository
    // so it can look up and save books and patrons.
    private val repository: LibraryRepository
) : LibraryService {

    // Add a new book by asking the repository to save it.
    // true means the book was added.
    // false means its ISBN already exists.
    override fun addBook(book: Book): Boolean {
        return repository.addBook(book)
    }

    // Borrow a book for a registered patron.
    override fun borrowBook(patronId: String, isbn: String): BorrowResult {

        // Check whether the book exists.
        // ?: means: if the result is missing, return the result on the right.
        val book = repository.findBook(isbn)
            ?: return BorrowResult.BOOK_NOT_FOUND

        // Check whether the patron is registered.
        val patron = repository.findPatron(patronId)
            ?: return BorrowResult.PATRON_NOT_FOUND

        // A book that is already borrowed cannot be borrowed again.
        if (!book.isAvailable) {
            return BorrowResult.NOT_AVAILABLE
        }

        // Make a new book record showing that the book is borrowed.
        val updatedBook = book.copy(isAvailable = false)
        repository.updateBook(updatedBook)

        // Add the book's ISBN to the patron's borrowed-books list.
        val updatedPatron = patron.copy(
            borrowedBooks = patron.borrowedBooks + isbn
        )
        repository.updatePatron(updatedPatron)

        // The complete borrowing process:
        // 1. Check that the book exists.
        // 2. Check that the patron exists.
        // 3. Check that the book is available.
        // 4. Mark the book as borrowed.
        // 5. Add the ISBN to the patron's record.
        return BorrowResult.SUCCESS
    }

    // Return a borrowed book.
    // true means the return succeeded.
    // false means it could not be returned.
    override fun returnBook(patronId: String, isbn: String): Boolean {

        val book = repository.findBook(isbn) ?: return false
        val patron = repository.findPatron(patronId) ?: return false

        // The patron must have this book in their borrowed-books list.
        if (isbn !in patron.borrowedBooks) {
            return false
        }

        // Mark the book as available again.
        repository.updateBook(
            book.copy(isAvailable = true)
        )

        // Remove the returned book from the patron's list.
        // filter keeps only ISBNs different from the returned ISBN.
        repository.updatePatron(
            patron.copy(
                borrowedBooks = patron.borrowedBooks.filter { it != isbn }
            )
        )

        return true
    }

    // Search books by title or author.
    // The search ignores uppercase and lowercase differences.
    override fun search(query: String): List<Book> {
        return repository.getAllBooks().filter { book ->
            book.title.contains(query, ignoreCase = true) ||
                book.author.contains(query, ignoreCase = true)
        }
    }
}
