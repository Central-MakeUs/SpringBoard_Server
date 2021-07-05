package com.springboard.zzatmari.src.list;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.user.model.GetUserRes;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class ListProvider {

    private final ListDao listDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ListProvider(ListDao listDao, JwtService jwtService) {
        this.listDao = listDao;
        this.jwtService = jwtService;
    }

    //리스트 중복 조회
    public int checkListItem(int userIdx, String listItem) throws BaseException{
        try{
            int checkListItemRes = listDao.checkListItem(userIdx, listItem);
            System.out.println("ji");
            return checkListItemRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
