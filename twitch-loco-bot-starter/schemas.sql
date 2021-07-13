drop table if exists messages;
drop table if exists tokens;
drop table if exists irc_event;
drop table if exists users;
drop table if exists bots;


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
values('streamlabs'),
('moobot'),
('streamelements'),
('nightbot'),
('streamholics'),
('discord_for_streamers'),
('bot97loco');

create table if not exists irc_event (
	id bigserial primary key,
	username text references users(username) not null,
	channel text not null,
	unknown_event_string text,
	event_receive text not null,
	keys text,
	event_date timestamp not null
);