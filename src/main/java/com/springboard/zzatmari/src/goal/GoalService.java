package com.springboard.zzatmari.src.goal;

import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoalService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GoalDao goalDao;
    private final GoalProvider goalProvider;
    private final JwtService jwtService;


    @Autowired
    public GoalService(GoalDao goalDao, GoalProvider goalProvider, JwtService jwtService) {
        this.goalDao = goalDao;
        this.goalProvider = goalProvider;
        this.jwtService = jwtService;

    }

}

