package com.springboard.zzatmari.src.stat;

import com.springboard.zzatmari.src.user.UserDao;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatProvider {

    private final StatDao statDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StatProvider(StatDao statDao, JwtService jwtService) {
        this.statDao = statDao;
        this.jwtService = jwtService;
    }
}