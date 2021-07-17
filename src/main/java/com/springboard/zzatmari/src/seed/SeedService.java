package com.springboard.zzatmari.src.seed;

import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeedService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SeedDao seedDao;
    private final SeedProvider seedProvider;
    private final JwtService jwtService;


    @Autowired
    public SeedService(SeedDao seedDao, SeedProvider seedProvider, JwtService jwtService) {
        this.seedDao = seedDao;
        this.seedProvider = seedProvider;
        this.jwtService = jwtService;

    }
}