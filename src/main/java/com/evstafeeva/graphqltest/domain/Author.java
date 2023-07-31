package com.evstafeeva.graphqltest.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Author")
public class Author {
    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_generator")
    @SequenceGenerator(name = "author_generator", sequenceName = "author_author_id_seq", allocationSize = 1)
    private int id;
    @Column(name = "name")
    private String name;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "Author_to_Book",
        joinColumns = { @JoinColumn(name = "author_id") },
        inverseJoinColumns = { @JoinColumn(name = "book_id") }
    )
    private List<Book> books;

    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String name, List<Book> books) {
        this.name = name;
        this.books = books;
    }

    public Author(String name) {
        this.name = name;
    }
}