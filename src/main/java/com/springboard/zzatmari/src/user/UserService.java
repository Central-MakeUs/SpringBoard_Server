package com.springboard.zzatmari.src.user;



import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.secret.Secret;
import com.springboard.zzatmari.src.seed.SeedDao;
import com.springboard.zzatmari.src.user.model.*;
import com.springboard.zzatmari.utils.AES128;
import com.springboard.zzatmari.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final SeedDao seedDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, SeedDao seedDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.seedDao = seedDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //하루 시작시간 등록
    public int updateUserTime(PostUserTimeReq postUserTimeReq, int userIdx) throws BaseException {

        try{
            int isSuccess = userDao.updateUserTime(postUserTimeReq, userIdx);
            return isSuccess;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //JWT 발급
    public String createJWT(int userIdx) throws BaseException {

        try{
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return jwt;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //비회원 회원가입, 로그인
    public PostUserRes createUnknownUser(PostUserReq postUserReq) throws BaseException {

        UserDeviceId userDeviceId = userProvider.checkDeviceId(postUserReq.getToken());
        int userIdx = userDeviceId.getUserIdx();

        try{

            //중복
            if(userDeviceId.getCount() == 0){ //회원가입 진행
                userIdx = userDao.createUnknownUser(postUserReq.getToken());
                createUserDefault(userIdx);
            }

            String jwt = createJWT(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //이메일 로그인
    public PostUserRes loginEmailUser(PostUserReq postUserReq) throws BaseException {

        UserEmail userEmail = userProvider.checkUserEmail(postUserReq.getEmail());

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        //이메일, 비밀번호 확인
        if(userEmail.getCount() > 0 && userEmail.getLoginType() != 1)
            throw new BaseException(POST_USERS_EMAIL_EXIST_SOCIAL);

        if(userEmail.getCount() == 0 || !(userEmail.getPassword().equals(postUserReq.getPassword())))
            throw new BaseException(USERS_LOGIN_NOT_MATCH);

        try{
            //jwt 발급.
            String jwt = jwtService.createJwt(userEmail.getUserIdx());
            return new PostUserRes(jwt,userEmail.getUserIdx());
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //새로 추가된 사용자에 대한 기본값 세팅
    public void createUserDefault(int userIdx) throws BaseException {

        try{
            //기본타이머 세팅
            userDao.insertUserDefault(userIdx);

            //기본씨앗 세팅
            seedDao.insertSeedDefault(userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EMAIL_EXIST);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //비밀번호 변경
    public void modifyPassword(int userIdx, PatchUserPasswordReq patchUserPasswordReq) throws BaseException {


            //비밀번호 확인
            User user = userProvider.checkUser(userIdx);

            String pwd;
            try{
                //암호화
                pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchUserPasswordReq.getNowPassword());
                patchUserPasswordReq.setNowPassword(pwd);
            } catch (Exception ignored) {
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }

            if(!(user.getPassword().equals(patchUserPasswordReq.getNowPassword())))
                throw new BaseException(USERS_PASSWORD_NOT_MATCH);

            try{
                //암호화
                pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchUserPasswordReq.getNewPassword());
                patchUserPasswordReq.setNewPassword(pwd);
            } catch (Exception ignored) {
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }

        try{
            int result = userDao.modifyPassword(userIdx, patchUserPasswordReq.getNewPassword());
            if(result == 0){
                throw new BaseException(REQUEST_FAIL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
