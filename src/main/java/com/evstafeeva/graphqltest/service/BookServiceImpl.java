package com.evstafeeva.graphqltest.service;

import com.evstafeeva.graphqltest.application.api.AuthorInput;
import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.repository.AuthorRepository;
import com.evstafeeva.graphqltest.repository.BookRepository;
import com.evstafeeva.graphqltest.service.exception.NotFoundException;
import graphql.com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Author saveAuthor(String name, List<BookInput> bookInputs) {
        List<Book> savedBooks = new ArrayList<>();
        List<Book> booksToSave = new ArrayList<>();

        for (BookInput bookInput : bookInputs) {
            if (bookInput.id() != null) {
                savedBooks.add(bookRepository.findById(bookInput.id())
                    .orElseThrow(() -> new NotFoundException("The book was not found by id" + bookInput.id())));
            } else {
                booksToSave.add(new Book(bookInput.title()));
            }
        }
        savedBooks.addAll(Lists.newArrayList(bookRepository.saveAll(booksToSave)));
        Author author = new Author(name, savedBooks);
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Book saveBook(String title, List<AuthorInput> authorInputs) {
        Book book = new Book(title);
        bookRepository.save(book);

        List<Author> authors = authorInputs.stream()
            .map(authorInput -> {
                if (authorInput.id() != null) {
                    Author author = authorRepository.findById(authorInput.id())
                        .orElseThrow(() -> new NotFoundException("The author was not found by id " + authorInput.id()));
                    author.getBooks().add(book);
                    return author;
                } else {
                    Author author = new Author();
                    author.setName(authorInput.name());
                    author.setBooks(List.of(book));
                    return author;
                }
            })
            .toList();
        Iterable<Author> savedAuthors = authorRepository.saveAll(authors);
        book.setAuthors(Lists.newArrayList(savedAuthors));
        return book;
    }
}
