package com.umc.miner.src.playmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayMapService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayMapDao playMapDao;

    @Autowired
    public PlayMapService(PlayMapDao playMapDao) {
        this.playMapDao = playMapDao;
    }
}
