use std::{
    io::{Read, Result as IOResult, Write},
    net::TcpStream,
};

struct LocoConnection {
    connection: TcpStream,
    nickname: String,
    channel: String,
}

fn main() -> IOResult<()> {
    let mut loco_connection =
        LocoConnection::new("irc.chat.twitch.tv", 6667, "paulo97loco", "kingvenom", "")?;
    loco_connection.send_command("PRIVMSG #", "kingvenom : message from Rust application\r\n")?;
    loco_connection.read(|msg| println!("{}", msg))?;
    Ok(())
}

impl LocoConnection {
    fn new(
        url: &str,
        port: u16,
        nickname: &str,
        channel: &str,
        oauth: &str,
    ) -> IOResult<LocoConnection> {
        let connection = TcpStream::connect(&format!("{}:{}", url, port))?;
        let mut loco_connection = LocoConnection {
            connection,
            nickname: nickname.into(),
            channel: channel.into(),
        };
        loco_connection.send_command("PASS ", &format!("oauth:{}", oauth))?;
        loco_connection.send_command("NICK ", nickname)?;
        loco_connection.send_command("JOIN #", channel)?;
        Ok(loco_connection)
    }

    fn send_command(&mut self, command: &str, arg: &str) -> IOResult<()> {
        self.connection
            .write(format!("{}{}\r\n", command, arg).as_bytes())?;
        self.connection.flush()
    }

    //TODO: greceful shutdown
    fn read(&mut self, exec: impl Fn(String)) -> IOResult<()> {
        let mut buf = [0; 1024];
        loop {
            self.connection.read(&mut buf)?;
            exec(String::from_utf8(Vec::from(buf)).unwrap());
        }
        Ok(())
    }
}
