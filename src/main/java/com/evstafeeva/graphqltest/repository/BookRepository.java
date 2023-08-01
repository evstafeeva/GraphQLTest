package com.evstafeeva.graphqltest.repository;

import com.evstafeeva.graphqltest.domain.Book;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findByAuthors_Name(String authorName);

}
