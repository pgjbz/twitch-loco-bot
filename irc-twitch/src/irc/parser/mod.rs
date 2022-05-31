use std::collections::HashMap;

use super::{Irc, IrcResult, IrcType, Parser};

#[derive(Default)]
pub(super) struct MessageParser;
#[derive(Default)]
pub(super) struct EventParser;

impl Parser for MessageParser {
    fn parse(&self, input: String) -> IrcResult {
        println!("message parser");
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

impl Parser for EventParser {
    fn parse(&self, input: String) -> IrcResult {
        println!("event parsetr");
        if !input.contains("JOIN") || !input.contains("PART") {
            return Err(format!("cannot parse message '{}' yet", input));
        }
        EventParser::parse_join_part(&input)
    }
}

impl EventParser {
    fn parse_join_part(input: &str) -> IrcResult {
        let mut splited = input.split_whitespace();
        let left = splited.next().unwrap();
        let event = splited.next().unwrap();
        let channel = splited.next().unwrap().replace('#', "");
        let irc_type = match event {
            "JOIN" => IrcType::Join,
            "PART" => IrcType::Part,
            _ => return Err("event not supported yet".into()),
        };
        let nickname = left.split('!').next().unwrap().replace(':', "");
        Ok(Irc::new(irc_type, Some(nickname), None, channel, None))
    }
}
//TODO: WRITE A FUCKING TEST MOTHERFUCKER
