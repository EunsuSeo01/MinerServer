package com.umc.miner.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    // 전화번호
    public static boolean isRegexPhoneNum(String target) {
        String regex = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
        Pattern pattern = Pattern.compile(regex); // compile(String regex) : 주어진 정규 표현식으로부터 패턴 생성
        Matcher matcher = pattern.matcher(target);
        return matcher.find(); // find() : 대상 문자열과 패턴이 일치하는 경우 true 반환
    }

    // 이메일
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); // Pattern.CASE_INSENSITIVE : 대소문자 구분 X
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // YYYY-MM-DD
    public static boolean isRegexDate(String target) {
        String regex = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); // Pattern.CASE_INSENSITIVE : 대소문자 구분 X
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 비밀번호
    // 최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자 -> 비밀번호
    public static boolean isRegexPassword(String target) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[$@$!%*#?&^])[A-Za-z\\\\d$@$!%*#?&^]{8,20}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); // Pattern.CASE_INSENSITIVE : 대소문자 구분 X
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 닉네임
    public static boolean isRegexNickName(String target) {
        String regex = "^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,5}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); // Pattern.CASE_INSENSITIVE : 대소문자 구분 X
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

}
