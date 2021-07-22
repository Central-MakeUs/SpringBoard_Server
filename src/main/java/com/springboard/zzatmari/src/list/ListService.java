package com.springboard.zzatmari.src.list;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.src.list.model.*;
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

        //리스트 중복체크
        if(postListReq.getListItem() != null) {

            Lists list = listProvider.checkListItem(userIdx, postListReq.getListItem());

            //리스트 중복체크
            if(list.getCount() > 0){

                if(list.getStatus()==0)
                    throw new BaseException(LISTS_EXIST_NAME);

                //삭제된 상태면 살리기
                listDao.updateListStatus(list.getListIdx());
                return new PostListRes(list.getListIdx());
            }
        }

        try{
            int listIdx = listDao.insertLists(postListReq, userIdx);
            return new PostListRes(listIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //리스트 수정
    public void modifyList(int userIdx, int listIdx, PatchListReq patchListReq) throws BaseException {

        //리스트 idx 체크
        Lists check = listProvider.checkListIdx(listIdx);
        if(check.getCount() == 0)
            throw new BaseException(LIST_ID_NOT_EXIST);
        else if(check.getUserIdx() != userIdx)
            throw new BaseException(LIST_USER_NOT_MATCH);

        Lists list = listProvider.checkListItem(userIdx, patchListReq.getListItem());

        //리스트 중복체크
        if(list.getCount() > 0){
            if(list.getStatus()==0)
                throw new BaseException(LISTS_EXIST_NAME);
            if(list.getStatus()==1)
                throw new BaseException(LISTS_EXIST_NAME_DELETED);
        }

        try{
            int result = listDao.updateList(listIdx, patchListReq);
            if(result != 1)
                throw new BaseException(REQUEST_FAIL);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //리스트 삭제
    public void deleteList(int userIdx, int listIdx) throws BaseException {

        //리스트 idx 체크
        Lists check = listProvider.checkListIdx(listIdx);
        if(check.getCount() == 0)
            throw new BaseException(LIST_ID_NOT_EXIST);
        else if(check.getUserIdx() != userIdx)
            throw new BaseException(LIST_USER_NOT_MATCH);
        else if(check.getStatus() == 1)
            throw new BaseException(LIST_STATUS_ERROR_TYPE);

        try{
            int result = listDao.deleteList(listIdx);
            if(result != 1)
                throw new BaseException(REQUEST_FAIL);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

