package com.hy.demo.component.exception;

/**
 * Created by hy on 2019/9/10 9:14
 */
public class CustomException extends RuntimeException {

    public CustomException(String msg){
        super(msg);
    }

    public CustomException() {
        super();
    }
}