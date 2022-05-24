
use std::{io::Result as IOResult, env};

use irc_twitch::irc::{LocoConfig, LocoConnection};

fn main() -> IOResult<()> {

    dotenv::dotenv().ok();

    let oauth = env::var("TWITCH_OAUTH").expect("oauth key is required");
    let nickname = env::var("TWITCH_NICK").expect("nickname is required");
    let channel_to_join = env::var("TWITCH_CHANNEL_JOIN").expect("channel to join is required");
    
    let loco_config = LocoConfig::new(
        oauth,
        nickname,
        channel_to_join,
    );
    let mut loco_connection = LocoConnection::new(loco_config)?;
    loco_connection.read(|msg| println!("{}", msg));
    Ok(())
    
}