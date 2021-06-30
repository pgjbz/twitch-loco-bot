create table if not exists users (
	username text primary key,
	join_date timestamp
);

create table if not exists messages (
	id bigserial primary key,
	username text references users(username),
	message text not null,
	channel text not null,
	message_date timestamp
);