package com.springboard.zzatmari.src.execution;

import com.springboard.zzatmari.src.goal.GoalDao;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutionProvider {

    private final ExecutionDao executionDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ExecutionProvider(ExecutionDao executionDao, JwtService jwtService) {
        this.executionDao = executionDao;
        this.jwtService = jwtService;
    }
}
