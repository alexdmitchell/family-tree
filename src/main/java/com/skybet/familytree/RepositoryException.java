package com.skybet.familytree;

public class RepositoryException extends RuntimeException{
    private String errorMsg;

    public RepositoryException(String errorMsg)
    {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
