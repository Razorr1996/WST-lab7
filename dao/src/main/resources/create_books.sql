CREATE TABLE "books"
(
  id          bigserial              NOT NULL,
  name        character varying(200) not null,
  author      character varying(200),
  public_date TIMESTAMP,
  isbn        character varying(200),
  CONSTRAINT "Books_pkey" PRIMARY KEY (id)
);

insert into books(name, author, public_date, isbn)
values ('Harry Potter and the philosopher''s stone', 'Rowling, J. K.', '1997-06-27', '0747532699');

insert into books(name, author, public_date, isbn)
values ('book1', 'author1', '2019-03-01', '1');
insert into books(name, author, public_date, isbn)
values ('book2', 'author1', '2019-03-01', '2');
insert into books(name, author, public_date, isbn)
values ('book3', 'author1', '2019-03-02', '3');

insert into books(name, author, public_date, isbn)
values ('book4', 'author2', '2019-03-02', '4');
insert into books(name, author, public_date, isbn)
values ('book5', 'author2', '2019-03-03', '5');
insert into books(name, author, public_date, isbn)
values ('book6', 'author2', '2019-03-04', '6');
