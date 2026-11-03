create table if not exists features
(
    feature_id integer generated always as identity,
    feature    varchar(30) not null,
    primary key (feature_id),
    constraint unique_feature
        unique (feature)
);

alter table features
    owner to postgres;

create table if not exists customers
(
    cpf           varchar(11) not null,
    name          varchar(50) not null,
    email         varchar(50) not null,
    phone         varchar(11) not null,
    active_rental integer,
    primary key (cpf),
    constraint unique_email
        unique (email),
    constraint unique_phone
        unique (phone)
);

alter table customers
    owner to postgres;

create table if not exists rentals
(
    rental_id    integer generated always as identity (start with 100),
    customer_cpf varchar(11)               not null,
    initial_date date default CURRENT_DATE not null,
    return_date  date                      not null,
    total_days   integer generated always as (((return_date - initial_date) + 1)) stored,
    total_price  numeric(15, 2)            not null,
    status       varchar(30),
    primary key (rental_id),
    constraint customer_fk
        foreign key (customer_cpf) references customers,
    constraint valid_rental_period
        check (return_date >= initial_date),
    constraint valid_status
        check ((status)::text = ANY
               ((ARRAY ['ACTIVE'::character varying, 'RESERVED'::character varying, 'COMPLETED'::character varying, 'CANCELED'::character varying])::text[]))
);

alter table rentals
    owner to postgres;

alter table customers
    add constraint customer_active_rental_fk
        foreign key (active_rental) references rentals;

create table if not exists cars
(
    plate         varchar(6)     not null,
    brand         varchar(20)    not null,
    model         varchar(20)    not null,
    daily_rate    numeric(10, 2) not null,
    active_rental integer,
    primary key (plate),
    constraint car_active_rental_fk
        foreign key (active_rental) references rentals
);

alter table cars
    owner to postgres;

create table if not exists car_features
(
    car_plate  varchar(6) not null,
    feature_id integer    not null,
    constraint composite_primary_key
        primary key (car_plate, feature_id),
    constraint car_fk
        foreign key (car_plate) references cars,
    constraint feature_fk
        foreign key (feature_id) references features
);

alter table car_features
    owner to postgres;

create table if not exists rental_cars
(
    rental_id integer    not null,
    car_plate varchar(6) not null,
    constraint composite_pk
        primary key (rental_id, car_plate),
    constraint rental_fk
        foreign key (rental_id) references rentals,
    constraint car_fk
        foreign key (car_plate) references cars
);

alter table rental_cars
    owner to postgres;

