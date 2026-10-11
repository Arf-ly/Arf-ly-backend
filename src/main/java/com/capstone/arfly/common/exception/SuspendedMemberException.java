package com.capstone.arfly.common.exception;

public class SuspendedMemberException extends BusinessException {
    public SuspendedMemberException() {
        super(ErrorCode.SUSPENDED_MEMBER);
    }
}
