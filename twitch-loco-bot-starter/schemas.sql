create table users (
	username text unique not null,
	id bigserial primary key,
	join_date timestamp
);