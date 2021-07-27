package com.springboard.zzatmari.src.root;

import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.BaseResponse;
import com.springboard.zzatmari.config.secret.Secret;
import com.springboard.zzatmari.src.root.RootDao;
import com.springboard.zzatmari.src.root.RootProvider;
import com.springboard.zzatmari.src.root.model.PostSignUpReq;
import com.springboard.zzatmari.src.root.model.PostSignUpRes;
import com.springboard.zzatmari.src.user.UserProvider;
import com.springboard.zzatmari.src.user.model.PostUserRes;
import com.springboard.zzatmari.src.user.model.UserEmail;
import com.springboard.zzatmari.utils.AES128;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

@Service
public class RootService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RootDao rootDao;
    private final RootProvider rootProvider;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public RootService(RootDao rootDao, RootProvider rootProvider, UserProvider userProvider, JwtService jwtService) {
        this.rootDao = rootDao;
        this.rootProvider = rootProvider;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //이메일 회원가입
    public PostSignUpRes createUser(PostSignUpReq postSignUpReq) throws BaseException {

        //이메일 중복체크
        UserEmail userEmail = userProvider.checkUserEmail(postSignUpReq.getEmail());
        if(userEmail.getCount() != 0){
            if(userEmail.getLoginType() > 1)
                throw new BaseException(POST_USERS_EMAIL_EXIST_SOCIAL);
            else
                throw new BaseException(POST_USERS_EMAIL_EXIST);
        }

        String password;
        try{
            //암호화
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postSignUpReq.getPassword());
            postSignUpReq.setPassword(password);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try{
            //회원가입
            int userIdx=rootDao.insertUser(postSignUpReq.getEmail(), postSignUpReq.getPassword());

            String jwt = jwtService.createJwt(userIdx);
            return new PostSignUpRes(jwt,userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}