use std::env;

use loco_twitch::irc::{Command, IrcType, LocoConfig, LocoConnection};

fn main() {
    dotenv::dotenv().ok();

    let oauth = env::var("TWITCH_OAUTH").expect("oauth key is required");
    let nickname = env::var("TWITCH_NICK").expect("nickname is required");
    let channel_to_join = env::var("TWITCH_CHANNEL_JOIN").expect("channel to join is required");

    let loco_config = LocoConfig::new(oauth, nickname, channel_to_join);
    let mut loco_connection = LocoConnection::new(loco_config).unwrap();

    while let Some(item) = loco_connection.next() {
        match item.irc_type {
            IrcType::Usernotice => {
                let msg = format!("{} é uma pessoa linda", item.nickname.unwrap());
                println!("{}", msg);
                loco_connection
                    .send_command(Command::Privmsg, &msg)
                    .unwrap();
            }
            IrcType::Message => println!(
                "{}@{}: {}",
                item.nickname.unwrap(),
                item.channel,
                item.message.unwrap()
            ),
            IrcType::Ping => {
                loco_connection.send_command(Command::Pong, "").unwrap();
                println!("pong");
            }
            IrcType::Join => println!(
                "{} entrou no canal {}",
                item.nickname.unwrap(),
                item.channel
            ),
            IrcType::Part => loco_connection
                .send_command(
                    Command::Privmsg,
                    &format!("{} por que você se foi? BibleThump", item.nickname.unwrap()),
                )
                .unwrap(),

            _ => println!(
                "{:?} -> {:?}@{:?}",
                item.irc_type, item.nickname, item.channel
            ),
        }
    }
}
