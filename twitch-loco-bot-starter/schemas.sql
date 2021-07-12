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

create table if not exists tokens (
	username text references users(username),
	channel text,
	unit bigint,
	constraint tokens_pkay primary key (username, channel)
);

create table if not exists bots (
    id bigserial primary key,
    bot_name text not null
);

insert
	into
	bots(bot_name)
values('nihghtbot'),
('moobot'),
('streamelements'),
('bot97loco');