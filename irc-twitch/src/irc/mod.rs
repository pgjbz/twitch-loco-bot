use std::{
    collections::HashMap,
    io::{Read, Result as IOResult, Write},
    net::TcpStream,
    thread,
    time::Duration,
};

use fancy_regex::Regex;

use self::parser::Parser;

mod parser;

const IRC_PORT: u16 = 6667;
const IRC_URL: &str = "irc.chat.twitch.tv";

#[derive(Debug)]
pub enum IrcError {
    Timeout,
    Host(String),
    MaxAttemps,
    Permission,
    Aborted,
    Unknown,
}

pub type IrcResult = Result<Irc, IrcError>;

impl From<std::io::Error> for IrcError {
    fn from(err: std::io::Error) -> Self {
        use std::io::ErrorKind;
        match err.kind() {
            ErrorKind::ConnectionReset => Self::Host("connection reset by peer".into()),
            ErrorKind::ConnectionRefused => Self::Host("connection refused by host".into()),
            ErrorKind::NotFound => Self::Host("unknown host".into()),
            ErrorKind::PermissionDenied => Self::Permission,
            ErrorKind::ConnectionAborted => Self::Aborted,
            ErrorKind::BrokenPipe => Self::Host("broken pipe".into()),
            _ => Self::Unknown,
        }
    }
}

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
            Self::Privmsg => format!("PRIVMSG #{} :", connection.config.channel_to_join.clone()),
        };
        format!("{}{}\r\n", prefix, &arg)
    }
}

pub struct LocoConnection {
    connection: Option<TcpStream>,
    config: LocoConfig,
}

#[derive(Clone)]
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
    Join,
    Part,
    Usernotice,
    CleanChat,
    Pong,
    Ping,
    UserState,
    Notice,
    Unknown,
}

impl IrcType {
    fn display_name(&self) -> Regex {
        let expr = match self {
            Self::Message => r"(?<=:)(\w+)(?=!)",
            Self::Join => r"(?<=:)(\w+)(?=!)",
            Self::Part => r"(?<=:)(\w+)(?=!)",
            Self::Usernotice => r"(?<=display-name=)([\w]+)",
            Self::CleanChat => r"(?<=:)([\w]+)(?!.)",
            Self::Pong => r"TODOU",
            Self::Ping => r"TODOU",
            Self::UserState => r"(?<=display-name=)([\w]+)",
            Self::Notice => r"TODOU",
            _ => r"TODOU",
        };
        Regex::new(expr).unwrap()
    }
}

impl From<String> for IrcType {
    fn from(value: String) -> Self {
        match &value[..] {
            "PRIVMSG" => Self::Message,
            "JOIN" => Self::Join,
            "PART" => Self::Part,
            "USERNOTICE" => Self::Usernotice,
            "CLEARCHAT" => Self::CleanChat,
            "PING" => Self::Ping,
            "PONG" => Self::Pong,
            "NOTICE" => Self::Notice,
            _ => Self::Unknown,
        }
    }
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
    pub fn new(loco_config: LocoConfig) -> Result<LocoConnection, IrcError> {
        LocoConnection::try_connect(loco_config)
    }

    fn try_connect(loco_config: LocoConfig) -> Result<LocoConnection, IrcError> {
        const MAX_ATTEMPS: usize = 3;
        for attemp in 1..=MAX_ATTEMPS {
            println!("connection attemp {}", attemp);
            match TcpStream::connect(&format!("{}:{}", IRC_URL, IRC_PORT)) {
                Ok(connection) => {
                    let mut loco_connection = LocoConnection {
                        connection: Some(connection),
                        config: loco_config.clone(),
                    };
                    loco_connection.batch_command(&[
                        Command::Pass.build(loco_config.oauth.clone(), &loco_connection),
                        Command::Nick.build(loco_config.nickname.clone(), &loco_connection),
                        Command::Join.build(loco_config.channel_to_join, &loco_connection),
                        "CAP REQ :twitch.tv/commands\r\n".into(),
                        "CAP REQ :twitch.tv/membership\r\n".into(),
                        "CAP REQ :twitch.tv/tags\r\n".into(),
                    ])?;
                    return Ok(loco_connection);
                }
                _ => {
                    if attemp == MAX_ATTEMPS {
                        return Err(IrcError::MaxAttemps);
                    }
                    thread::sleep(Duration::from_secs((1_u64).pow(attemp as u32)))
                }
            }
        }
        Err(IrcError::Unknown)
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
        for irc in self {
            exec(irc)
        }
    }
}

impl Iterator for LocoConnection {
    type Item = Irc;

    fn next(&mut self) -> Option<Self::Item> {
        let mut irc: Option<Self::Item> = None;
        loop {
            if let Some(connection) = &mut self.connection {
                let mut buf = [0; 1024];
                match connection.read(&mut buf) {
                    Ok(_) => {
                        if let Ok(msg) = String::from_utf8(Vec::from(buf)) {
                            if let Ok(value) = Parser.parse(msg) {
                                irc = Some(value);
                                break;
                            }
                        }
                    }
                    Err(err) => match IrcError::from(err) {
                        IrcError::Aborted | IrcError::Host(_) => {
                            match Self::new(self.config.clone()) {
                                Ok(con) => self.connection = con.connection,
                                Err(err) => {
                                    eprintln!("{:?}", err);
                                    continue;
                                }
                            }
                        }
                        err => {
                            eprintln!("{:?}", err);
                            break;
                        }
                    },
                }
            }
        }
        irc
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn build_commands() {
        let fake_conn = LocoConnection {
            connection: None,
            config: LocoConfig {
                oauth: "test".into(),
                nickname: "test".into(),
                channel_to_join: "test".into(),
            },
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
