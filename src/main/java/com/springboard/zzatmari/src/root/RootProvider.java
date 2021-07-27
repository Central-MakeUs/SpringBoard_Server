package com.springboard.zzatmari.src.root;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class RootProvider {

    private final RootDao rootDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RootProvider(RootDao rootDao, JwtService jwtService) {
        this.rootDao = rootDao;
        this.jwtService = jwtService;
    }

}
