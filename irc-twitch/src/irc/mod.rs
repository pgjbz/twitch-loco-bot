use std::{
    collections::HashMap,
    io::{Read, Result as IOResult, Write},
    net::TcpStream,
};

use self::parser::MessageParser;

mod parser;

const IRC_PORT: u16 = 6667;
const IRC_URL: &str = "irc.chat.twitch.tv";

pub enum Command {
    Pass,
    Nick,
    Join,
    Privmsg,
}

impl Command {
    pub fn build(&self, arg: String, connection: &LocoConnection) -> String {
        let prefix = match self {
            Self::Pass => "PASS oauth:".into(),
            Self::Nick => "NICK ".into(),
            Self::Join => "JOIN #".into(),
            Self::Privmsg => format!("PRIVMSG #{} :", connection.channel),
        };
        format!("{}{}\r\n", prefix, &arg)
    }
}

pub struct LocoConnection {
    connection: Option<TcpStream>,
    _nickname: String,
    channel: String,
}

pub struct LocoConfig {
    oauth: String,
    nickname: String,
    channel_to_join: String,
}

#[derive(Debug)]
pub struct Irc {
    pub irc_type: IrcType,
    pub nickname: Option<String>,
    pub keys: Option<HashMap<String, String>>,
    pub channel: String,
    pub message: Option<String>,
}

impl Irc {
    pub fn new(
        irc_type: IrcType,
        nickname: Option<String>,
        keys: Option<HashMap<String, String>>,
        channel: String,
        message: Option<String>,
    ) -> Self {
        Self {
            irc_type,
            nickname,
            keys,
            channel,
            message,
        }
    }
}

#[derive(Debug)]
pub enum IrcType {
    Message,
    Event,
}

impl LocoConfig {
    pub fn new(oauth: String, nickname: String, channel_to_join: String) -> Self {
        Self {
            oauth,
            nickname,
            channel_to_join,
        }
    }
}

impl LocoConnection {
    pub fn new(loco_config: LocoConfig) -> IOResult<LocoConnection> {
        let connection = TcpStream::connect(&format!("{}:{}", IRC_URL, IRC_PORT))?;
        let mut loco_connection = LocoConnection {
            connection: Some(connection),
            _nickname: loco_config.nickname.clone(),
            channel: loco_config.channel_to_join.clone(),
        };
        loco_connection.batch_command(&[
            Command::Pass.build(loco_config.oauth.clone(), &loco_connection),
            Command::Nick.build(loco_config.nickname.clone(), &loco_connection),
            Command::Join.build(loco_config.channel_to_join, &loco_connection),
            "CAP REQ :twitch.tv/commands\r\n".into(),
            "CAP REQ :twitch.tv/membership\r\n".into(),
            "CAP REQ :twitch.tv/tags\r\n".into(),
        ])?;
        Ok(loco_connection)
    }

    fn batch_command(&mut self, vec: &[String]) -> IOResult<()> {
        let map = vec.iter().flat_map(|val| val.bytes()).collect::<Vec<u8>>();
        if let Some(connection) = &mut self.connection {
            connection.write_all(&map)?;
            connection.flush()?;
        }
        Ok(())
    }

    pub fn send_command(&mut self, command: Command, arg: &str) -> IOResult<()> {
        let command = command.build(arg.into(), self);
        if let Some(connection) = &mut self.connection {
            connection.write_all(command.as_bytes())?;
            connection.flush()?;
        }
        Ok(())
    }

    //TODO: greceful shutdown
    pub fn read(&mut self, exec: impl Fn(Irc)) {
        if let Some(connection) = &mut self.connection {
            loop {
                let mut buf = [0; 1024];
                connection.read_exact(&mut buf).unwrap();
                if let Ok(msg) = String::from_utf8(Vec::from(buf)) {
                    //for now only process chat message
                    if !msg.starts_with("@badge") || !msg.contains("PRIVMSG") { 
                        continue;
                    }
                    if let Ok(parsed) = MessageParser::parse(msg) {
                        exec(parsed);
                    }
                }
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn build_commands() {
        let fake_conn = LocoConnection {
            connection: None,
            _nickname: "test".into(),
            channel: "test".into(),
        };
        let inputs = [
            (Command::Join, "test", "JOIN #test\r\n"),
            (Command::Nick, "test", "NICK test\r\n"),
            (Command::Privmsg, "test", "PRIVMSG #test :test\r\n"),
            (Command::Pass, "test", "PASS oauth:test\r\n"),
        ];

        for (command, param, expected) in inputs {
            assert_eq!(expected, command.build(param.into(), &fake_conn))
        }
    }
}
