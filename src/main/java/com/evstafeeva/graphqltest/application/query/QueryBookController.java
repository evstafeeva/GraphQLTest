package com.evstafeeva.graphqltest.application.query;

import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.repository.AuthorRepository;
import com.evstafeeva.graphqltest.repository.BookRepository;
import graphql.com.google.common.collect.Lists;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QueryBookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * Эндпоинт предназначен для получения всех книг, написанных автором, переданным в запросе.
     * При этом считатся, что имя автора - не уникальное поле. Из-за чего в ответе запроса возвращается список книг
     * всех авторов, имеющих имя, идентичное с именем в запросе.
     *
     * @param author имя автора
     * @return список книг автораю
     */
    @QueryMapping
    public List<Book> getBooksByAuthor(@Argument String author) {
        log.info("Getting books by author with name \"{}\"", author);
        return bookRepository.findByAuthors_Name(author);
    }

    /**
     * Эндпоинт предназначен для получения всех книг из базы.
     *
     * @return список всех книг в базе
     */
    @QueryMapping
    public List<Book> getAllBooks() {
        log.info("Getting all books.");
        return Lists.newArrayList(bookRepository.findAll());
    }

    /**
     * Эндпоинт предназначен для получения автора по имени.
     * При этом считатся, что имя автора - не уникальное поле. Из-за чего в ответе запроса возвращается список, на
     * случай, если в базе сохранены несколько авторов с идентичными именами.
     *
     * @param name имя автора
     * @return список авторов
     */
    @QueryMapping
    public List<Author> getAuthor(@Argument String name) {
        log.info("Getting the author (or authors) with name \"{}\"", name);
        return authorRepository.findAuthorsByName(name);
    }
}
