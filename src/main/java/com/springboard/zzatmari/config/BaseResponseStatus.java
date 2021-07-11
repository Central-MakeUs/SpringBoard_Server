package com.springboard.zzatmari.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "성공"),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요"),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요"),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다"),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다"),

    // users
    USERS_TIME_INVALID(false, 2010, "시간단위를 확인해주세요"),

    //lists
    LISTS_TYPE_ERROR_TYPE(false, 2020, "리스트 타입은 0 또는 1값을 입력해주세요"),
    LISTS_ITEM_EMPTY(false, 2021, "아이템을 1개 이상 입력해주세요"),
    POST_LISTS_EXIST_NAME(false, 2022, "이미 존재하는 리스트 항목입니다"),

    //timers
    TIMERS_TIME_EMPTY(false, 2030, "타이머 시간을 1분이상 입력해주세요"),
    TIMERS_TIME_ERROR_TYPE(false, 2031, "시간 형식을 올바르게 입력해주세요"),
    TIMERS_EXIST_TIME(false, 2032, "이미 존재하는 타이머입니다"),
    TIMERS_ID_EMPTY(false, 2033, "timerIdx를 입력해주세요"),

    //goals
    GOALS_LIST_ID_EMPTY(false, 2040, "listIdx를 입력해주세요"),
    GOALS_TIME_ERROR_TYPE(false, 2041, "목표시간은 0분 이상 입력해주세요"),

    // sample
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요"),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요"),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요"),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다"),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다"),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다"),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다"),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다"),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다"),
    REQUEST_FAIL(false,4002,"요청에 실패했습니다"),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다"),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
