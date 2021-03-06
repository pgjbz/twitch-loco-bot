package com.pgjbz.twitch.loco.service.impl;

import com.pgjbz.twitch.loco.enums.HttpMethod;
import com.pgjbz.twitch.loco.model.StreamInfo;
import com.pgjbz.twitch.loco.service.StreamInfoService;
import com.pgjbz.twitch.loco.util.DeserializeJsonUtil;
import com.pgjbz.twitch.loco.util.HttpUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.util.Optional;

import static com.pgjbz.twitch.loco.constant.TwitchConstants.TWITCH_CHATTERS_URL;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
public class StreamInfoServiceImpl implements StreamInfoService {

    @Override
    public Optional<StreamInfo> getStreamInfo(String channel) {
        try {
            String responseBody = performHttpRequest(channel);
            if(isNotBlank(responseBody))
                return ofNullable(DeserializeJsonUtil.deserialize(responseBody, StreamInfo.class));
            log.info("Empty body return");
        } catch (Exception e) {
            log.info("Error on deserialize http response", e);
        }
        return Optional.empty();
    }

    private String performHttpRequest(String channel) {
        try {
            log.info("Performing http request to get chatters for channel {}", channel);
            return HttpUtil.execute(HttpMethod.GET, TWITCH_CHATTERS_URL.replace("${channel}", channel));
        } catch (Exception e) {
            log.error("Error on performing http request to get chatters for channel {}", channel, e);
        }
        return Strings.EMPTY;
    }

}
