package com.springboard.zzatmari.src.stat;

import com.springboard.zzatmari.src.user.UserProvider;
import com.springboard.zzatmari.src.user.UserService;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StatProvider statProvider;
    @Autowired
    private final JwtService jwtService;


    public StatController(StatProvider statProvider, JwtService jwtService) {
        this.statProvider = statProvider;
        this.jwtService = jwtService;
    }
}