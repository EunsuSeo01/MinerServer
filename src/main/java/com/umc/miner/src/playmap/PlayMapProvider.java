package com.umc.miner.src.playmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayMapProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayMapDao playMapDao;

    @Autowired
    public PlayMapProvider(PlayMapDao playMapDao) {
        this.playMapDao = playMapDao;
    }
}
