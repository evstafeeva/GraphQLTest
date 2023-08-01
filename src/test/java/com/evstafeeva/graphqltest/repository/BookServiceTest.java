package com.evstafeeva.graphqltest.repository;

import com.evstafeeva.graphqltest.application.api.AuthorInput;
import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.service.BookServiceImpl;
import com.evstafeeva.graphqltest.service.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BookServiceTest extends TestContainersSpringBootTests {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookServiceImpl bookService;

    @Test
    @Transactional
    public void saveAuthorWithNotSavedBooksTest() {
        String authorName = "authorName";
        String bookTitle = "bookTitle";

        Author savedAuthor = bookService.saveAuthor(authorName, List.of(new BookInput(null, bookTitle)));

        Author author = authorRepository.findById(savedAuthor.getId()).get();
        Assertions.assertEquals(authorName, author.getName());
        Assertions.assertTrue(!author.getBooks().isEmpty());
        Assertions.assertEquals(bookTitle, author.getBooks().get(0).getTitle());
        Book book = bookRepository.findById(author.getBooks().get(0).getId()).get();
        Assertions.assertEquals(bookTitle, book.getTitle());
    }

    @Test
    @Transactional
    public void saveAuthorWithAlreadySavedBooksTest() {
        String authorName = "authorName";
        String bookTitle = "bookTitle";
        int bookId = bookRepository.save(new Book(bookTitle)).getId();


        Author savedAuthor = bookService.saveAuthor(authorName, List.of(new BookInput(bookId, null)));

        Author author = authorRepository.findById(savedAuthor.getId()).get();
        Assertions.assertEquals(authorName, author.getName());
        Assertions.assertTrue(!author.getBooks().isEmpty());
        Assertions.assertEquals(bookTitle, author.getBooks().get(0).getTitle());
    }

    @Test
    @Transactional
    public void saveAuthorWithDifferentBooksTest() {
        String authorName = "authorName";
        String savedBookTitle = "savedBookTitle";
        String notSavedBookTitle = "notSavedBookTitle";
        int bookId = bookRepository.save(new Book(savedBookTitle)).getId();


        Author savedAuthor = bookService.saveAuthor(authorName,
            List.of(new BookInput(bookId, null), new BookInput(null, notSavedBookTitle)));

        Author author = authorRepository.findById(savedAuthor.getId()).get();
        Assertions.assertEquals(authorName, author.getName());
        Assertions.assertEquals(2, author.getBooks().size());
        Assertions.assertTrue(author.getBooks().stream().anyMatch(b -> savedBookTitle.equals(b.getTitle())));
        Assertions.assertTrue(author.getBooks().stream().anyMatch(b -> notSavedBookTitle.equals(b.getTitle())));
    }

    @Test
    @Transactional
    public void saveAuthorWithIdOfNotExistBook() {
        Assertions.assertThrows(NotFoundException.class,
            () -> bookService.saveAuthor("authorName", List.of(new BookInput(1, null))));
    }

    @Test
    @Transactional
    public void saveBookWithNotSavedAuthorTest() {
        String title = "title";
        String name = "name";

        Book savedBook = bookService.saveBook(title, List.of(new AuthorInput(null, name)));

        Book book = bookRepository.findById(savedBook.getId()).get();
        Assertions.assertEquals(title, book.getTitle());
        Assertions.assertTrue(!book.getAuthors().isEmpty());
        Assertions.assertEquals(name, book.getAuthors().get(0).getName());
        Author author = authorRepository.findById(book.getAuthors().get(0).getId()).get();
        Assertions.assertEquals(name, author.getName());
    }

    @Test
    @Transactional
    public void saveBookWithSavedAuthorTest() {
        String title = "title";
        String name = "name";
        Book testBook = bookRepository.save(new Book("test"));
        int authorId = authorRepository.save(new Author(name, new ArrayList<>(List.of(testBook)))).getId();

        Book savedBook = bookService.saveBook(title, List.of(new AuthorInput(authorId, null)));

        Book book = bookRepository.findById(savedBook.getId()).get();
        Assertions.assertEquals(title, book.getTitle());
        Assertions.assertTrue(!book.getAuthors().isEmpty());
        Assertions.assertEquals(name, book.getAuthors().get(0).getName());
    }

    @Test
    @Transactional
    public void saveBookWithDifferentAuthorTest() {
        String title = "title";
        String savedName = "name";
        String notSavedName = "notSavedName";
        Book testBook = bookRepository.save(new Book("test"));
        int authorId = authorRepository.save(new Author(savedName, new ArrayList<>(List.of(testBook)))).getId();

        Book savedBook = bookService.saveBook(title,
            List.of(new AuthorInput(authorId, null), new AuthorInput(null, notSavedName)));

        Book book = bookRepository.findById(savedBook.getId()).get();
        Assertions.assertEquals(title, book.getTitle());
        Assertions.assertEquals(2, book.getAuthors().size());
        Assertions.assertTrue(book.getAuthors().stream().anyMatch(a -> savedName.equals(a.getName())));
        Assertions.assertTrue(book.getAuthors().stream().anyMatch(a -> notSavedName.equals(a.getName())));
    }

    @Test
    @Transactional
    public void saveBookWithIdOfNotExistAuthtor() {
        Assertions.assertThrows(NotFoundException.class,
            () -> bookService.saveBook("bookTitle", List.of(new AuthorInput(1, null))));
    }

}
