package com.example.messages.dto;

import java.sql.Timestamp;
import java.time.Instant;

public class Response<T> {
    public boolean isSuccess;
    public String Message;
    public T Data;
    public Instant Timestamp;

    public Response(boolean isSuccess, String Message, T Data, Instant Timestamp) {
        this.Data = Data;
        this.Message = Message;
        this.isSuccess = isSuccess;
        Timestamp = this.Timestamp;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        this.Message = message;
    }

    public T getData() {
        return Data;
    }
    public void setData(T data) {
        this.Data = data;
    }

    public Instant getTimestamp() {
        return Timestamp;
    }
    public void setTimestamp(Instant time) {
        this.Timestamp = time;
    }
}
