package com.evstafeeva.graphqltest.application;

import com.evstafeeva.graphqltest.application.api.AuthorInput;
import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.repository.AuthorRepository;
import com.evstafeeva.graphqltest.repository.BookRepository;
import graphql.com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @QueryMapping
    public Book bookById(@Argument String id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @QueryMapping
    public Author authorById(@Argument String id) {
        return authorRepository.findById(id).orElseThrow();
    }

    @QueryMapping
    public List<Book> getBooksByAuthor(@Argument String author) {
        return bookRepository.findByAuthors_Name(author);
    }

    @QueryMapping
    public List<Book> getAllBooks() {
        return Lists.newArrayList(bookRepository.findAll());
    }

    @QueryMapping
    public Author getAuthor(@Argument String name) {
        return authorRepository.findAuthorByName(name);
    }

    @MutationMapping
    @Transactional
    public Book saveBook(@Argument String title,
                         @Argument List<AuthorInput> authorInputs) {
        Book book = new Book(title);
        Book savedBook = bookRepository.save(book);

        List<Author> authors = authorInputs.stream()
            .map(authorInput -> {
                Author author = new Author();
                author.setId(authorInput.id());
                author.setName(authorInput.name());
                author.setBooks(List.of(savedBook));
                return author;
            })
            .toList();
        Iterable<Author> savedAuthors = authorRepository.saveAll(authors);
        savedBook.setAuthors(Lists.newArrayList(savedAuthors));
        return savedBook;
    }

    @MutationMapping
    @Transactional
    public Author saveAuthor(@Argument String name,
                             @Argument List<BookInput> bookInputs) {
        List<Book> books = bookInputs.stream()
            .map(bookInput -> new Book(bookInput.id(), bookInput.title()))
            .toList();
        Iterable<Book> savedBooks  = bookRepository.saveAll(books);
        Author author = new Author(name, Lists.newArrayList(savedBooks));
        return authorRepository.save(author);
    }
}
