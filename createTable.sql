

create database `BookLib` default character set utf8 collate utf8_general_ci;
 
 use booklib;

create table user(
    id nvarchar(40) primary key,
    name nvarchar(40),
    phone char(11),
    passwd nvarchar(32),
    header nvarchar(256)
);

create table type
(
    id nvarchar(40) primary key,
    name nvarchar(40)
);

create table book
(
    id nvarchar(40) primary key,
    userid nvarchar(40),
    typeid nvarchar(40),
    name nvarchar(128),
    descriptor nvarchar(256),
    price int,
    sellsum int,
    storesum int,
    addtime bigint,
    picture nvarchar(256),
    foreign key(userid) references user(id),
    foreign key(typeid) references type(id)
);


create table comment
(
    id nvarchar(40) primary key,
    userid nvarchar(40),
    bookid nvarchar(40),
    writetime bigint,
    comment nvarchar(256),
    foreign key(userid) references user(id),
    foreign key(bookid) references book(id)
);

create table favorite
(
    id nvarchar(40) primary key,
    userid nvarchar(40),
    bookid nvarchar(40),
    foreign key(userid) references user(id),
    foreign key(bookid) references book(id)
);

create table addr(
    id nvarchar(40) primary key,
    userid nvarchar(40),
    address nvarchar(128),
    pname nvarchar(128),
    cityname nvarchar(128),
    adname nvarchar(128),
    communityname nvarchar(64),
    addrdetail nvarchar(128),
    fulladdr nvarchar(128),
    foreign key(userid) references user(id)
);

create table orderbook
(
    id nvarchar(40) primary key,
    userid nvarchar(40),
    addrid nvarchar(40),
    name nvarchar(40),
    phone nvarchar(40),
    ordertime bigint,
    totalprice int,
    foreign key(userid) references user(id),
    foreign key(addrid) references addr(id)
);

create table intentbook
(
    id nvarchar(40) primary key,
    userid nvarchar(40),
    bookid nvarchar(40),
    orderid nvarchar(40),
    count int,
    foreign key(userid) references user(id),
    foreign key(bookid) references book(id),
    foreign key(orderid) references orderbook(id)
);

drop table intentbook;
drop table orderbook;
drop table addr;
drop table favorite;
drop table comment;
drop table book;
drop table type;
drop table user;