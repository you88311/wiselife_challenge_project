package be.wiselife.exception;

import lombok.Getter;

public enum ExceptionCode {
    /*
     * 필요시 추가로 구현하시면 됩니다 에러이름(code, message)
     */

    //Member 부분 예외 ==============시작
    MEMBER_NOT_FOUND(404, "Member not exists"),
    MEMBER_NAME_ALREADY_EXISTS(400, "This memberName already exists");
    //Member 부분 예외 ==============끝
    @Getter
    private int code;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
