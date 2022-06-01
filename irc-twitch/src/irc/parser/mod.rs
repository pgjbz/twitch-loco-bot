use fancy_regex::Regex;

use super::{IrcResult, IrcType};

pub(super) struct Parser;

impl Parser {
    pub(super) fn parse(&self, input: String) -> IrcResult {
        let irc_type = self.extract_event(&input);
        let _channel = self.extract_channel(&input);
        let _nickname = self.extract_nickname(&input, &irc_type);
        todo!()
    }

    fn extract_event(&self, input: &str) -> IrcType {
        let regex = Regex::new("((?<=\\s)([A-Z]+))|(([A-Z]+)(?=\\s))").unwrap();
        self.match_regex_string(regex, &input).into()
    }

    fn extract_channel(&self, input: &str) -> String {
        let regex = Regex::new("(?<=\\s#)(\\w+)").unwrap();
        self.match_regex_string(regex, input)
    }

    fn extract_nickname(&self, input: &str, irc_type: &IrcType) -> String {
        let regex = irc_type.display_name();
        self.match_regex_string(regex, input)
    }

    fn match_regex_string(&self, regex: Regex, input: &str) -> String {
        match regex.captures(&input) {
            Ok(Some(cap)) => match cap.get(0) {
                Some(m) => m.as_str().into(),
                None => "none".into(),
            },
            _ => "none".into(),
        }
    }
}

//TODO: WRITE A FUCKING TEST MOTHERFUCKER
