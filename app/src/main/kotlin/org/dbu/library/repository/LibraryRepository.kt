package org.dbu.library.repository

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
//Your project has a file named Book.kt. That file defines what information a book contains.
//Your project has a file called Patron.kt. It describes the information stored about a library user.

interface LibraryRepository {

    fun addBook(book: Book): Boolean
    fun findBook(isbn: String): Book?
    //isbn string new ena isbn ketegene book.kt lay yesu info return yderegal
    fun updateBook(book: Book)

    fun addPatron(patron: Patron): Boolean
    fun findPatron(id: String): Patron?
    fun updatePatron(patron: Patron)

    fun getAllBooks(): List<Book>
}