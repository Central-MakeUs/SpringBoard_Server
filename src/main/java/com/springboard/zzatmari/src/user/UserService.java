package com.springboard.zzatmari.src.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.springboard.zzatmari.config.BaseException;
import com.springboard.zzatmari.config.secret.Secret;
import com.springboard.zzatmari.config.Constant;
import com.springboard.zzatmari.src.seed.SeedDao;
import com.springboard.zzatmari.src.user.model.*;
import com.springboard.zzatmari.utils.AES128;
import com.springboard.zzatmari.utils.JwtService;
import com.sun.mail.util.logging.MailHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import static com.springboard.zzatmari.config.BaseResponseStatus.*;
import static com.springboard.zzatmari.config.Constant.mailTitle;

// Service Create, Update, Delete 의 로직 처리
@Service
@AllArgsConstructor
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final SeedDao seedDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "ssy4230@gmail.com";

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

    //카카오 로그인
    public PostUserRes createKakaoUser(PostUserReq postUserReq) throws BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String email = "";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + postUserReq.getToken());

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);


            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            br.close();

        } catch (IOException e) {
            throw new BaseException(KAKAO_LOGIN_FAIL);
        }

        //이메일 중복체크
        int userIdx = 0;
        UserEmail userEmail = userProvider.checkUserEmail(email);
        if(userEmail.getCount() != 0) {
            if(userEmail.getLoginType() == 1)
                throw new BaseException(POST_USERS_EMAIL_EXIST_EMAIL);

            userIdx = userEmail.getUserIdx();
        }

        try{

            if(userEmail.getCount() == 0){ //회원가입 진행
                userIdx = userDao.createKakaoUser(email);
                createUserDefault(userIdx);
            }

            String jwt = createJWT(userIdx);
            return new PostUserRes(jwt,userIdx);

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

    //비밀번호 찾기 메일 전송
    public void sendEmail(int userIdx) throws BaseException {
        //사용자 이메일 조회
        String email = userProvider.getUser(userIdx).getEmail();
        if(email == null){
            throw new BaseException(USERS_EMAIL_NOT_EXIST);
        }
        try{
            MailHandler mailHandler = new MailHandler((Properties) mailSender);

            /*// 받는 사람
            mailHandler.setTo("ssy4230@naver.com");
            // 제목
            mailHandler.setSubject(mailTitle);
            // HTML Layout
            String htmlContent = "<p>" + "" +"<p> <img src='cid:sample-img'>";
            mailHandler.setText(htmlContent, true);
            mailHandler.send();*/
        }
        catch(Exception e){
            e.printStackTrace();
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
