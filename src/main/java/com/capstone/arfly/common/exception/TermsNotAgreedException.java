package com.capstone.arfly.common.exception;

public class TermsNotAgreedException extends BusinessException {
    public TermsNotAgreedException() {
        super(ErrorCode.TERMS_NOT_AGREED);
    }
}
