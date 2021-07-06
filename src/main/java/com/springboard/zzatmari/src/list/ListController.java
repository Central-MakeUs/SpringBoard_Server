package com.springboard.zzatmari.src.list;

import com.springboard.zzatmari.src.list.model.GetListsRes;
import com.springboard.zzatmari.src.list.model.PostListReq;
import com.springboard.zzatmari.src.list.model.PostListRes;
import com.springboard.zzatmari.src.user.model.GetUserRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

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
    public BaseResponse<PostListRes> createLists(@RequestBody PostListReq postListReq) throws BaseException {

        int userIdx = jwtService.getUserIdx();

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

    /**
     * 리스트 조회 API
     * [GET] /lists?type=
     * query = type:[디지털디톡스, 자기계발, 목표]
     * @return BaseResponse<List<GetListsRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetListsRes>> getLists(@RequestParam(required = false) int type) {
        try{

            int userIdx = jwtService.getUserIdx();

            if(type < 0 || type > 2){
                return new BaseResponse<>(LISTS_TYPE_ERROR_TYPE);
            }

            List<GetListsRes> response = listProvider.getLists(userIdx, type);
            return new BaseResponse<>(response);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
