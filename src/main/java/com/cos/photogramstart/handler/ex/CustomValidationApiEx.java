package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationApiEx extends RuntimeException{

    private static final long serialVersionUID=1L;

    private Map<Object, Object> errorMap;

    public CustomValidationApiEx(String message, Map<Object, Object> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public CustomValidationApiEx(String message) {
        super(message);
    }

    public Map<Object, Object> getErrorMap() {
        return errorMap;
    }
}
