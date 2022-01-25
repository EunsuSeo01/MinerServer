package com.umc.miner.src.playmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayMapController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayMapProvider playMapProvider;

    @Autowired
    public PlayMapController(PlayMapProvider playMapProvider) {
        this.playMapProvider = playMapProvider;
    }
}
