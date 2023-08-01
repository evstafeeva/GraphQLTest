package com.evstafeeva.graphqltest.application.command;

import com.evstafeeva.graphqltest.application.api.AuthorInput;
import com.evstafeeva.graphqltest.application.api.BookInput;
import com.evstafeeva.graphqltest.domain.Author;
import com.evstafeeva.graphqltest.domain.Book;
import com.evstafeeva.graphqltest.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommandBookController {

    private final BookService bookService;

    /**
     * Эндпоинт предназначен для сохранения книг.
     * При этом, если у автора книги задано только имя, то будет считаться, что это новый автор в базе.
     * Если же задан только id, значит такой автор уже имеется в базе.
     *
     * @param title название книги
     * @param authors авторы книги
     * @return книга
     */
    @MutationMapping
    public Book saveBook(@Argument String title,
                         @Argument List<AuthorInput> authors) {
        log.info("Saving the book with title \"{}\", authors {}", title, authors);
        return bookService.saveBook(title, authors);
    }

    /**
     * Эндпоинт предзназначен для сохранения автора.
     * При этом, если у книги данного автора задано только название, то будет считаться, что эта книга новая в базе.
     * Если же задан только id, значит такая книга уже имеется в базе.
     *
     * @param name имя автора
     * @param books книги, написанные автором
     * @return автор
     */
    @MutationMapping
    public Author saveAuthor(@Argument String name,
                             @Argument List<BookInput> books) {
        log.info("Saving the author with name \"{}\", books {}", name, books);
        return bookService.saveAuthor(name, books);
    }
}
