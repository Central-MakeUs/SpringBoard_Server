package com.springboard.zzatmari.src.timer;

import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimerService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TimerDao timerDao;
    private final TimerProvider timerProvider;
    private final JwtService jwtService;


    @Autowired
    public TimerService(TimerDao timerDao, TimerProvider timerProvider, JwtService jwtService) {
        this.timerDao = timerDao;
        this.timerProvider = timerProvider;
        this.jwtService = jwtService;

    }
}