
package org.dbu.library.repository

import org.dbu.library.model.Book
import org.dbu.library.model.Patron

class InMemoryLibraryRepository : LibraryRepository {

// books is the name of the notebook.
// We use the ISBN to identify each book.
// mutableMapOf creates a collection that can hold book records
// and be changed as the program runs.
// Like when we update the notebook by adding another book.
    private val books = mutableMapOf<String, Book>()
    private val patrons = mutableMapOf<String, Patron>()

    // TODO: Implement the methods to manage books and patrons in memory

    override fun addBook(book: Book): Boolean {
        if (books.containsKey(book.isbn)) {
            return false
        }

        books[book.isbn] = book
        return true
    }

    override fun addPatron(patron: Patron): Boolean {
        if (patrons.containsKey(patron.id)) {
            return false
        }

        patrons[patron.id] = patron
        return true
    }

    override fun findBook(isbn: String): Book? {
        return books[isbn]
    }

    // The ? in Patron? means the method might return a patron
    // or null if no matching person exists.
    override fun findPatron(id: String): Patron? {
        return patrons[id]
    }

    // book on the right: the new book information we want to save.
    // It might say Clean Code, ISBN 101, status Borrowed.
    // book.isbn: the ISBN of that new book, which is 101.
    // books[book.isbn]: the place in our notebook where the
    // record for ISBN 101 belongs.
    // = : put the information on the right into the place on the left.
    override fun updateBook(book: Book) {
        books[book.isbn] = book
    }

    override fun updatePatron(patron: Patron) {
        patrons[patron.id] = patron
    }

    // books.values gets all the books in the notebook.
    // toList() turns them into a list.
    // List<Book> means a list containing books.
    override fun getAllBooks(): List<Book> {
        return books.values.toList()
    }
}
