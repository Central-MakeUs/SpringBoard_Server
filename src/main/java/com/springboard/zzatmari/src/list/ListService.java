package com.springboard.zzatmari.src.list;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.list.model.PostListReq;
import com.springboard.zzatmari.src.list.model.PostListRes;
import com.springboard.zzatmari.src.user.model.PostUserTimeReq;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;
import static com.springboard.zzatmari.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ListService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ListDao listDao;
    private final ListProvider listProvider;
    private final JwtService jwtService;


    @Autowired
    public ListService(ListDao listDao, ListProvider listProvider, JwtService jwtService) {
        this.listDao = listDao;
        this.listProvider = listProvider;
        this.jwtService = jwtService;

    }

    //리스트 등록
    public PostListRes createLists(PostListReq postListReq, int userIdx) throws BaseException {

        try{
            //리스트 중복체크
            if(postListReq.getListItem() != null && listProvider.checkListItem(userIdx, postListReq.getListItem()) == 1){
                System.out.println("ji");
                throw new BaseException(POST_LISTS_EXIST_NAME);
            }
            System.out.println("ji");
            int listIdx = listDao.insertLists(postListReq, userIdx);

            return new PostListRes(listIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

