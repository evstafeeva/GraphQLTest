package com.evstafeeva.graphqltest.service;

import com.evstafeeva.graphqltest.application.api.AuthorInput;
import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import java.util.List;

public interface BookService {
    Author saveAuthor(String name, List<BookInput> bookInputs);

    Book saveBook(String title, List<AuthorInput> authorInputs);
}
