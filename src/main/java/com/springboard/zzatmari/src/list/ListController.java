package com.springboard.zzatmari.src.list;

import com.springboard.zzatmari.src.list.model.PostListReq;
import com.springboard.zzatmari.src.list.model.PostListRes;
import com.springboard.zzatmari.src.user.UserProvider;
import com.springboard.zzatmari.src.user.UserService;
import com.springboard.zzatmari.src.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.springboard.zzatmari.config.BaseResponseStatus.*;
import static com.springboard.zzatmari.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/lists")
public class ListController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ListProvider listProvider;
    @Autowired
    private final ListService listService;
    @Autowired
    private final JwtService jwtService;

    public ListController(ListProvider listProvider, ListService listService, JwtService jwtService){
        this.listProvider = listProvider;
        this.listService = listService;
        this.jwtService = jwtService;
    }

    /**
     * 리스트 등록 API
     * [POST] /lists
     * @return BaseResponse<PostListRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostListRes> createLists(@RequestBody PostListReq postListReq) {

        int userIdx = 1;

        //빈값 체크
        if(postListReq.getListItem() == null && postListReq.getListItems() == null){
            return new BaseResponse<>(LISTS_ITEM_EMPTY);
        }

        //형식 체크
        if(postListReq.getListType() != 0 && postListReq.getListType() != 1){
            return new BaseResponse<>(LISTS_TYPE_ERROR_TYPE);
        }

        try{
            PostListRes response = listService.createLists(postListReq, userIdx);
            return new BaseResponse<PostListRes>(response);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

