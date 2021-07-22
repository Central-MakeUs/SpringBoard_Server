package com.springboard.zzatmari.src.root;

import com.springboard.zzatmari.src.root.model.GetAutoLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class RootController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final JwtService jwtService;

    public RootController(JwtService jwtService){

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


}


