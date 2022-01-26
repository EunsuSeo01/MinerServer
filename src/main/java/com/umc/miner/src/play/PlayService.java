package com.umc.miner.src.play;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PlayDao playDao;

    @Autowired
    public PlayService(PlayDao playDao) {
        this.playDao = playDao;
    }
}
