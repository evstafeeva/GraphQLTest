package com.evstafeeva.graphqltest.repository;

import com.evstafeeva.graphqltest.domain.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, String> {
    Author findAuthorByName(String name);

}
