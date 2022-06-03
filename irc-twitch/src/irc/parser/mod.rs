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
        let message = if let IrcType::Message = irc_type {
            self.extract_message(&input)
        } else {
            None
        };
        Ok(Irc::new(irc_type, nickname, None, channel, message))
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
            Ok(Some(cap)) => match cap.get(idx) {
                Some(m) => Some(m.as_str().into()),
                None => None,
            },
            _ => None,
        }
    }
}

//TODO: WRITE A FUCKING TEST MOTHERFUCKER
