package com.springboard.zzatmari.src.plant;

import com.springboard.zzatmari.src.plant.PlantDao;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantProvider {

    private final PlantDao plantDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PlantProvider(PlantDao plantDao, JwtService jwtService) {
        this.plantDao = plantDao;
        this.jwtService = jwtService;
    }
}