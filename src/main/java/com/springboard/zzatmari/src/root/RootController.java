package com.springboard.zzatmari.src.root;

import com.springboard.zzatmari.config.BaseResponseStatus;
import com.springboard.zzatmari.src.root.model.GetAutoLogin;
import com.springboard.zzatmari.src.root.model.PostSignUpReq;
import com.springboard.zzatmari.src.root.model.PostSignUpRes;
import com.springboard.zzatmari.src.user.UserProvider;
import com.springboard.zzatmari.src.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;
import static com.springboard.zzatmari.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("")
public class RootController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RootProvider rootProvider;
    @Autowired
    private final RootService rootService;
    @Autowired
    private final JwtService jwtService;


    public RootController(RootProvider rootProvider, RootService rootService, JwtService jwtService){
        this.rootProvider = rootProvider;
        this.rootService = rootService;
        this.jwtService = jwtService;
    }

    /**
     * 자동로그인 API
     * [GET] /auto-login
     * @return BaseResponse<GetGAutoLogin>
     */
    @ResponseBody
    @GetMapping("/auto-login")
    public BaseResponse<GetAutoLogin> autoLogin() {
        try{
            int userIdx = jwtService.getUserIdx();
            GetAutoLogin getAutoLogin = new GetAutoLogin(userIdx);
            return new BaseResponse<>(getAutoLogin);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 이메일 회원가입 API
     * [GET] /sign-up
     * @return BaseResponse<PostSignUpRes>
     */
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<PostSignUpRes> signUp(@RequestBody PostSignUpReq postSignUpReq) {
        try{

            if(postSignUpReq.getEmail() == null)
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            if(!isRegexEmail(postSignUpReq.getEmail()))
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);

            if(postSignUpReq.getPassword() == null)
                return new BaseResponse<>(POST_USERS_PASSWORD_EMPTY);
            if(postSignUpReq.getPassword().length() < 6 || postSignUpReq.getPassword().length() > 15)
                return new BaseResponse<>(POST_USERS_PASSWORD_LENGTH);

            PostSignUpRes postSignUpRes = rootService.createUser(postSignUpReq);
            return new BaseResponse<>(postSignUpRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}


