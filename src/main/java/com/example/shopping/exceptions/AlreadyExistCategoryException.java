package com.example.shopping.exceptions;

public class AlreadyExistCategoryException extends RuntimeException{
    public AlreadyExistCategoryException(String message) {
        super(message);
    }
}
