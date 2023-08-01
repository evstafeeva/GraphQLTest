package com.evstafeeva.graphqltest.repository;

import com.evstafeeva.graphqltest.domain.Author;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
    List<Author> findAuthorsByName(String name);

}
