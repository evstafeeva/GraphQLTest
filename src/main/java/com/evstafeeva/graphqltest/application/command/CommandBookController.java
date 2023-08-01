package com.evstafeeva.graphqltest.application.command;

import com.evstafeeva.graphqltest.application.api.AuthorInput;
import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CommandBookController {

    private final BookService bookService;

    @MutationMapping
    public Book saveBook(@Argument String title,
                         @Argument List<AuthorInput> authors) {
        return bookService.saveBook(title, authors);
    }

    @MutationMapping
    public Author saveAuthor(@Argument String name,
                             @Argument List<BookInput> books) {
        return bookService.saveAuthor(name, books);
    }
}
