package com.capstone.arfly.common.exception;

public class WithdrawnMemberException extends BusinessException {
    public WithdrawnMemberException() {
        super(ErrorCode.WITHDRAWN_MEMBER);
    }
}
