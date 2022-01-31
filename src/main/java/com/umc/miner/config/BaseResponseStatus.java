package com.umc.miner.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    POST_USERS_AVAILABLE_EMAIL(true, 1001,"사용가능한 이메일입니다."),
    POST_USERS_AVAILABLE_NAME(true, 1002,"사용가능한 닉네임입니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003,"권한이 없는 유저의 접근입니다."),

    // 로그인
    USERS_EMPTY_USER_EMAIL(false, 2010, "이메일을 입력해주세요."),
    USERS_INACTIVE_USER_EMAIL(false, 2012,"활성화된 계정이 아닙니다."),

    // 이메일 중복확인
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),

    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_NOT_EXISTS_EMAIL(false,2031,"존재하지 않는 이메일입니다."),


    // 닉네임 중복확인
    POST_USERS_EMPTY_NAME(false, 2018, "닉네임을 입력해주세요."),
    POST_USERS_INVALID_NAME(false, 2019, "6자 미만으로 설정해주세요."),
    POST_USERS_EXISTS_NAME(false, 2020,"이미 존재하는 닉네임입니다."),

    // 전화번호 형식 확인
    INVALID_PHONENUM(false, 2025, "전화번호 형식을 확인해주세요."),

    NOT_MATCHED_AUTH(false, 2030, "인증번호가 일치하지 않습니다."),

    POST_USERS_EMPTY_PASSWORD(false, 2032, "비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2033,"비밀번호 형식을 확인해주세요."),


    // [PATCH] /users
    PATCH_USERS_INVALID_PASSWORD(false, 2034, "비밀번호 형식을 확인해주세요."),
    PATCH_USERS_EMPTY_EMAIL(false, 2035, "이메일을 입력해주세요."),

    // [POST] /email
    POST_EMAIL_EMPTY_EMAIL(false, 2021, "인증번호를 입력해주세요"),
    POST_EMAIL_FAIL_EMAIL(false, 2022, "인증번호가 틀립니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    USERS_NOT_EXISTS_EMAIL(false,3017,"없는 이메일 정보입니다."),

    NOT_REGISTERED_PHONE_NUMBER(false, 3015, "해당 전화번호로 가입된 아이디가 없습니다."),
    FAILED_TO_MSG(false, 3016, "메세지 전송에 실패하였습니다."),




    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    UNEXPECTED_ERROR(false, 4002, "예상치못한 에러가 발생했습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_PW(false,4015,"비밀번호 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    // [PATCH] /playmaps
    FAILED_TO_SHARE_MAP(false, 4020, "맵 공유는 최대 3개까지 가능합니다."),
    FAILED_TO_MODIFY_SHARED_MAP(false, 4021, "공유 수정에 실패하였습니다."),
    FAILED_TO_DELETE_SHARED_MAP(false, 4022, "공유 삭제에 실패하였습니다."),
    FAILED_TO_DELETE_PLAY_TIME(false, 4023, "플레이 정보 삭제에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
