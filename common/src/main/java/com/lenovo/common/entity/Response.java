package com.lenovo.common.entity;

/**
 * Created by noahkong on 17-6-5.
 */

public class Response<T> {
    public int code;
    public String msg;
    public T data;
}
