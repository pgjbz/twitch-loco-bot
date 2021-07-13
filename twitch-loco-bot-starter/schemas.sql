drop table if exists messages;
drop table if exists tokens;
drop table if exists irc_event;
drop table if exists users;
drop table if exists bots;
drop table if exists jokes;
drop table if exists teasers;
drop table if exists steals;


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

create table if not exists jokes (
	id bigserial primary key,
	joke text not null
);

insert into jokes(joke)
values ('Eu morava numa ilha e me mudei para outra. Isso não foi um trocadilho, mas uma trocadilha.'),
('Quando eu tiver uma ovelha, vou chamá-la de Rover. Assim, quando eu tosá-la, poderei dizer que tenho lã de Rover.'),
('Por que o lagarto deixou seu filho de castigo? Porque ele réptil de ano.'),
('Sabe por que sempre convidavam o Napoleão pras festinhas? Porque ele era Bom na Party.'),
('Você sabe qual é o contrário de volátil? Vem cá, sobrinho.'),
('Sabe por que o pinheiro não se perde na floresta? Porque ele tem uma pinha.'),
('Uma pessoa passou dois dias na piscina e depois foi ao médico. Sabe qual foi o diagnóstico do médico? Nada demais.'),
('Por que não é bom guardar kibe no freezer? Porque lá dentro ele esfirra.'),
('Por que na Argentina as vacas não param de olhar pro céu? Porque lá tem boi nos ares.'),
('O músico foi à farmácia e pediu ao farmacêutico um lá menor. O farmacêutico disse que não tinha. Então o músico pediu um ré médio.'),
('Se chovesse achocolatado, a gente ia se molhar Toddynho.'),
('Sabe qual o nome do intestino do boi em espanhol? Intestino del gado.'),
('Por que a formiga tem quatro patas? Porque se ela tivesse cinco se chamaria fivemiga.'),
('Qual é o antônimo de simpatia? Nãopotio.'),
('Para que servem óculos verdes? Para ver de perto.'),
('Sabe qual o nome do homem que está montando o presépio de Natal? Armando Nascimento de Jesus.'),
('Sabe qual é o cereal favorito do vampiro? Aveia.'),
('Eu tinha um pintinho chamado Relam. Quando chovia, Relam piava.'),
('O que um instrutor de direção foi fazer no forró? Foi ensinar o Frank Aguiar.'),
('Sabe por que o Pedro toma o suco devagar? Porque é mais suculento.');

create table if not exists steals (
	id bigserial primary key,
	steal text not null
);

insert into steals(steal)
values ('@${touser} roubou a calcinha de @${target}'),
('@${touser} roubou o sapato de @${target}'),
('@${touser} roubou a cueca de @${target}'),
('@${touser} roubou o penico da casa de @${target}'),
('@${touser} roubou a carteira de @${target}'),
('@${touser} roubou um beijo de @${target}'),
('@${touser} roubou o nariz de @${target}');

create table if not exists flirts (
	id bigserial primary key,
	flirt text not null
);


insert into flirts (flirt)
values ('@${target} não sou carro, mas sou Para ti.'),
('@${target} eu não sou a Casas Bahia, mas prometo dedicação total a você.'),
('@${target} estou fazendo uma campanha de doação de órgãos! Então, não quer doar seu coração pra mim?'),
('@${target} você não é pescoço mais mexeu com a minha cabeça!'),
('@${target} eu perdi o número do meu telefone… Me empresta o seu?'),
('@${target} me chama de capeta e deixa te possuir'),
('@${target} você é o ovo que faltava na minha marmita.'),
('@${target} me chama de tabela periódica e diz que rola uma química entre nós.'),
('@${target} você é sempre gostosa(o) assim ou hoje tá fantasiada(o) de lasanha?'),
('@${target} você foi feita com velas, mel, fitinhas vermelhas e rosas? Porque te achei uma simpatia.'),
('@${target} que roupa feia, Tira isso agora!'),
('@${target} seu pai é dono da Tam?. Como é que faz pra ter um avião desse em casa?'),
('@${target} você é o doce de leite Que falta no meu churros'),
('@${target} você deve comer Photoshop no café da manhã! Pra ser tão linda(o) assim'),
('@${target} eu não sou manteiga de cacau, Mas adoraria amaciar seus lábios'),
('@${target} nome e Arnaldo mais pode me chamar de Naldo, pois quando eu te vi perdi o ar'),
('@${target} você é tão linda(o) que não caga, lança bombom');