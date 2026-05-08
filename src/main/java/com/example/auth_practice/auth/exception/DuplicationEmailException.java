package com.example.auth_practice.auth.exception;

public class DuplicationEmailException extends RuntimeException{
    public DuplicationEmailException(String message){
        super(message);
    }
}
