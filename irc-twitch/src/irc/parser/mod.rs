use std::collections::HashMap;

use super::{Irc, IrcType};

pub(super) struct MessageParser;

impl MessageParser {
    pub(super) fn parse(input: String) -> Result<Irc, String> {
        if !input.starts_with("@badge-info") || input.is_empty() {
            return Err("invalid string".into());
        }
        let mut splited = input.split(" :");
        let keys_side = splited.next().unwrap();
        let infos = splited.next().unwrap();
        let message = splited.next().unwrap();
        let mut keys = HashMap::<String, String>::default();
        for pair in keys_side.split(';') {
            let mut pair = pair.split('=');
            let key = pair.next().unwrap();
            let value = pair.next().unwrap();
            keys.insert(key.into(), value.into());
        }
        let mut infos = infos.split('@');
        let nickname = infos.next().unwrap().split('!').next().unwrap();
        let channel = infos.next().unwrap().split('#').next().unwrap();
        Ok(Irc::new(
            IrcType::Message,
            Some(nickname.into()),
            Some(keys),
            channel.into(),
            Some(message.split("\r\n").next().unwrap().into()),
        ))
    }
}
