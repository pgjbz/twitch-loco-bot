package com.pgjbz.twitch.loco.service.impl;

import com.pgjbz.twitch.loco.model.StreamInfo;
import com.pgjbz.twitch.loco.service.StreamInfoService;
import com.pgjbz.twitch.loco.util.DeserializeJsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

        log.info("Preparing http request");

        String httpBodyResponse = Strings.EMPTY;
        HttpClient httpClient= HttpClient.newBuilder()
                .proxy(ProxySelector.getDefault())
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(TWITCH_CHATTERS_URL.replace("${channel}", channel)))
                .build();
        try {
            log.info("Performing http request");
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            httpBodyResponse = httpResponse.body();
        } catch (Exception e) {
            log.error("Error on send http request", e);
        }
        return httpBodyResponse;
    }

}
