package com.evstafeeva.graphqltest.service;

import com.evstafeeva.graphqltest.application.api.AuthorInput;
import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import java.util.List;

public interface BookService {
    /**
     * Метод предназначен для сохранения автора.
     *
     * @param name имя автора
     * @param bookInputs книги автора
     * @return автор
     */
    Author saveAuthor(String name, List<BookInput> bookInputs);

    /**
     * Метод предназначен для сохранения книги.
     *
     * @param title название книги
     * @param authorInputs авторы книги
     * @return книга
     */
    Book saveBook(String title, List<AuthorInput> authorInputs);
}
