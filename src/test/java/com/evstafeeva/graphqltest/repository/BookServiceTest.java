package com.evstafeeva.graphqltest.repository;

import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.service.BookServiceImpl;
import com.evstafeeva.graphqltest.service.exception.NotFoundException;
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
    public void saveAuthorWithNoSavedBooksTest() {
        String authorName = "authorName";
        String bookTitle = "bookTitle";

        Author savedAuthor = bookService.saveAuthor(authorName, List.of(new BookInput(null, bookTitle)));

        Author author = authorRepository.findById(savedAuthor.getId()).get();
        Assertions.assertEquals(authorName, author.getName());
        Assertions.assertTrue(!author.getBooks().isEmpty());
        Assertions.assertEquals(bookTitle, author.getBooks().get(0).getTitle());
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
}
