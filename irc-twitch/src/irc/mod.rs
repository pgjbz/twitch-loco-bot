const IRC_URL: &str = "irc.chat.twitch.tv";
const IRC_PORT: u16 = 6667;

use std::{
    io::{Read, Result as IOResult, Write},
    net::TcpStream,
};

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

pub struct LocoConfig {
    oauth: String,
    nickname: String,
    channel_to_join: String,
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

pub struct LocoConnection {
    connection: Option<TcpStream>,
    _nickname: String,
    channel: String,
}

impl LocoConnection {
    pub fn new(loco_config: LocoConfig) -> IOResult<LocoConnection> {
        let connection = TcpStream::connect(&format!("{}:{}", IRC_URL, IRC_PORT))?;
        let mut loco_connection = LocoConnection {
            connection: Some(connection),
            _nickname: loco_config.nickname.clone(),
            channel: loco_config.channel_to_join.clone(),
        };
        loco_connection.buffer_commands(&[
            Command::Pass.build(loco_config.oauth.clone(), &loco_connection),
            Command::Nick.build(loco_config.nickname.clone(), &loco_connection),
            Command::Join.build(loco_config.channel_to_join, &loco_connection),
            "CAP REQ :twitch.tv/commands\r\n".into(),
            "CAP REQ :twitch.tv/membership\r\n".into(),
            "CAP REQ :twitch.tv/tags\r\n".into(),
        ])?;
        Ok(loco_connection)
    }

    fn buffer_commands(&mut self, vec: &[String]) -> IOResult<()> {
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
            connection.write(command.as_bytes())?;
            connection.flush()?;
        }
        Ok(())
    }

    //TODO: greceful shutdown
    pub fn read(&mut self, exec: impl Fn(String)) {
        if let Some(connection) = &mut self.connection {
            loop {
                let mut buf = [0; 1024];
                connection.read(&mut buf).unwrap();
                exec(String::from_utf8(Vec::from(buf)).unwrap());
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
            (Command::Pass, "test", "PASS oauth:test\r\n")
        ];

        for (command, param, expected) in inputs {
            assert_eq!(expected, command.build(param.into(), &fake_conn))
        }
    }
}
