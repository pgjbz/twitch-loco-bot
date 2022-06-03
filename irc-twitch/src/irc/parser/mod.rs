use std::collections::HashMap;

use fancy_regex::Regex;

use super::{Irc, IrcResult, IrcType};

pub(super) struct Parser;

const DEFAULT_NONE: &str = "none";

impl Parser {
    pub(super) fn parse(&self, input: String) -> IrcResult {
        let irc_type = self.extract_event(&input);
        let channel = if let Some(channel) = self.extract_channel(&input) {
            channel
        } else {
            DEFAULT_NONE.into()
        };
        let nickname = self.extract_nickname(&input, &irc_type);
        let (message, keys) = if let IrcType::Message = irc_type {
            let message = self.extract_message(&input);
            let keys = self.extract_keys(&input);
            (message, keys)
        } else {
            (None, None)
        };
        Ok(Irc::new(irc_type, nickname, keys, channel, message))
    }

    fn extract_event(&self, input: &str) -> IrcType {
        let regex = Regex::new(r"((?<=\s)([A-Z]+))|(([A-Z]+)(?=\s))").unwrap();
        if let Some(value) = self.match_regex_string(regex, input, 0) {
            value.into()
        } else {
            IrcType::Unknown
        }
    }

    fn extract_channel(&self, input: &str) -> Option<String> {
        let regex = Regex::new(r"(?<=\s#)(\w+)").unwrap();
        self.match_regex_string(regex, input, 0)
    }

    fn extract_nickname(&self, input: &str, irc_type: &IrcType) -> Option<String> {
        let regex = irc_type.display_name();
        self.match_regex_string(regex, input, 0)
    }

    fn extract_message(&self, input: &str) -> Option<String> {
        let regex = Regex::new(r"(?<=PRIVMSG\s#)(.+?)(?<=\w\s:)(.+)").unwrap();
        self.match_regex_string(regex, input, 2)
    }

    fn match_regex_string(&self, regex: Regex, input: &str, idx: usize) -> Option<String> {
        match regex.captures(input) {
            Ok(Some(cap)) => cap.get(idx).map(|m| m.as_str().into()),
            _ => None,
        }
    }

    fn extract_keys(&self, input: &str) -> Option<HashMap<String, String>> {
        let key_pair = input.split(" :").next().unwrap();
        let mut key_value = HashMap::new();
        for key_pair in key_pair.split(';') {
            let mut key_value_iter = key_pair.split('=');
            let key = key_value_iter.next().unwrap();
            let value = key_value_iter.next().unwrap();
            key_value.insert(key.into(), value.into());
        }
        Some(key_value)
    }
}

//TODO: WRITE A FUCKING TEST MOTHERFUCKER
