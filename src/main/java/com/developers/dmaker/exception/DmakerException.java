package com.developers.dmaker.exception;

import lombok.Getter;

@Getter
public class DmakerException extends RuntimeException{
    private DmakerErrorCode dmakerErrorCode;
    private String detailMessage;

    public DmakerException(DmakerErrorCode errorCode){
        super(errorCode.getMessage());
        this.dmakerErrorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

    public DmakerException(DmakerErrorCode errorCode, String detailMessage){
        super(errorCode.getMessage());
        this.dmakerErrorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
