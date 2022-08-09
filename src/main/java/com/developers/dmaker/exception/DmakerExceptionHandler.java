package com.developers.dmaker.exception;

import com.developers.dmaker.dto.DmakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.developers.dmaker.exception.DmakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.developers.dmaker.exception.DmakerErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class DmakerExceptionHandler {

    // 커스텀 Exception
    // 비즈니스 검증시 예외처리
    @ExceptionHandler(DmakerException.class)
    public DmakerErrorResponse handleDmakerException(
            DmakerException e,
            HttpServletRequest request
    ) {
        log.error("errorCode: {}, url: {}, message: {}",
                e.getDmakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DmakerErrorResponse.builder()
                .errorCode(e.getDmakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    // 잘못된 Http method 사용시 예외처리
    // 데이터 검증시 예외처리
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DmakerErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ){
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return DmakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    // 알 수 없는 오류
    @ExceptionHandler(Exception.class)
    public DmakerErrorResponse handleException(
            Exception e, HttpServletRequest request
    ){
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return DmakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
