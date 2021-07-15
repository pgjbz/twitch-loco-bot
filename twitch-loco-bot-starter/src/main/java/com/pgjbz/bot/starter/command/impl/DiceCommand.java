package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.bot.starter.repository.TokenRepository;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@RequiredArgsConstructor
public class DiceCommand implements StandardCommand {

    private final TokenRepository tokenRepository;
    private final int DICE_NUMBER_POS = 0;
    private final int BET_AMOUNT_POS = 1;
    private final int DICE_FACES = 6;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive dice command {}", chatMessage.toString());
        final String username = chatMessage.getUser();
        int[] bet = extractBet(chatMessage.getMessage());
        if(!chatMessage.getMessage().matches("!dice\\s\\d+\\s\\d+")
                || bet[DICE_NUMBER_POS] > DICE_FACES
                || bet[DICE_NUMBER_POS] < 1) {
            twitchConnection.sendMessage(String.format("@%s please use !dice dice_number[1-6] bet_amount", username));
            return;
        }
        tokenRepository.findByPk(new TokenPk(username, chatMessage.getChannel())).ifPresentOrElse(token ->
            executeTokenWon(token, username, bet, twitchConnection)
        , () -> twitchConnection.sendMessage(String.format("%s you don't have tokens", username)));
    }
    
    private int[] extractBet(String message) {
        int[] values = new int[] {0, 0};
        final Pattern pattern = Pattern.compile("(?<=\\s)(\\d+)");
        Matcher matcher = pattern.matcher(message);
        int pos = -1;
        while(matcher.find() && ++pos < values.length)
            values[pos] = Integer.parseInt(matcher.group());
        return values;
    }

    private void executeTokenWon(Token token, String username, int[] bet, TwitchConnection twitchConnection) {
        final Random random = new Random();
        int rollNumber = random.nextInt(DICE_FACES) + 1;
        int betAmount = bet[BET_AMOUNT_POS];
        if(token.getUnit() < bet[BET_AMOUNT_POS]) {
            twitchConnection.sendMessage(String.format("@%s you don't have enough tokens, you have %s tokens",
                    username,
                    token.getUnit()));
        }
        if(rollNumber == bet[DICE_NUMBER_POS]) {
            int winAmount = betAmount * 5;
            token.addTokenUnit(winAmount);
            tokenRepository.update(token);
            twitchConnection.sendMessage(String.format("@%s you roll %s and won %s tokens", username, rollNumber, winAmount));
        } else {
            token.removeTokenUnit(betAmount);
            tokenRepository.update(token);
            twitchConnection.sendMessage(String.format("@%s you roll %s and lost %s tokens", username, rollNumber, betAmount));
        }
    }


}
