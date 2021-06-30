package com.pgjbz.twitch.loco.service;


import com.pgjbz.twitch.loco.model.StreamInfo;

import java.util.Optional;

public interface StreamInfoService {

    Optional<StreamInfo> getStreamInfo(String channel);

}
