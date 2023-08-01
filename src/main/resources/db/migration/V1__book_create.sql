CREATE TABLE public.Book
(
    book_id serial not null primary key,
    title   varchar not null
);

CREATE TABLE public.Author
(
    author_id serial not null primary key,
    name      varchar not null
);

CREATE TABLE public.Author_to_Book
(
    id        serial primary key,
    author_id int not null,
    book_id   int not null
);

ALTER TABLE public.Author_to_Book
    ADD FOREIGN KEY (author_id) REFERENCES public.Author (author_id);

ALTER TABLE public.Author_to_Book
    ADD FOREIGN KEY (book_id) REFERENCES public.Book (book_id);