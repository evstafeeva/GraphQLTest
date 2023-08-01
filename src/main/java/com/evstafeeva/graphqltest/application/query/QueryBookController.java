package com.evstafeeva.graphqltest.application.query;

import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.repository.AuthorRepository;
import com.evstafeeva.graphqltest.repository.BookRepository;
import graphql.com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QueryBookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @QueryMapping
    public List<Book> getBooksByAuthor(@Argument String author) {
        return bookRepository.findByAuthors_Name(author);
    }

    @QueryMapping
    public List<Book> getAllBooks() {
        return Lists.newArrayList(bookRepository.findAll());
    }

    @QueryMapping
    public List<Author> getAuthor(@Argument String name) {
        return authorRepository.findAuthorsByName(name);
    }
}
