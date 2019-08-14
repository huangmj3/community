package com.huangmj.community.exception;

import lombok.Data;

//问题找不到所引起的异常
//因为继承了运行时异常，所以可以不使用try catch的时候直接进行异常抛出
//@Data
public class CustomizeException extends RuntimeException{
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
